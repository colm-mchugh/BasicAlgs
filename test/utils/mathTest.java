package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class mathTest {

    @Test
    public void testDiagonal() {
        System.out.println("diagonal");
        int[][] M0 = {{1, 2}, {3, 4}};
        int[][] M1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] M = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        List<List<Integer>> result = math.diagonal(M0);
        System.out.println("[");
        for (List<Integer> r : result) {
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
        for (int x = N, dPrev = 0, d = -1; x != 0 && isStepping; x = x / 10) {
            dPrev = d;
            d = x % 10;
            if ((dPrev != -1) && (Math.abs(d - dPrev) != 1)) {
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

    @Test
    public void testMaxset() {
        List<Integer> a = new ArrayList<>();
        int[] nos = {1159126505, -1632621729, 1433925857, 84353895, 2001100545, 1548233367, -1585990364};
        for (int no : nos) {
            a.add(no);
        }
        List<Integer> m = math.maxset(a);
        assert m.size() == 4;

        assert m.get(0) == 1433925857;
    }

    @Test
    public void testRange() {
        int[] a1 = {3, 4, 5, 8, 8, 8, 9};
        int[] a2 = {8, 8, 8, 9, 11};
        int[] a3 = {3, 4, 5, 6, 8, 8};
        int[] a4 = {3, 4, 5, 6, 8, 9};
        int[] a5 = {3, 4, 5, 6, 9, 9};

        List<Integer> r = math.findRange(makeList(a1), 8);
        assert r.get(0) == 3 && r.get(1) == 5;
        r = math.findRange(makeList(a2), 8);
        assert r.get(0) == 0 && r.get(1) == 2;
        r = math.findRange(makeList(a3), 8);
        assert r.get(0) == 4 && r.get(1) == 5;
        r = math.findRange(makeList(a4), 8);
        assert r.get(0) == 4 && r.get(1) == 4;
        r = math.findRange(makeList(a5), 8);
        assert r.get(0) == -1 && r.get(1) == -1;
    }

    @Test
    public void testCoins() {
        List<Integer> coins = new ArrayList<>(8);
        int[] vals = {1, 100, 500, 10};
        for (int v : vals) {
            coins.add(v);
        }
        assert math.maxcoin(coins) == 501;

        assert math.revBits(0) == 0;
        assert math.revBits(3) == 3221225472l;
        assert math.revBits(3221225472l) == 3;

    }

    @Test
    public void testContainer() {
        int[] a1 = {1, 5, 4, 3};
        int[] a2 = {0, 0, 1, 1};
        int[] a3 = {2, 14, 18, 23, 25, 36, 40, 44, 44, 53, 54, 68, 71, 80, 94};
        List<Integer> test = makeList(a1);

        int area = math.maxContainer(test);
        assert area == 6;

        test = makeList(a2);
        int x = math.remDups(test);
        assert x == 2;

        test = makeList(a3);
        assert math.diffK(test, 1) == 1;

    }

    @Test
    public void testCombs() {
        List<List<Integer>> combs = math.combinations(5, 3);
        assert combs.size() == 10;
        for (List<Integer> comb : combs) {
            assert comb.size() == 3;
            for (int i = 0; i < comb.size() - 1; i++) {
                assert comb.get(i) < comb.get(i + 1);
            }
            System.out.println(comb);
        }
    }

    @Test
    public void testSubsets() {
        int[][] a1 = { {1, 2, 3, 4, 5}, {1, 2, 3}, {3}, {4,6,3,2,5,7,8,9,1}, };
        for (int[] ar  : a1) {
            List<Integer> s = makeList(ar);
            System.out.println("Next set: " + s);
            List<List<Integer>> subsets = math.subsets(s);
            int expectedNumSubsets = 1 << s.size();
            assert subsets.size() == expectedNumSubsets;
            assert subsets.get(0).isEmpty();
            for (List<Integer> subset : subsets) {
                System.out.println(subset);
            }
        }

    }

    @Test
    public void testSubsetsWithDuplicates() {
        int[][] a1 = {{2,2,3,4}, {3,3,5,6,6,6},{3, 3, 3, 3}, {1, 2, 2},  };
        for (int[] ar  : a1) {
            List<Integer> set1 = makeList(ar);
            System.out.println("Next set: " + set1);
            List<List<Integer>> subsets = math.subsets(set1);
            //assert subsets.size() == math.pow(set1.size(), 2) - 1;
            assert subsets.get(0).isEmpty();
            for (List<Integer> subset : subsets) {
                System.out.println(subset);
            }
        }

    }
    
    @Test
    public void testGrayCode() {
        int n = 3;
        List<Integer> codes = math.grayCode(3);
        int[] expected = {0,1,3,2,6,7,5,4};
        for (int i = 0; i < expected.length; i++) {
            assert expected[i] == codes.get(i);
        }
    }
    
    private List<Integer> makeList(int[] data) {
        List<Integer> rv = new ArrayList<>(data.length);
        for (int d : data) {
            rv.add(d);
        }
        return rv;
    }

}
