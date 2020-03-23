import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {

    @Test
    public void testHashString() {
        ECHashStringSet hash = new ECHashStringSet();
        hash.put("d");
        hash.put("c");
        hash.put("a");
        hash.put("b");
        assertTrue(hash.contains("a"));
        assertTrue(hash.contains("b"));
        assertTrue(hash.contains("c"));
        assertTrue(hash.contains("d"));
        assertFalse(hash.contains("e"));
    }
}
