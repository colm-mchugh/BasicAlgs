package sort;

public class InversionCounter {

    /**
     * Count the number of inversions in the array a.
     * 
     * An inversion in an array a of length N exists if 
     * i > j and a[i] < a[j] for any 0 <= i < N and 0 <= j < N
     * This function counts all such inversions in the array.
     * Additionally, the array is sorted.
     * @param a
     * @return 
     */
    public static long countInversions(Comparable[] a) {
        Comparable[] newa = new Comparable[a.length];
        return mergeSort(a, 0, a.length - 1, newa);
    }

    /**
     * Use MergeSort to implement inversion counting; while sorting, if an
     * element is copied from the upper array while there are still unprocessed
     * elements in the lower array, then there are n inversions where n is the
     * number of unprocessed elements in the lower array.
     *
     * @param a The array to be sorted
     * @param i The index of the first element
     * @param j The index of the last element
     * @param tmp Copy of input array; mergesort requires a copy of the input,
     * it does not sort in place.
     */
    private static long mergeSort(Comparable[] a, int i, int j, Comparable[] tmp) {
        if (i >= j) {
            return 0;
        }
        long numInversions = 0;
        int m = i + (j - i) / 2;  // determine the midpoint
        numInversions += mergeSort(a, i, m, tmp); // sort the lower half
        numInversions += mergeSort(a, m + 1, j, tmp);  // sort the upper half

        for (int k = i; k <= j; k++) {
            tmp[k] = a[k];
        }
        int li = i;           // for indexing into the sorted lower half
        int ui = m + 1;       // for indexing into the sorted upper half
        int ia = i;
        while ((li <= m) && (ui <= j)) {
            if (tmp[li].compareTo(tmp[ui]) <= 0) {
                a[ia++] = tmp[li++];    // take the next lower half element - its smaller (or equal)
            } else {
                a[ia++] = tmp[ui++];    // take the next upper half element - its smaller
                numInversions += (m + 1) - li; // increment inversion count by remaining # items
                // in the lower array
            }
        }
        while (ui < j + 1) {  // upper half not completely processed ?
            a[ia++] = tmp[ui++]; // => copy over the remaining upper half
        }
        while (li < m + 1) {    // upper half not completely processed ?
            a[ia++] = tmp[li++]; // => copy over the remaining lower half
        }
        return numInversions;
    }
}
