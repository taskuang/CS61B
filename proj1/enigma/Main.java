package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine enigma = readConfig();
        if (!_input.hasNext("\\*")) {
            throw new EnigmaException("Incorrect setting");
        }
        String setting = _input.nextLine();
        while (_input.hasNextLine()) {
            /**setUp(enigma, setting);
            setting = _input.nextLine();
            if (!setting.isEmpty()) {
                while (!setting.contains("*") && _input.hasNextLine()) {
                    printMessageLine(enigma.convert(setting));
                    setting = _input.nextLine();
                }
                if (!(setting.isEmpty() && setting.contains("*"))) {
                    printMessageLine(enigma.convert(setting));
                }
            }
            else {
                _output.println();
            }**/
            setting = _input.nextLine();
            if (setting.startsWith("*")) {
                setUp(enigma, setting);
            } else {
                setting = setting.replaceAll("\\s*", "");
                printMessageLine(enigma.convert(setting));
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            if (_alphabet.contains('*') || _alphabet.contains('(') || _alphabet.contains(')')) {
                throw new EnigmaException("Configuration contains illegal characters.");
            }
            if (!_config.hasNext()) {
                throw new EnigmaException("Configuration is empty");
            }
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Rotor is unreachable");
            }
            int slots = _config.nextInt();

            if (!_config.hasNextInt()) {
                throw new EnigmaException("Pawls are unreachable");
            }
            int movingRotors = _config.nextInt();

            if (movingRotors >= slots) {
                throw new EnigmaException("Too many pawls");
            }
            if (!(movingRotors >= 0 || slots >= 0)) {
                throw new EnigmaException("Pawls and rotors need to be greater than 0");
            }

            ArrayList<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }

            return new Machine(_alphabet, slots, movingRotors, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String notch = _config.next();
            /**if (name.contains("(") || name.contains(")")) {
                throw new EnigmaException("Invalid configuration");
            }
            name = _config.next();
            char type = name.charAt(0);
            String notches = name.substring(1);
            if (type == 'M' && notches.isEmpty()) {
                throw new EnigmaException("Moving rotor needs notches.");
            }
            String permutation = "";
            name = _config.next();
            while (name.contains("(") && _config.hasNext()) {
                permutation += name + " ";
                name = _config.next();
            }
            if (!_config.hasNext()) {
                permutation += name;
            }**/
            char type = notch.charAt(0);
            String permutation = "";
            while (_config.hasNext("\\(.*")) {
                permutation += _config.next("(\\([^\\)]*\\))*");
            }
            Permutation perm = new Permutation(permutation, _alphabet);
            if (type == 'N') {
                return new FixedRotor(name, perm);
            } else if (type == 'M') {
                return new MovingRotor(name, perm, notch.substring(1));
            } else if (type == 'M') {
                return new Reflector(notch, perm);
            } else {
                throw new EnigmaException("No rotor type");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] s = settings.split("\\s");
        String[] rotors = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i++) {
            rotors[i] = s[i + 1];
        }
        for (String r: rotors) {
            if (r.contains("(")) {
                throw new EnigmaException("Incorrect config.");
            }
        }
        M.insertRotors(rotors);
        M.setRotors(s[M.numRotors() + 1]);
        String setting = "";
        for (int i = M.numRotors() + 2; i < s.length; i++) {
            if (!s[i].contains("(") || !s[i].contains(")")) {
                throw new EnigmaException("Incorrect plugboard");
            }
            setting += s[i] + " ";
        }
        M.setPlugboard(new Permutation(setting, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            if (msg.length() - i > 5) {
                _output.print(msg.substring(i, i + 5) + " ");
            } else {
                _output.println(msg.substring(i));
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
