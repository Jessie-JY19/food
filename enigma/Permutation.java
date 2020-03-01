package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jessie Yu
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        checkCycles(cycles);
        _cycles = cycles;
        mutated = mutate();
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String newcyc = "(" + cycle + ")";
        checkCycles(newcyc);
        _cycles = newcyc + _cycles;

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
        char pc = _alphabet.toChar(p);
        return mutated.toInt(pc);


    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char invert = mutated.toChar(c);
        return _alphabet.toInt(invert);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int index =  mutated.toInt(p);
        return _alphabet.toChar(index);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int index = _alphabet.toInt(c);
        char inverse = mutated.toChar(index);
        return inverse;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (_alphabet.toChar(i) == mutated.toChar(i)) {
                return false;
            }
        }
        return true;
    }

    /** @param c  */
    void checkCycles(String c) {
        int paren = 0;
        for (int i = 0; i < c.length(); i++) {
            if (c.charAt(i) == '(') {
                paren++;
            } else if (c.charAt(i) == ')') {
                if (paren == 1) {
                    paren--;
                }
            } else if (c.charAt(i) == ' ') {
                continue;
            } else {
                if (!_alphabet.contains(c.charAt(i))) {
                    throw error("illegal char in cycles");
                }
                for (int j = i + 1; j < c.length(); j++) {
                    if (c.charAt(i) == c.charAt(j)) {
                        throw error("wrong cycles: repetition");
                    }
                }
            }
        }
        if (paren != 0) {
            throw error("wrong cycles: parentheses not match");
        }
    }

    /** returns the mutated alphabet. */
    Alphabet getMutated() {
        return mutated;
    }

    /** Execute cycles on alphabet to get to see what each char becomes.
     * @return
     */
    Alphabet mutate() {
        char[] buffer = new char[_alphabet.size()];
        int left = 0;
        for (int i = 0; i < _cycles.length() - 1; i++) {
            if (_cycles.charAt(i) == '(') {
                i++;
                left = i;
            }
            if (_cycles.charAt(i) == ')') {
                i++;
            }
            if (_cycles.charAt(i) == ' ') {
                i++;
            }
            if (_cycles.charAt(i) == '(') {
                i++;
                left = i;
            }
            char curr = _cycles.charAt(i);
            char next = _cycles.charAt(i + 1);
            if (next == ')') {
                next = _cycles.charAt(left);
            }

            int pos = _alphabet.toInt(next);
            buffer[pos] = curr;
        }
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == '\u0000') {
                buffer[i] = _alphabet.toChar(i);
            }
        }
        String chara = new String(buffer);
        return new Alphabet(chara);
    }

    /** The alphabet used in this permutation. */
    private Alphabet _alphabet;

    /** The cycles to permute the alphabet. */
    private String _cycles;

    /** What the alphabet looks like after permutation. */
    private Alphabet mutated;
}
