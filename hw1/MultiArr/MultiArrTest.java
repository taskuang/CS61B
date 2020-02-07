import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        //TODO: Your code here!
        int[][] testArr = new int[3][2];
        testArr[2][1] = 2;
        assertEquals(2, MultiArr.maxValue(testArr));
    }

    @Test
    public void testAllRowSums() {
        //TODO: Your code here!
        int[][] testArr = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[] expected = new int[]{6, 15, 24};
        assertArrayEquals(expected, MultiArr.allRowSums(testArr));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
