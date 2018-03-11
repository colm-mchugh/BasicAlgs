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
        String[] files = {"Median.txt", "median_test.txt", "median_7_20.txt",
        "median_10_40.txt", "median_13_80.txt", "median_16_80.txt", "median_19_160.txt",
        "median_21_320.txt", "median_29_1280.txt", "median_34_2560.txt", "median_41_10000.txt",
        "median_42_10000.txt", "median_43_10000.txt" };
        int fileSz = 10000;
        int[] expected = {1213, 8814, 177, 695, 3463, 3384, 2667, 8114, 2554, 7655,
                        5760, 9479, 3793, };
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
                System.out.println("File=" + files[i] + " " + (medianSum % fileSz) + "(" + expected[i] + ")");
                assert medianSum % fileSz == expected[i];
            } catch (IOException | NumberFormatException | IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

}
