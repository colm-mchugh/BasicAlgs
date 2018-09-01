package sort;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.Test;
import utils.math;
import static utils.math.inOrder;

public class TimSortTest {

    Integer[] a1element = {234};
    Integer[] aEmpty = {};
    Integer[] aAsc = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    Integer[] aDsc = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    Integer[] rnd1 = {23, 74, 30, 12, 45, 98, 53, 75, 23, 95, 17, 23, 89, 32, 54, 67, 48, 75};
    Integer[] aAsc1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1};
    Integer[] aAscDes = {1, 20, 2, 19, 3, 18, 4, 17, 5, 16};

    @Test
    public void insertionSort() {
        testInsertionSort(a1element);
        testInsertionSort(aEmpty);
        testInsertionSort(aAsc);
        testInsertionSort(aDsc);
        testInsertionSort(rnd1);
    }

    @Test
    public void reverse() {
        testReversed(aEmpty);
        testReversed(a1element);
        testReversed(aAsc);
        testReversed(aDsc);
        testReversed(rnd1);
    }

    @Test
    public void identifyRuns() {
        testRun(a1element, 1);
        testRun(aEmpty, 0);
        testRun(aAsc, 1);
        testRun(aDsc, 1);
        testRun(aAsc1, 2);
        testRun(rnd1, 8);
        testRun(aAscDes, aAscDes.length / 2);
    }

    @Test
    public void testSorted() {
        float[] cardinalities = {0.99f, 0.75f, 0.5f, 0.33f, 0.1f, 0.01f};

        for (float card : cardinalities) {
            Integer[] intArray = math.genUniformArray(10000, card);
            TimSort.Sort(intArray);
            assert inOrder(intArray);
        }
    }

    @Test
    public void testInsertionSort() {
        float[] cardinalities = { 0.99f, 0.75f, 0.5f, 0.33f, 0.1f, 0.01f, 0.001f, 0.0001f};

        for (float card : cardinalities) {
            Integer[] intArray = math.genUniformArray(10000, card);
            int num_swaps = InsertionSort.InstrumentedSort(intArray, 0, intArray.length - 1);
            assert inOrder(intArray);
            System.out.println("Number of swaps =" + num_swaps + ", cardinality=" + card + ", #distinct values =" + countDistinct(intArray));
        }
        
        // Get number of swaps made on an array with all elements distinct
        Integer[] intArray = new Integer[10000];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = i;
        }
        new QuickSorter().unsort(intArray);
        
        int num_swaps = InsertionSort.InstrumentedSort(intArray, 0, intArray.length - 1);
        assert inOrder(intArray);
        System.out.println("Number of swaps =" + num_swaps + ", cardinality=1" + ", #distinct values =" + intArray.length);
    
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = intArray.length - i;
        }
        num_swaps = InsertionSort.InstrumentedSort(intArray, 0, intArray.length - 1);
        assert inOrder(intArray);
        System.out.println("Number of swaps =" + num_swaps + ", cardinality=1" + ", #distinct values =" + intArray.length);
    
    }

    private void testRun(Comparable[] a, int expectedSz) {
        List<TimSort.Run> runs = TimSort.identifyRuns(a);
        assert runs.size() == expectedSz;
        if (expectedSz == 1) {
            TimSort.Run run = runs.get(0);
            assert run.len() == a.length;
        }
    }

    private void testInsertionSort(Comparable[] a) {
        InsertionSort.Sort(a, 0, a.length - 1);
        assert isSorted(a);
        System.out.println(Arrays.toString(a));
    }

    private void testReversed(Comparable[] a) {
        Comparable[] aCopy = Arrays.copyOf(a, a.length);
        TimSort.Reverse(aCopy, 0, a.length - 1);
        assert isReversed(aCopy, a);
    }

    private boolean isSorted(Comparable[] a) {
        for (int i = a.length - 1; i > 0; i--) {
            if (a[i].compareTo(a[i - 1]) < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isReversed(Comparable[] a, Comparable[] aCopy) {
        for (int i = 0, j = a.length - 1; i < j; i++, j--) {
            if (a[i].compareTo(aCopy[j]) != 0) {
                return false;
            }
        }
        return true;
    }

    private int countDistinct(Integer[] array) {
        if (!inOrder(array)) {
            System.err.println("countDistinct: array must be sorted");
        }
        if (array.length == 0) {
            return 0;
        }
        int count = 1;
        for (int i = 0; i < array.length - 1; i++) {
            if (!Objects.equals(array[i], array[i + 1])) {
                count++;
            }
        }
        return count;
    }
}
