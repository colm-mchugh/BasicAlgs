package sort;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import utils.math;
import static utils.math.isSorted;

public class InversionCounterTest {

    private final static long EXPECTED = 2407905288l;
    
    @Test
    public void testCountInversions1() {
        Integer[] a = {67, 23, 45, 89, 17, 1, 99};
        InversionCounter.countInversions(a);
        assert isSorted(a);
    }

    @Test
    public void testCountInversions2() {
        Integer[] a = {6, 7, 8, 2, 3, 4, 1};
        InversionCounter.countInversions(a);
        assert isSorted(a);
    }

    @Test
    public void testCountInversions3() {
        Integer[] a = {3, 4, 1};
        InversionCounter.countInversions(a);
        assert isSorted(a);
    }

    @Test
    public void testCountInversions4() {
        Integer[] a = {4, 2, 3, 5, 1};
        long c = InversionCounter.countInversions(a);
        assert isSorted(a);
        assert c == 6;
    }

    @Test
    public void testCountInversionsBig() {
        BufferedReader br = null;
        try {
            Integer[] a = new Integer[100000];
            br = new BufferedReader(new FileReader("resources/inversion_count_test.txt"));
            String line;
            for (int i = 0; (line = br.readLine()) != null; i++) {
                a[i] = (Integer.parseInt(line));
            }
            long c = InversionCounter.countInversions(a);
            System.out.println("Number of inversions: " + c);
            assert math.inOrder(a);
            assert c == EXPECTED; 
        } catch (FileNotFoundException ex) {
            Assert.fail("FileNotFoundException: " + ex.getLocalizedMessage());
        } catch (IOException ex) {
            Assert.fail("IOException: " + ex.getLocalizedMessage());
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Assert.fail("IOException: " + ex.getLocalizedMessage());
            }
        }
    }

}
