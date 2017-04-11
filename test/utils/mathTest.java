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
        List<List<Integer>> result = math.diagonal(M1);
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

        int[] r = math.findRange(makeList(a1), 8);
        assert r[0] == 3 && r[1] == 5;
        r = math.findRange(makeList(a2), 8);
        assert r[0] == 0 && r[1] == 2;
        r = math.findRange(makeList(a3), 8);
        assert r[0] == 4 && r[1] == 5;
        r = math.findRange(makeList(a4), 8);
        assert r[0] == 4 && r[1] == 4;
        r = math.findRange(makeList(a5), 8);
        assert r[0] == -1 && r[1] == -1;
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
        int[][] a1 = {{1, 2, 3, 4, 5}, {1, 2, 3}, {3}, {4, 6, 3, 2, 5, 7, 8, 9, 1},};
        for (int[] ar : a1) {
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
        int[][] a1 = {{2, 2, 3, 4}, {3, 3, 5, 6, 6, 6}, {3, 3, 3, 3}, {1, 2, 2},};
        for (int[] ar : a1) {
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
        int[] expected = {0, 1, 3, 2, 6, 7, 5, 4};
        for (int i = 0; i < expected.length; i++) {
            assert expected[i] == codes.get(i);
        }
    }

    @Test
    public void testPerms() {
        int[] data = {1, 2, 3, 4, 5};
        List<List<Integer>> perms = math.permutations(makeList(data));
        System.out.println("#perms=" + perms.size());
        assert perms.size() == 120;
        for (List<Integer> perm : perms) {
            System.out.println(perm);
        }
    }

    @Test
    public void testCombos() {
        int[] data = {7, 8, 10, 6, 11, 1, 16, 8};
        int N = 28;
        List<List<Integer>> combos = math.combinations(makeList(data), N);
        //assert combos.size() == 2;
        for (List<Integer> comb : combos) {
            System.out.println(comb);
        }
    }

    private List<Integer> makeList(int[] data) {
        List<Integer> rv = new ArrayList<>(data.length);
        for (int d : data) {
            rv.add(d);
        }
        return rv;
    }

    @Test
    public void testFactorial() {
        assert math.trailingZerosInFactorialOf(5) == 1;
        assert math.trailingZerosInFactorialOf(4) == 0;
        assert math.trailingZerosInFactorialOf(101) == 24;
        assert math.trailingZerosInFactorialOf(4617) == 1151;
    }
    
    @Test
    public void testExcel() {
        assert math.excel2(3).equals("C");
        assert math.excel2(26).equals("Z");
        assert math.excel2(27).equals("AA");
        assert math.excel2(28).equals("AB");
    }
    
    @Test
    public void testRevint() {
        assert math.revint(1146467285) == 0;
        assert math.revint(1170064042) == 0;
        assert math.revint(123) == 321;
        assert math.revint(-123) == -321;
    }
    
    @Test
    public void testPlus1() {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(9);
        List<Integer> aPlus1 = math.plus1(a);   
    }
    
    @Test
    public void testRepeatedNumber() {
        int[] a = {3,1,2,5,3};
        int dup = -1;
        for (int i = 0; i < a.length; i++) {
            int j = Math.abs(a[i]) - 1;
            if (a[j] > 0) {
                a[j] = -a[j];
            } else {
                dup = Math.abs(a[j]);
                break;
            }
            
        }
        System.out.println(math.pascalK(3));
        
        math.pascalIt(3);
    }
    
    @Test
    public void testPositive() {
        int[] a0 = {1,};
        int[] a1 = {1,2,0};
        int[] a2 = {3,4,-1,1};
        int[] a3 = {-8,-7,-6};
        int[] a4 = {2, 3, 7, 6, 8, -1, -10, 15};
        int[] a5 = { 2, 3, -7, 6, 8, 1, -10, 15 };
        int[] a6 = {1, 1, 0, -1, -2};
        
        assert math.positive(makeList(a1)) == 3;
        assert math.positive(makeList(a0)) == 2;
        assert math.positive(makeList(a2)) == 2;
        assert math.positive(makeList(a3)) == 1;
        assert math.positive(makeList(a4)) == 1;
        assert math.positive(makeList(a5)) == 4;
        assert math.positive(makeList(a6)) == 2;
    }
}
