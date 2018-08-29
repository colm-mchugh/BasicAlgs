package sort;

public class InsertionSort {
    
     public static void Sort(Comparable[] a, int from, int to) {
         if (from < 0 || to >= a.length) {
             throw new IllegalArgumentException("Index out of bounds: " + from + ", " + to);
         }
         for (int i = from; i <= to; i++) {
             Comparable tmp = a[i];
             int j = i - 1;
             for (;j >= 0 && a[j].compareTo(tmp) > 0; j-- ) {
                 a[j + 1] = a[j];
             }
             a[j + 1] = tmp;
         }
     }
     
     public static int InstrumentedSort(Comparable[] a, int from, int to) {
         if (from < 0 || to >= a.length) {
             throw new IllegalArgumentException("Index out of bounds: " + from + ", " + to);
         }
         int num_swaps = 0;
         for (int i = from; i <= to; i++) {
             Comparable tmp = a[i];
             int j = i - 1;
             for (;j >= 0 && a[j].compareTo(tmp) > 0; j-- ) {
                 a[j + 1] = a[j];
                 num_swaps++;
             }
             a[j + 1] = tmp;
         }
         return num_swaps;
     }
}
