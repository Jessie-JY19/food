package enigma;

import java.util.Collection;
import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Jessie Yu
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        if (numRotors < 2) {
            throw error("there should be more than one rotors");
        }
        if (pawls < 0 || pawls >= numRotors) {
            throw error("wrong number of pawls");
        }
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors.toArray(new Rotor[_numRotors]);
        slots = new Rotor[numRotors];
        _plugboard = new FixedRotor("pluginitial",
                new Permutation(" ", alpha));

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
        Rotor[] copy = _allRotors.clone();
        if (_numRotors != rotors.length) {
            throw error("wrong number of names");
        }
        for (int i = 0; i < _numRotors; i++) {
            String name = rotors[i];
            boolean nonexist = true;
            for (int j = 0; j < _allRotors.length; j++) {
                String cn = copy[j] != null ? copy[j].name() : " ";
                if (name.equalsIgnoreCase(cn)) {
                    slots[i] = copy[j];
                    copy[j] = null;
                    slots[i].set(0);
                    nonexist = false;
                }
            }
            if (nonexist) {
                throw error("Rotor named " + name + " unavailable.");
            }
            if (i != 0 && slots[i].reflecting()) {
                throw error("misuse of reflector");
            }
            if (i < _numRotors - _pawls && slots[i].rotates()) {
                throw error("the left ones should be fixed");
            }
            if (i >= _numRotors - _pawls && (!slots[i].rotates())) {
                throw error("the right ones should rotate");
            }
        }
        if (!slots[0].reflecting()) {
            slots[0] = null;
            throw error("Invalid reflector");
        }
    }


    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (_numRotors - 1 != setting.length()) {
            throw error("wrong length of setting");
        }
        for (int i = 1; i < _numRotors; i++) {
            slots[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = new FixedRotor("plugboard", plugboard);
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        boolean[] whoAdv = new boolean[_numRotors];
        for (int i = 0; i < _numRotors - 1; i++) {
            if (slots[i + 1].atNotch() && slots[i].rotates()) {
                whoAdv[i + 1] = true;
                whoAdv[i] = true;
            }
        }
        whoAdv[_numRotors - 1] = true;

        for (int i = 0; i < _numRotors; i++) {
            if (whoAdv[i]) {
                slots[i].advance();
            }
        }


        int tp = _plugboard.convertForward(c);
        for (int i = _numRotors - 1; i > 0; i--) {
            tp = slots[i].convertForward(tp);
        }
        tp = slots[0].convertForward(tp);
        for (int j = 1; j < _numRotors; j++) {
            tp = slots[j].convertBackward(tp);
        }
        tp = _plugboard.convertBackward(tp);
        return tp;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        int l = msg.length();
        char[] buffer = new char[l];
        for (int i = 0; i < l; i++) {
            int ind = _alphabet.toInt(msg.charAt(i));
            int converted = convert(ind);
            buffer[i] = _alphabet.toChar(converted);
        }
        return new String(buffer);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The number of rotors used in this machine. */
    private int _numRotors;

    /** The number of pawls, aka moving rotors. */
    private int _pawls;

    /** Contains all available rotors. */
    private Rotor[] _allRotors;

    /** Contains all rotors used, ordered from left to right. */
    private Rotor[] slots;

    /** Consider the plugborad as a fixed rotor with certain permutation. */
    private FixedRotor _plugboard;
}
