import java.util.Arrays;

/** HW #7, Two-sum problem.
 * @author
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        A = Arrays.copyOf(A, A.length);
        Arrays.sort(A);
        for (int i = 0; i < B.length; i++) {
            int j = Arrays.binarySearch(A, m - B[i]);
            if (j >= 0 && j < A.length && A[j] + B[i] == m) {
                return true;
            }
        }
        return false;
    }

}
