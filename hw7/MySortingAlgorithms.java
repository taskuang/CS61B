import java.util.Arrays;
import static java.lang.System.arraycopy;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k > 1 || array.length > 1) {
                for (int i = 0; i < Math.min(k, array.length); i++) {
                    for (int j = i; j > 0 && array[j] < array[j - 1]; j--) {
                        swap(array, j, j - 1);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array.length > 1 || k > 1) {
                k = Math.min(array.length, k);
                for (int i = 0; i < k; i++) {
                    int min = i;
                    for (int j = i + 1; j < k; j++) {
                        if (array[j] < array[min]) {
                            min = j;
                        }
                    }
                    swap(array, i, min);
                }
            }
        }
        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k > 1 || array.length > 1) {
                sort(array, 0, k);
            }
        }


        private void sort(int[] array, int start, int end) {
            if (start + 1 != end) {
                int mid = (start + end) / 2;
                sort(array, start, mid);
                sort(array, mid, end);
                merge(array, start, mid, end);
            }
        }

        private void merge(int[] array, int start, int mid, int end) {
            for (int i = mid; i < end; i ++) {
                int temp = array[i];
                int j;
                for (j = i - 1; j >= start; j--) {
                    if (array[j] > temp) {
                        array[j+1]=array[j];
                    } else {
                        break;
                    }
                }
                array[j+1]=temp;
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            if (a.length > 1 || k > 1) {
                int max = a[0];
                int min = a[0];
                for (int i = 1; i < Math.min(a.length, k); i++) {
                    if (a[i] > max)
                        max = a[i];
                    else if (a[i] < min)
                        min = a[i];
                }
                int x = 1;
                while ((max - min) / x >= 1) {
                    sort(a, k, x, min);
                    x *= k;
                }
            }
        }

        public void sort(int[] a, int radix, int x, int min) {
            int[] buckets = new int[radix];
            for (int i = 0; i < radix; i++) {
                buckets[((a[i] - min) / x) % radix] += 1;
            }
            for (int i = 1; i < radix; i++) {
                buckets[i] += buckets[i - 1];
            }
            int[] result = new int[a.length];
            for (int i = radix - 1; i >= 0; i--) {
                result[--buckets[((a[i] - min) / x) % radix]] = a[i];
            }
            for (int i = 0; i < radix; i++) {
                a[i] = result[i];
            }
        }
        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            //FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
