import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {

    private BSTStringSet bst = new BSTStringSet();

    @Test
    public void testBSTString() {
        bst.put("d");
        bst.put("c");
        bst.put("a");
        bst.put("b");
        assertTrue(bst.contains("a"));
        assertTrue(bst.contains("b"));
        assertTrue(bst.contains("c"));
        assertTrue(bst.contains("d"));
        assertFalse(bst.contains("e"));
    }

    @Test
    public void testIterator() {
        bst.put("a");
        bst.put("b");
        bst.put("c");
        bst.put("d");
        Iterator<String> iter = bst.iterator("a","d");
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        assertEquals("c", iter.next());
        assertFalse(iter.hasNext());
    }
}