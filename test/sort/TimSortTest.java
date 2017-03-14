package sort;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

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
        testRun(aAscDes, aAscDes.length/2);
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
}
