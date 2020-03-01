package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author JessieYu
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        int curr = setting();
        char ch = permutation().alphabet().toChar(curr);
        for (int i = 0; i < _notches.length(); i++) {
            if (Character.compare(_notches.charAt(i), ch) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        int curr = setting();
        set((curr + 1) % size());
    }

    /** Returns the notches. */
    String notches() {
        return _notches;
    }

    /** Each char of this string is a notch of the rotor. */
    private String _notches;

}
