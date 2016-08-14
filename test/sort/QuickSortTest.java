package sort;

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
   
}
