package sort;


import utils.RandGen;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuickSorter {

    // Configuration
    private final boolean randomize;
    private final boolean logging;

    public final static boolean RANDOM = true;
    public final static boolean LOGGING = true;

    // Statistics
    private int lastSortSize;
    private int lastSortCompares;
    private int lastSortComparesCheck;
    private int lastSortSwaps;

    public enum PartitionStrategy {
        THREE_WAY,
        FIRST,
        LAST,
        MEDIAN
    }
    
    private final PartitionStrategy partitionStrategy;
    
    /**
     * Configure the quick sort:
     * @param strategy The strategy for choosing the pivot element 
     * @param randomize The array to be sorted will be randomized before sorting 
     * @param logging Log intermediate actions
     */
    public QuickSorter(PartitionStrategy strategy, boolean randomize, boolean logging) {
        this.partitionStrategy = strategy;
        this.randomize = randomize;
        this.logging = logging;
    }
    /**
     * Default quick sort: randomize the input, choose median as the pivot
     */ 
    public QuickSorter() {
        this.randomize = true;
        this.logging = false;
        this.partitionStrategy = PartitionStrategy.MEDIAN;
    }
    
    /**
     * Randomize the order of elements in the given array.
     * @param a the array to be unsorted
     */
    public void unsort(Comparable a[]) {
        for (int i = 0; i < a.length; i++) {
            int j = RandGen.uniform(i + 1);
            this.exch(a, i, j);
        }
    }

    /**
     * Sort the given array
     * 
     * @param a the array to be sorted
     */ 
    public void sort(Comparable a[]) {
        if (a.length <= 1) {
            return;
        }

        this.lastSortSize = a.length;
        this.lastSortCompares = 0;
        this.lastSortComparesCheck = 0;
        this.lastSortSwaps = 0;

        if (this.randomize) {
            this.unsort(a);
        }
        if (this.partitionStrategy == PartitionStrategy.THREE_WAY) {
            this.threeWaySort(a, 0, a.length - 1);
        } else {
            this.sort(a, 0, a.length - 1);
        }
    }

    public Comparable RSelect(Comparable[] a, int k) {
        if (k > a.length) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        this.lastSortCompares = 0;
        this.lastSortComparesCheck = 0;
        return doRSelect(a, k, 0, a.length - 1);
    }

    private Comparable doRSelect(Comparable[] a, int k, int lo, int hi) {
        if (hi - lo <= 1) {
            return a[lo];
        }
        int pivotIndex = this.simplePartition(a, lo, hi);
        if (pivotIndex == k) {
            return a[pivotIndex];
        }
        if (pivotIndex > k) {
            return doRSelect(a, k, lo, pivotIndex - 1);
        }
        // pivotIndex < k
        return doRSelect(a, k, pivotIndex + 1, hi);
    }

    public Comparable select(Comparable[] a, int k) {
        if (k > a.length) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (this.randomize) {
            this.unsort(a);
        }
        int lo = 0;
        int hi = a.length - 1;
        while (hi > lo) {
            int j = this.simplePartition(a, lo, hi);
            if (j > k) {
                hi = j - 1;
            } else if (j < k) {
                lo = j + 1;
            } else {
                // j == k
                break;
            }
        }
        return a[k];
    }

    public int getLastSortSize() {
        return lastSortSize;
    }

    public int getLastSortCompares() {
        return lastSortCompares;
    }

    public int getLastSortSwaps() {
        return lastSortSwaps;
    }

    private void sort(Comparable[] a, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int k = this.simplePartition(a, lo, hi);
        if (this.logging) {
            this.printIntermediateArray(a);
        }
        this.sort(a, lo, k - 1);
        this.sort(a, k + 1, hi);
    }

    private int simplePartition(Comparable[] a, int lo, int hi) {
        int numCompares = hi - lo;
        int cmpCheck = 0;
        int pivotIndex = lo;
        switch (this.partitionStrategy) {
            case FIRST: pivotIndex = lo; 
                break;
            case LAST: pivotIndex = hi;
                break;
            case MEDIAN: pivotIndex = QuickSorter.medianOf3(a, lo, hi);
                break;
        }
 
        Comparable pivot = a[pivotIndex];
        this.exch(a, lo, pivotIndex);

        int i = lo + 1;
        for (int j = lo + 1; j <= hi; j++) {
            if (a[j].compareTo(pivot) < 0) {
                this.exch(a, i, j);
                i++;
            }
            cmpCheck++;
        }
        this.lastSortCompares += numCompares;
        this.lastSortComparesCheck += cmpCheck;
        assert cmpCheck == numCompares;
        this.exch(a, lo, i - 1);
        return i - 1;
    }

    private void threeWaySort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int i = lo;
        int lt = lo;
        int gt = hi;
        Comparable k = a[lo];
        while (i <= gt) {
            int cmp = a[i].compareTo(k);
            this.lastSortCompares++;
            if (cmp < 0) {
                this.exch(a, i++, lt++);
                this.lastSortSwaps++;
            } else if (cmp > 0) {
                this.exch(a, i, gt--);
                this.lastSortSwaps++;
            } else {
                i++;
            }
        }
        if (this.logging) {
            this.printIntermediateArray(a);
        }
        threeWaySort(a, lo, lt - 1);
        threeWaySort(a, gt + 1, hi);
    }

    private void exch(Comparable a[], int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static Comparable kthOrderStatistic(Comparable[] input, int k) {
        QuickSorter qs = new QuickSorter(PartitionStrategy.MEDIAN, !RANDOM, !LOGGING);
        
        System.out.println("kthOrderStatistic() size=" + input.length);
        Comparable theAnswer = qs.RSelect(input, k);
        System.out.println("kthOrderStatistic() RSelect #compares=" + qs.getLastSortCompares());
        Comparable check = qs.select(input, k);
        if (theAnswer.compareTo(check) != 0) {
            System.out.println("Check fail! " + theAnswer + " != " + check);
            throw new AssertionError();
        };

        qs.sort(input);

        if (input[k].compareTo(theAnswer) != 0) {
            System.out.println("Sorted Check fail! " + theAnswer + " != " + input[k]);
            throw new AssertionError();
        };
        return theAnswer;
    }

    private static void qs(Comparable[] input, PartitionStrategy strategy) {
        QuickSorter qs = new QuickSorter(strategy, !RANDOM, !LOGGING);
        qs.sort(input);
        System.out.println("Number of Compares=" + qs.getLastSortCompares());
        qs.validateSortOrder(input);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Integer[] ints = {58, 58, 33, 96, 45, 62, 58, 65, 26, 58};
        Integer[] ints2 = {24, 97, 28, 86, 48, 87, 44, 38, 69, 13, 89, 23};

        String[] strings = {"Stately", "plump", "Buck", "Mulligan", "came", "from",
            "the", "stairhead", "bearing", "a", "bowl", "of", "lather", "on",
            "which", "a", "razor", "and", "a", "mirror", "lay", "crossed"};

        Comparable[] input;

        BufferedReader br = new BufferedReader(new FileReader("resources/qstest.txt"));
        String line;
        List<Integer> foo = new ArrayList<Integer>();
        while ((line = br.readLine()) != null) {
            foo.add(Integer.parseInt(line));
        }
        Integer[] bar = new Integer[foo.size()];
        input = foo.toArray(bar);
        //qsMedian(input);

        int k = input.length / 2 - 1;
        Comparable p = kthOrderStatistic(input, k);

        System.out.println("The " + k + "th order statistic is " + p);

    }

    private void printIntermediateArray(Comparable[] a) {
        for (Comparable c : a) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    public void validateSortOrder(Comparable[] a) {
        for (int i = 0; i < a.length - 2; i++) {
            assert a[i].compareTo(a[i + 1]) <= 0;
        }
    }

    static int medianOf3(Comparable a[], int p, int r) {
        int m = (p + r) / 2;
        if (a[p].compareTo(a[m]) < 0) {
            if (a[p].compareTo(a[r]) >= 0) {
                return p;
            } else if (a[m].compareTo(a[r]) < 0) {
                return m;
            }
        } else if (a[p].compareTo(a[r]) < 0) {
            return p;
        } else if (a[m].compareTo(a[r]) >= 0) {
            return m;
        }
        return r;
    }

}
