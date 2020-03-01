package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author JessieYu
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        offset = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return offset;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        if (posn > _permutation.size()) {
            throw error("setting index out of bound");
        }
        offset = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        int index = _permutation.alphabet().toInt(cposn);
        if (index == -1) {
            throw error("setting char not exist");
        }
        set(index);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int size = _permutation.size();
        if (p >= size || p < 0) {
            throw error("forward input index out of bound");
        }
        int input = (p + offset) % size;
        int raw = _permutation.permute(input) - offset;
        return _permutation.wrap(raw);
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int size = _permutation.size();
        if (e >= size || e < 0) {
            throw error("backward input index out of bound");
        }
        int output = (e + offset) % size;
        int raw = _permutation.invert(output) - offset;
        return _permutation.wrap(raw);
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** the position setting of this rotor. */
    private int offset;

}
