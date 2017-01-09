package sort;

public class InversionCounter {
    
    private static long numInversions;
    
    public static long countInversions(Comparable[] a) {
        Comparable[] newa = new Comparable[a.length];
        numInversions = 0;
        mergeSort(a, 0, a.length - 1, newa);
        //The array a is sorted
        return numInversions;
    }
    
    /**
     * Use MergeSort to implement inversion counting; while sorting, if an element 
     * is copied from the upper array while there are still unprocessed elements in the lower array,
     * then there are N inversions where N = the number of unprocessed elements in the lower array.
     * 
     * @param a The array to be sorted
     * @param i The index of the first element
     * @param j The index of the last element
     * @param tmp Copy of input array; mergesort requires a copy of the input, it does not sort in place.
     */
    private static void mergeSort(Comparable[] a, int i, int j, Comparable[] tmp) {
        if (i >= j) {
            return;
        }
        int m = i + (j - i)/2;  // determine the midpoint
        mergeSort(a, i, m, tmp); // sort the lower half
        mergeSort(a, m + 1, j, tmp);  // sort the upper half
        
        for (int k = i; k <= j; k++) {
            tmp[k] = a[k];
        }
        int li = i;           // for indexing into the sorted lower half
        int ui = m + 1;       // for indexing into the sorted upper half
        for (int ia = i; ia <= j; ) {  // for indexing into the array portion being sorted
            if (li == m+1) {  // the lower half has been copied  
                while (ui < j + 1) {  // => copy over the remaining upper half
                    a[ia++] = tmp[ui++];
                }
            } else {
                if (ui == j + 1) { // the upper half has been copied
                    while (li < m + 1) {    // => copy over the remaining lower half
                        a[ia++] = tmp[li++];
                    }
                } else {
                    if (tmp[li].compareTo(tmp[ui]) <= 0) {
                        a[ia++] = tmp[li++];    // take the next lower half element - its smaller (or equal)
                    } else {
                        a[ia++] = tmp[ui++];    // take the next upper half element - its smaller
                        numInversions += (m+1) - li; // increment inversion count by remaining # items
                                                     // in the lower array
                    }
                }
            }
        }
    }
}
