package sort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

public class QuickSortTest {

    @Test
    public void testMedians() {
        Integer a[] = {4, 1, 2, 3, 5, 9, 8, 7, 7};

        assert QuickSorter.medianOf3(a, 0, a.length - 1) == 4;

        Integer a2[] = {1, 1, 1};
        assert QuickSorter.medianOf3(a2, 0, a2.length - 1) == 1;

        Integer a3[] = {7, 11, 3};
        assert QuickSorter.medianOf3(a3, 0, a3.length - 1) == 0;

        Integer a4[] = {12, 1, 8};
        assert QuickSorter.medianOf3(a4, 0, a4.length - 1) == 2;

        Integer a5[] = {8, 2, 4, 5, 7, 1};
        assert QuickSorter.medianOf3(a5, 0, a5.length - 1) == 2;

        Integer a6[] = {4, 5, 6, 7};
        assert QuickSorter.medianOf3(a6, 0, a6.length - 1) == 1;

    }

    @Test
    public void testThreeWay() {
        Integer a[] = {1, 4, 2, 4, 2, 4, 1, 2, 4, 1, 2, 2, 2, 2, 4, 1, 4, 4, 4};
        QuickSorter qs3way = new QuickSorter(QuickSorter.PartitionStrategy.THREE_WAY, false, false);
        qs3way.sort(a);
        
        QuickSorter qsFirst = new QuickSorter(QuickSorter.PartitionStrategy.FIRST, false, false);
        Integer a2[] = {1, 4, 2, 4, 2, 4, 1, 2, 4, 1, 2, 2, 2, 2, 4, 1, 4, 4, 4};
        qsFirst.sort(a2);
        
        // Three way quick sort works well on arrays with many repeated elements
        assert qs3way.getLastSortCompares() < 2 * qsFirst.getLastSortCompares();
    }

    @Test
    public void testFirstElementAsPivot() {
        this.testStrategy(QuickSorter.PartitionStrategy.FIRST, 162085);
    }
    
    @Test
    public void testLastElementAsPivot() {
        this.testStrategy(QuickSorter.PartitionStrategy.LAST, 164123);
    }

    @Test
    public void testKthOrderStatistics() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("resources/qstest.txt"));
            Integer a[] = new Integer[10000];
            String line;

            for (int i = 0; (line = br.readLine()) != null; i++) {
                a[i] = Integer.parseInt(line);
            }

            QuickSorter qs = new QuickSorter(QuickSorter.PartitionStrategy.MEDIAN, false, false);
            Comparable theAnswer = qs.RSelect(a, 9900);
            assert theAnswer.equals(9901);
        } catch (IOException ex) {
            Logger.getLogger(QuickSortTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(QuickSortTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void testStrategy(QuickSorter.PartitionStrategy strategy, int expectedCompares) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("resources/qstest.txt"));
            Integer a[] = new Integer[10000];
            String line;

            for (int i = 0; (line = br.readLine()) != null; i++) {
                a[i] = Integer.parseInt(line);
            }

            QuickSorter qs = new QuickSorter(strategy, false, false);
            qs.sort(a);
            qs.validateSortOrder(a);
            assert qs.getLastSortCompares() == expectedCompares;
        } catch (IOException ex) {
            Logger.getLogger(QuickSortTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(QuickSortTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
