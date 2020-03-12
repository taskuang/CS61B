package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.ArrayList;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Adam Wu
 */

public class MachineTest {
    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    Alphabet alpha = UPPER;
    ArrayList<Rotor> testList = new ArrayList<Rotor>(5);
    Reflector reflectoR =
            new Reflector("B", new Permutation(NAVALA.get("B"), alpha));
    FixedRotor fixRotor =
            new FixedRotor("Beta", new Permutation(NAVALA.get("Beta"), alpha));
    MovingRotor movRotor1 = new MovingRotor("III",
            new Permutation(NAVALA.get("III"), alpha), "V");
    MovingRotor movRotor2 = new MovingRotor("II",
            new Permutation(NAVALA.get("II"), alpha), "E");
    MovingRotor movRotor3 = new MovingRotor("I",
            new Permutation(NAVALA.get("I"), alpha), "Q");
    MovingRotor movRotor4 = new MovingRotor("IV",
            new Permutation(NAVALA.get("IV"), alpha), "J");

    @Test
    public void onePerm() {
        testList.add(reflectoR);
        testList.add(fixRotor);
        testList.add(movRotor1);
        testList.add(movRotor3);
        testList.add(movRotor4);
        Machine machine1 = new Machine(alpha, 5, 3, testList);
        String[] rotorS = new String[]{"B", "Beta", "III", "IV", "I"};
        machine1.insertRotors(rotorS);
        machine1.setPlugboard(new Permutation("(YF) (ZH)", alpha));
        machine1.setRotors("AXLE");
        assertEquals(25, machine1.convert(24));
    }


    @Test
    public void testConvertMsg() {
        Alphabet aal = UPPER;
        Rotor one = new Reflector("B",
                new Permutation(
                        "(AE) (BN) (CK) (DQ) (FU) (GY) "
                                + "(HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", aal));
        Rotor two = new FixedRotor("BETA",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", aal));
        Rotor three = new MovingRotor("III",
                new Permutation("(ABDHPEJT) "
                        + "(CFLVMZOYQIRWUKXSG) (N)", aal), "V");
        Rotor four = new MovingRotor("IV",
                new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) "
                        + "(DV) (KU)", aal), "J");
        Rotor five = new MovingRotor("I",
                new Permutation(
                        "(AELTPHQXRU) (BKNW) (CMOY) "
                                + "(DFG) (IV) (JZ) (S)", aal), "Q");
        String setting = "AXLE";
        ArrayList<Rotor> machineRotors = new ArrayList<Rotor>();
        machineRotors.add(one);
        machineRotors.add(two);
        machineRotors.add(three);
        machineRotors.add(four);
        machineRotors.add(five);
        String[] rotors = {"B", "BETA", "III", "IV", "I"};
        Machine mach = new Machine(aal, 5, 3, machineRotors);
        mach.insertRotors(rotors);
        mach.setRotors(setting);
        mach.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)", aal));
        assertEquals("QVPQSOKOILPUBKJZPISFXDW",
                mach.convert("FROMHISSHOULDERHIAWATHA"));
    }

}