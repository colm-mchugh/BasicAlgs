package sort;

import utils.RandGen;

public class UnSort {
    
    /**
     * Randomize the order of elements in the given array.
     * 
     * Swap each element of the array with a randomly chosen element.
     * Runs in O(n). 
     * 
     * @param a the array to be unsorted
     */
    static public Comparable[] Of(Comparable a[]) {
        for (int i = 0; i < a.length; i++) {
            int j = RandGen.uniform(i + 1);
            exch(a, i, j);
        }
        return a;
    }
    
    static private void exch(Comparable a[], int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

}
