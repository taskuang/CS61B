package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new HashMap<String, Rotor>(allRotors.size());
        _myRotors = new ArrayList<Rotor>();
        for (Rotor r: allRotors) {
            _allRotors.put(r.name(), r);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != _numRotors) {
            throw new EnigmaException("Incorrect number of rotors");
        }
        _myRotors.clear();
        int rotates = 0;
        for (String newRotor: rotors){
            if (_myRotors.contains(newRotor)) {
                throw new EnigmaException("Rotors cannot be duplicated");
            }
            if (_allRotors.containsKey(newRotor)) {
                Rotor rotor = _allRotors.get(newRotor);
                if (rotor.rotates()) {
                    rotates++;
                }
                _myRotors.add(rotor);
            }
            else {
                throw new EnigmaException("This rotor does not exist");
            }
        }
        if (!_myRotors.get(0).reflecting()) {
            throw new EnigmaException("The first rotor is not a reflector");
        }
        if (rotates != numPawls()) {
            throw new EnigmaException("Rotor order is incorrect");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Incorrect setting length");
        }
        for (int i = 1; i < setting.length(); i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw new EnigmaException("Setting contains an incorrect value");
            }
            _myRotors.get(i + 1).set(setting.charAt(i));
        }
    }


    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugBoard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        boolean advance = true;
        if (_myRotors.get(_numRotors - 1).atNotch()){
            advance = false;
        }
        for (int i = _numRotors - 1; i > 0; i--) {
            if (_myRotors.get(i).atNotch()) {
                _myRotors.get(i).advance();
                if (!_myRotors.get(i - 1).atNotch()) {
                    _myRotors.get(i - 1).advance();
                }
            }
        }
        if (advance) {
            _myRotors.get(_numRotors - 1).advance();
        }
        c = _plugBoard.permute(c);
        for (int i = _numRotors - 1; i >= 0; i--) {
            c = _myRotors.get(i).convertForward(c);
        }
        for (int i = 1; i <= _numRotors - 1; i++) {
            c = _myRotors.get(i).convertBackward(c);
        }
        return _plugBoard.permute(c);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (char c : msg.toCharArray()) {
            int converted = convert(_alphabet.toInt(c));
            result += _alphabet.toChar(converted);
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    private int _numRotors;

    private int _pawls;

    private HashMap<String, Rotor> _allRotors;

    private ArrayList<Rotor> _myRotors;

    private Permutation _plugBoard;

}
