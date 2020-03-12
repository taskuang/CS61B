package enigma;

import java.util.ArrayList;
import java.util.Arrays;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String str = cycles.replaceAll("[()]", "");
        _cycles = new ArrayList<>(Arrays.asList(str.split(" ")));
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles.add(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char charIn = _alphabet.toChar(wrap(p));
        for (String cycle: _cycles) {
            int index = cycle.indexOf(charIn);
            if (index > -1) {
                index += 1;
                if (index >= cycle.length()) {
                    index = 0;
                }
                return _alphabet.toInt(cycle.charAt(index));
            }
        }
        return wrap(p);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char charIn = _alphabet.toChar(wrap(c));
        for (String cycle: _cycles) {
            int index = cycle.indexOf(charIn);
            if (index > -1) {
                index -= 1;
                if (index == -1) {
                    index = cycle.length() - 1;
                }
                return _alphabet.toInt(cycle.charAt(index));
            }
        }
        return wrap(c);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int index = _alphabet.toInt(p);
        return _alphabet.toChar(permute(index));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int index = _alphabet.toInt(c);
        return _alphabet.toChar(invert(index));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (String cycle: _cycles) {
            if (cycle.length() == 1) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
    private ArrayList<String> _cycles;
}
