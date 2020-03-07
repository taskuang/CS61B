package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }
    public void testSize() {
        Permutation k = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation i = new Permutation("(KA)", new Alphabet());
        Permutation j = new Permutation("(A)", new Alphabet());
        Permutation o = new Permutation("(B)", new Alphabet("ABSDFGETHJ0"));
        assertEquals(26, j.size());
        assertEquals(4, k.size());
        assertEquals(26, i.size());
        assertEquals(11, o.size());
    }

    public void testPermuteInt() {
        Permutation k = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation i = new Permutation("(KA)", new Alphabet());
        Permutation p = new Permutation("(123)", new Alphabet("1234!"));
        assertEquals(0, k.permute(5));
        assertEquals(0, i.permute(10));
        assertEquals(4, p.permute(-1));
    }

    @Test
    public void testInvertInt() {
        Permutation k = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p = new Permutation("(123)", new Alphabet("1234!"));
        Permutation j = new Permutation("(A)", new Alphabet());
        assertEquals('B', p.invert('A'));
        assertEquals('A', p.invert('C'));
        assertEquals('!', p.invert('!'));
        assertEquals('K', p.invert('K'));
    }

    @Test
    public void testPermuteChar() {
        Permutation k = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p = new Permutation("(123)", new Alphabet("1234!"));
        Permutation o = new Permutation("", new Alphabet("AKSIRUDQ"));
        assertEquals('C', p.permute('A'));
        assertEquals('2', p.permute('1'));
        assertEquals('D', p.permute('D'));
    }

    @Test
    public void testInvertChar() {
        Permutation k = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation p = new Permutation("(123)", new Alphabet("1234!"));
        assertEquals(1, p.invert(0));
        assertEquals(3, p.invert(5));
        assertEquals(4, p.invert(-1));
    }

}
