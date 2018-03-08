package heap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

public class MedianMaintainerTest {

    /**
     * Test of Median method, of class MedianMaintainer.
     */
    @Test
    public void testMedian() {
        String[] files = {"Median.txt", "median_test.txt"};
        int fileSz = 10000;
        int[] expected = {1213, 8814};
        for (int i = 0; i < files.length; i++) {
            try {
                // The file contains a list of the integers from 1 to 10000 in 
                // unsorted order; it is treated as a stream of numbers.
                BufferedReader br = new BufferedReader(new FileReader("resources/" + files[i]));
                String line;
                MedianMaintainer medianMaintainer = new MedianMaintainer();
                int medianSum = 0;
                while ((line = br.readLine()) != null) {
                    medianMaintainer.Add(Integer.parseInt(line.trim()));
                    assert medianMaintainer.Invariant();
                    medianSum += medianMaintainer.Median();
                }

                assert medianSum % fileSz == expected[i];
            } catch (IOException | NumberFormatException | IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

}
