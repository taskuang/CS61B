package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Tasman Kuang
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
            throw new EnigmaException("Either invalid input or "
                   + "no rotors have been passed into machine");
        }
        String setting = _input.nextLine();
        setUp(enigma, setting);
        while (_input.hasNextLine()) {
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
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Rotor slot is unreachable");
            }
            int slots = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Pawls are unreachable");
            }
            int pawl = _config.nextInt();

            if (pawl >= slots) {
                throw new EnigmaException("Too many pawls entered");
            }
            ArrayList<Rotor> rotors = new ArrayList<>();
            _current = _config.next();
            while (_config.hasNext()) {
                rotors.add(readRotor());
            }
            return new Machine(_alphabet, slots, pawl, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _current;
            if (name.contains("(")) {
                throw new EnigmaException("Invalid configuration");
            }
            _current = _config.next();
            String notch = _current;
            _current = _config.next();
            String strPerm = "";
            while (_current.contains("(")) {
                strPerm += _current + " ";
                if (!_config.hasNext()) {
                    strPerm += _current;
                    break;
                } else {
                    _current = _config.next();
                }
            }
            Permutation perm = new Permutation(strPerm, _alphabet);
            if (notch.charAt(0) == 'M') {
                return new MovingRotor(name, perm, notch.substring(1));
            } else if (notch.charAt(0) == 'N') {
                return new FixedRotor(name, perm);
            } else if (notch.charAt(0) == 'R') {
                return new Reflector(name, perm);
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
        String[] allSettings = settings.split(" ");
        String[] allRotors = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i++) {
            allRotors[i] = allSettings[i + 1];
        }
        for (String r: allRotors) {
            if (r.contains("(")) {
                throw new EnigmaException("Invalid configuration");
            }
        }
        M.insertRotors(allRotors);
        M.setRotors(allSettings[M.numRotors() + 1]);
        String setting = "";
        for (int i = M.numRotors() + 2; i < allSettings.length; i++) {
            setting += allSettings[i] + " ";
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

    /** Last configuration input. */
    private String _current;
}

