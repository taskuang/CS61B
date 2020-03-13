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

    @Test
    public void testIntPermute() {
        perm = new Permutation("(ABCD) (EFGH)", UPPER);
        assertEquals(3, perm.permute(2));
        assertEquals(1, perm.permute(0));
    }

    @Test
    public void testIntInvert() {
        perm = new Permutation("(ABCD) (EFGH)", UPPER);
        assertEquals(0, perm.invert(1));
        assertEquals(2, perm.invert(3));
    }

    @Test
    public void testCharPermute() {
        perm = new Permutation("(ABCD) (EFGH)", UPPER);
        assertEquals('A', perm.permute('D'));
        assertEquals('C', perm.permute('B'));
    }

    @Test
    public void testCharCInvert() {
        perm = new Permutation("(ABCD) (EFGH)", UPPER);
        assertEquals('D', perm.invert('A'));
        assertEquals('B', perm.invert('C'));
    }
}
