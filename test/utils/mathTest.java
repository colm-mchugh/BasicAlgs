package utils;

import java.util.List;
import java.util.Set;
import org.junit.Test;

public class mathTest {
    
    
    @Test
    public void testDiagonal() {
        System.out.println("diagonal");
        int[][] M0 = {{1, 2}, {3, 4}};
        int[][] M1 = {{1,2,3}, {4,5,6}, {7,8,9}};
        int[][] M = { {1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16} };
        List<List<Integer>> expResult = null;
        List<List<Integer>> result = math.diagonal(M0);
        System.out.println("[");
        for (List<Integer> r: result) {
            System.out.print("[ ");
            for (Integer i : r) {
                System.out.print(i + " ");
            }
            System.out.println("]");
        }
        System.out.println("]");
    }
    
    @Test
    public void steppingNumbers() {
        int N = 1234;
        boolean isStepping = true;
        for (int x = N, dPrev = 0, d = -1; x != 0 && isStepping; x = x/10) {
            dPrev = d;
            d = x % 10;
            if ((dPrev != -1) && (Math.abs(d - dPrev) != 1)){
                isStepping = false;
            }
        }
        assert isStepping;
    }
    
    @Test
    public void colorfulTest() {
        assert math.colorful(3245) == 1;
    }
    
    @Test
    public void testPrimes() {
        int[] xpctd = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
        Set<Integer> primes100 = math.primes(100);
        assert primes100.size() == xpctd.length;
        for (int prime : xpctd) {
            assert primes100.contains(prime);
        }
    }
    
    @Test
    public void testPrimeSums() {
        int[] sums = math.primesums(10003292);
        assert sums[0] == 349;
        assert sums[1] == 10002943;
        
        assert math.primesums(10000001) == null;
        
        sums = math.primesums(10000021);
        assert sums[0] == 2;
        assert sums[1] == 10000019;
        
    }
}
