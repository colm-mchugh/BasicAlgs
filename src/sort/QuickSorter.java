package sort;


import utils.RandGen;

/**
 * Configurable quick sorter. 
 * 
 */
public class QuickSorter {

    // Configuration
    private final boolean randomize;
    private final boolean logging;

    public final static boolean RANDOM = true;
    public final static boolean LOGGING = true;

    // Statistics
    private int lastSortSize;   // the size of the most recently sorted array
    private int lastSortCompares;   // the number of compares made by the most recent sort
    private int lastSortSwaps;  // the number of swaps made by the most recent sort

    /**
     * Defines how the pivot element is chosen during a sort.
     * FIRST means choose the first element.
     * LAST means choose the last element.
     * MEDIAN means choose the median of the first, last and middle elements.
     * THREE_WAY means apply three way partitioning (FIRST, LAST, MEDIAN all use 
     * two way partitioning)
     */
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
     * Sort the given array. 
     * 
     * Do some preprocessing - initialize stats, randomize the input, if so
     * configured - then invoke private methods to do the actual sorting work.
     * 
     * @param a the array to be sorted
     */ 
    public void sort(Comparable a[]) {
        if (a.length <= 1) {
            return;
        }

        this.lastSortSize = a.length;
        this.lastSortCompares = 0;
        this.lastSortSwaps = 0;

        if (this.randomize) {
            UnSort.Of(a);
        }
        if (this.partitionStrategy == PartitionStrategy.THREE_WAY) {
            this.threeWaySort(a, 0, a.length - 1);
        } else {
            this.sort(a, 0, a.length - 1);
        }
    }

    
    /**
     * Return the Kth Order Statistic of the given array. The kth order statistic
     * of an array is the kth smallest item in the array.
     * 
     * This implementation uses pivot partitioning to derive the kth order statistic:
     * starting with all elements of the array, determine a pivot element, and 
     * then, depending on which side of the pivot that k is on, choose that side to
     * pivot on again, stopping when the pivot is equal to k.
     * 
     * It is similar to quicksort in that partitioning is used to determine an 
     * index to pivot on, but only one side of the pivot is processed each time,
     * and it stops when the kth item is found, resulting in a partially sorted array.
     * This is O(N) best case, particularly if the partition function returns a 
     * pivot that is close to the median of that portion of the array. O(N*N) worst
     * case, when the array is already sorted and the smallest element is the pivot
     * each time (similar to the degenerate case of quicksort).
     * 
     * @param a an array of unordered elements.
     * @param k the item of interest
     * @return the kth smallest item in a
     */
    public Comparable KSelect(Comparable[] a, int k) {
        if (k > a.length) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (this.randomize) {
            a = UnSort.Of(a);
        }
        int lo = 0;
        int hi = a.length - 1;
        while (hi > lo) {
            int pivot = this.partition(a, lo, hi);
            if (pivot > k) {
                hi = pivot - 1;
            } else if (pivot < k) {
                lo = pivot + 1;
            } else {
                // pivot == k
                break;
            }
        }
        return a[k];
    }

    /**
     * Return the kth order statistic of the given array.
     * 
     * Similar algorithm as KSelect, except its implemented using recursion.
     * 
     * @param a
     * @param k
     * @return 
     */
    public Comparable KSelectRecursive(Comparable[] a, int k) {
        if (k > a.length) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        this.lastSortCompares = 0;
        return doRSelect(a, k, 0, a.length - 1);
    }

    /** 
     * Return the size of the last array sorted, or 0 if no sorts have been made.
     * 
     * @return 
     */
    public int getLastSortSize() {
        return lastSortSize;
    }

    /**
     * Return the number of compares done during the most recent sort, or 0 if no
     * sorts have been made.
     * 
     * @return 
     */
    public int getLastSortCompares() {
        return lastSortCompares;
    }

    /**
     * Return the number of swaps done during the most recent sort, or 0 if no 
     * sorts have been made.
     * 
     * @return 
     */
    public int getLastSortSwaps() {
        return lastSortSwaps;
    }

    /**
     * Return the kth smallest item of the array a between indices lo and hi
     * (inclusive). Similar algorithm to KSelect(), (determine a pivot using
     * partition(), repeat until the pivot index equals k) except it uses
     * recursion instead of iteration. 
     * 
     * @param a
     * @param k
     * @param lo
     * @param hi
     * @return 
     */
    private Comparable doRSelect(Comparable[] a, int k, int lo, int hi) {
        if (hi - lo <= 1) {
            return a[lo];
        }
        int pivotIndex = this.partition(a, lo, hi);
        if (pivotIndex == k) {
            return a[pivotIndex];
        }
        if (pivotIndex > k) {
            return doRSelect(a, k, lo, pivotIndex - 1);
        }
        // pivotIndex < k
        return doRSelect(a, k, pivotIndex + 1, hi);
    }

    /**
     * Sort the elements of the given array starting at index lo up to and
     * including index hi. 
     * 
     * Straightforward application of quick sort:
     * - Use partition function to move one of the elements to its final sorted 
     *   position; this is the pivot element
     * - Recursively sort the elements less than the pivot element
     * - Recursively sort the elements greater than the pivot element
     * 
     * @param a
     * @param lo
     * @param hi 
     */
    private void sort(Comparable[] a, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        // Use simplePartition to put element a[k] in its final position
        int k = this.partition(a, lo, hi);
        if (this.logging) {
            this.printIntermediateArray(a);
        }
        // simplePartition post conditions:
        // a[k] is in its final, sorted position
        // elements a[lo] to a[k-1] are all less than a[k]
        // elements a[k+1] to a[hi] are all greater than a[k]
        
        // sort the array portions on eiher side of k:
        this.sort(a, lo, k - 1);
        this.sort(a, k + 1, hi);
    }

    /**
     * Choose a pivot element and partition the array around it so that:
     * The elements from lo to pivot index - 1 are less than the pivot
     * The elements from pivot index + 1 to hi are greater than the pivot.
     * 
     * After some preliminaries (update stats, determine the pivot index):
     * - put the pivot element in a[lo]
     * - examine each element from lo+1 to hi, maintaining the invariant that 
     *   elements smaller than the pivot element have index range: lo+1..i-1
     * - swap the pivot element with the last element found to be smaller than it
     * 
     * @param a
     * @param lo
     * @param hi
     * @return 
     */
    private int partition(Comparable[] a, int lo, int hi) {
        this.lastSortCompares += hi - lo;
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
        }
        this.exch(a, lo, i - 1);
        return i - 1;
    }

    /**
     * Quicksort the given array between index lo and hi (inclusive).
     * 
     * This implementation uses three way partitioning: one for elements smaller
     * than, equal to and larger than the pivot element. This variation has good
     * characteristics on data with a large number of equal items. 
     * 
     * @param a
     * @param lo
     * @param hi 
     */
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

    private void printIntermediateArray(Comparable[] a) {
        for (Comparable c : a) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    /**
     * Return the median of the three values: a[p], a[(p+r)/2], a[r]
     * 
     * @param a
     * @param p
     * @param r
     * @return 
     */
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
