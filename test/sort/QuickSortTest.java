package sort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

            QuickSorter qs = new QuickSorter(QuickSorter.PartitionStrategy.MEDIAN, !QuickSorter.RANDOM, !QuickSorter.LOGGING);
            Comparable theAnswer = qs.KSelectRecursive(a, 9900);
            assert theAnswer.equals(9901);
            
            int k = a.length / 2 - 1;
            theAnswer = qs.KSelectRecursive(a, k);
            Comparable check = qs.KSelect(a, k);
            assert theAnswer.equals(check);
            
            qs.sort(a);
            assert a[k].equals(theAnswer);
            
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
    
    @Test
    public void testSortUlysses() {
        String[] Ulysses = {"Stately", "plump", "Buck", "Mulligan", "came", "from",
            "the", "stairhead", "bearing", "a", "bowl", "of", "lather", "on",
            "which", "a", "razor", "and", "a", "mirror", "lay", "crossed"};
        QuickSorter qs = new QuickSorter();
        qs.sort(Ulysses);
        this.validateSortOrder(Ulysses);
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
            this.validateSortOrder(a);
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
    
    private void validateSortOrder(Comparable[] a) {
        for (int i = 0; i < a.length - 2; i++) {
            assert a[i].compareTo(a[i + 1]) <= 0;
        }
    }

    @Test
    public void bigTest() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("resources/input_dgrcode_20_1000000.txt"));
            Integer a[] = new Integer[1000000];
            String line;

            for (int i = 0; (line = br.readLine()) != null; i++) {
                a[i] = Integer.parseInt(line);
            }

            QuickSorter qs = new QuickSorter(QuickSorter.PartitionStrategy.LAST, !QuickSorter.RANDOM, !QuickSorter.LOGGING);
            qs.sort(a);
            validateSortOrder(a);
            
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
    
    /* testUnsortFrequency is for observing the randomness of unsort.
    */
    @Test
    public void testUnsortFrequency() {
        String[] seq = { "a", "b", "c", "d",  };
        int iterations = 10000000;
        double tolerance = 0.1;
        Map<String, Integer> freqs = unsortFrequency(seq, iterations);
        printFreqs(freqs, iterations, tolerance);
    }
            

    /**
     * Run unsort on the given list of strings the given number of times, and
     * return a map of how many times each distinct sequence was produced by 
     * an unsort.
     * This can be used to assess the randomness of the unsort function. 
     * Ideally, frequencies should be uniform.
     * 
     * @param list
     * @param times
     * @return 
     */
    Map<String, Integer> unsortFrequency(String[] list, int times) {
        Map<String, Integer> freqs = new HashMap<>();
        QuickSorter qs = new QuickSorter();
        for (int i = 0; i < times; i++) {
            UnSort.Of(list);
            StringBuilder sb = new StringBuilder();
            for (String s : list) {
               sb.append(s);
            }
            String res = sb.toString();
            if (freqs.containsKey(res)) {
                freqs.put(res, freqs.get(res) + 1);
            } else {
                freqs.put(res, 1);
            }
        }
        return freqs;
    }
    
    void printFreqs(Map<String, Integer> f, int sample_size, double tolerance) {
        int expected = sample_size / f.keySet().size();
        int lower = (int) (expected - expected * tolerance / 100);
        int upper = (int) (expected + expected * tolerance / 100);
        System.out.println("expected #occurences:" + expected + " [" + lower + ", " + upper + "]");
        
        for (String k : f.keySet()) {
            System.out.print(k + ": " + f.get(k));
            if (f.get(k) < lower || f.get(k) > upper) {
                System.out.println(" ** outside tolerance (" + Math.abs(expected - f.get(k))*100.0/expected + ")");
            } else {
                System.out.println();
            }
        }
    }
}
