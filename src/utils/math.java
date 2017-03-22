package utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class math {

    public static List<List<Integer>> diagonal(int[][] M) {
        assert M.length == M[0].length; // Must be an N x N matrix
        List<List<Integer>> diagonal = new ArrayList<>();
        int N = M.length;
        for (int k = 0; k < N; k++) {
            List<Integer> next = new ArrayList<>(k + 1);
            for (int i = 0, j = k; j >= 0; i++, j--) {
                next.add(M[i][j]);
            }
            diagonal.add(next);
        }
        for (int k = 1; k < N; k++) {
            List<Integer> next = new ArrayList<>(N - k);
            for (int i = k, j = N - 1; i < N; i++, j--) {
                next.add(M[i][j]);
            }
            diagonal.add(next);
        }
        return diagonal;
    }

    public static int colorful(int a) {
        List<Integer> digits = new ArrayList<>();
        Set<Integer> products = new HashSet<>();
        for (int x = a; x != 0; x = x / 10) {
            digits.add(x % 10);
        }
        int nDigits = digits.size();
        for (int pLen = 1; pLen <= nDigits; pLen++) {
            for (int i = 0; i <= nDigits - pLen; i++) {
                int nextP = 1;
                for (int j = i; j < nDigits && j < i + pLen; j++) {
                    nextP *= digits.get(j);
                }
                if (products.contains(nextP)) {
                    return 0;
                } else {
                    products.add(nextP);
                }
            }
        }
        return 1;
    }

    public static boolean isPrime(int n) {
        if ((n < 2) || ((n % 2 == 0) && (n != 2))) {
            return false;
        }
        for (int i = 3; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int bulbs(ArrayList<Integer> a) {
        int switches = 0;
        int N = a.size();
        int on = 1;
        for (int i = 0; i < N; i++) {
            while ((i < N) && (a.get(i) == on)) {
                i++;
            }
            if (i == N) {
                return switches;
            }
            switches++;
            on = (on == 1 ? 0 : 1);
        }
        return switches;
    }

    public static int gcd(int a, int b) {
        if ((a == 0) || (b == 0)) {
            return Integer.max(a, b);
        }
        if (a == b) {
            return a;
        }
        if (b < a) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        if (b % a == 0) {
            return a;
        }
        return gcd(b - a, a);
    }

    public static Set<Integer> primes(int n) {
        Set<Integer> primesN = new HashSet<>();
        for (int i = 2; i <= n; i++) {
            primesN.add(i);
        }
        double sqrtN = Math.sqrt(n);
        for (int i = 2; i <= sqrtN; i++) {
            if (primesN.contains(i)) {
                for (int p = 2; p * i <= n; p++) {
                    primesN.remove(p * i);
                }
            }
        }
        return primesN;
    }

    public static boolean isExponentiable(int n) {
        int maxY = (int) Math.sqrt(n);
        for (int y = 2; y <= maxY; y++) {
            int x = n;
            while (x > 1 && ((x % n) == 0)) {
                x = x / y;
            }
            if (x == 1) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Integer> wave(ArrayList<Integer> a) {
        Collections.sort(a);
        for (int i = 0; i < a.size() - 1; i += 2) {
            int tmp = a.get(i);
            a.set(i, a.get(i + 1));
            a.set(i + 1, tmp);
        }
        return a;
    }

    public static boolean isPowerOf2(String ns) {
        BigInteger n = new BigInteger(ns);
        BigInteger x = new BigInteger(ns);
        BigInteger two = new BigInteger("2");
        while (x.compareTo(BigInteger.ONE) > 0 && ((x.mod(two)).compareTo(BigInteger.ZERO) == 0)) {
            x = x.divide(two);
        }
        if (x.compareTo(BigInteger.ONE) == 0) {
            return true;
        }
        return false;
    }

    public static int[] primesums(int n) {
        int[] rv = null;
        for (int i = 2; i <= n / 2; i++) {
            if (isPrime(i) && isPrime(n - i)) {
                rv = new int[2];
                rv[0] = i;
                rv[1] = n - i;
                return rv;
            }
        }
        return rv;
    }

    public static int pow(int datum, int e) {
        if (e == 1) {
            return datum;
        } else {
            int t = pow(datum, e / 2);
            if (e % 2 == 1) {
                return t * t * e;
            } else {
                return t * t;
            }
        }
    }

    public static class subarray implements Comparable<subarray> {

        int start;
        int end;
        long sum;

        public subarray(int s, int e, List<Integer> a) {
            start = s;
            end = e;
            sum = 0;
            for (int i = s; i <= e; i++) {
                sum += (long) a.get(i);
            }
        }

        public int length() {
            return end - start;
        }

        @Override
        public int compareTo(subarray s) {
            if (s == this) {
                return 0;
            }
            if (this.sum == s.sum) {
                if (this.length() == s.length()) {
                    return s.start - this.start;
                }
                return this.length() - s.length();
            }
            return (this.sum > s.sum ? 1 : -1);
        }

    }

    public static List<Integer> maxset(List<Integer> a) {
        subarray max = null;
        int N = a.size();
        for (int i = 0; i < N;) {
            while (i < N && a.get(i) < 0) {
                i++;
            }
            if (i < N) {
                int start = i;
                while (i < N && a.get(i) >= 0) {
                    i++;
                }
                int end = i - 1;
                subarray nextsa = (new subarray(start, end, a));
                if (max == null || max.compareTo(nextsa) < 0) {
                    max = nextsa;
                }
            }
        }
        ArrayList<Integer> rv = new ArrayList<>();
        if (max != null) {
            for (int k = max.start; k <= max.end; k++) {
                rv.add(a.get(k));
            }
        }
        return rv;
    }

    public static List<Integer> findRange(List<Integer> numbers, int n) {
        List<Integer> rv = new ArrayList<>();
        int N = numbers.size();
        int mid = getIndex(numbers, n, true);
        if (numbers.get(mid) != n) { // number of interest is not in the list
            rv.add(-1);
            rv.add(-1);
        } else {
            rv.add(mid);
            rv.add(getIndex(numbers, n, false));
        }
        return rv;
    }

    private static int getIndex(List<Integer> numbers, int n, boolean lowEnd) {
        int mid = -1;
        int N = numbers.size();
        int lo = 0, hi = N - 1;
        while (lo <= hi) {
            mid = lo + (hi - lo) / 2;
            if (numbers.get(mid) == n) {
                if (lowEnd) {
                    if ((mid == 0) || numbers.get(mid - 1) < n) {
                        break;
                    } else {
                        hi = mid - 1;
                    }
                } else if ((mid == N - 1) || numbers.get(mid + 1) > n) {
                    break;
                } else {
                    lo = mid + 1;
                }
            }
            if (numbers.get(mid) > n) {
                hi = mid - 1;
            }
            if (numbers.get(mid) < n) {
                lo = mid + 1;
            }
        }
        return mid;
    }

    public static int maxcoin(List<Integer> a) {
        if (a.isEmpty()) {
            return 0;
        }
        int[][] mem = new int[a.size()][a.size()];
        return max(a, 0, a.size() - 1, mem);
    }

    private static int max(List<Integer> coins, int i, int j, int[][] mem) {
        if (i > j) {
            return 0;
        } else if (i == j) {
            return coins.get(i);
        }
        if (mem[i][j] == 0) {
            int vi = coins.get(i) + Math.min(max(coins, i + 2, j, mem), max(coins, i + 1, j - 1, mem));
            int vj = coins.get(j) + Math.min(max(coins, i + 1, j - 1, mem), max(coins, i, j - 2, mem));
            mem[i][j] = Math.max(vi, vj);
        }
        return mem[i][j];
    }

    public static long revBits(long a) {
        long lower = 0x1;
        long upper = lower << 31;
        long aRev = a;
        while (upper > lower) {
            if (((lower & aRev) == lower) && (((upper & aRev) == 0))) {
                aRev = aRev & ~lower;
                aRev = aRev | upper;
            } else if (((upper & aRev) == upper) && (((lower & aRev) == 0))) {
                aRev = aRev & ~upper;
                aRev = aRev | lower;
            }
            lower = lower << 1;
            upper = upper >> 1;
        }
        return aRev;
    }

    public static int sumClosestElements(List<Integer> a, List<Integer> b, List<Integer> c) {
        int minSoFar = Integer.MAX_VALUE;
        int i = 0, j = 0, k = 0;
        int aN = a.size(), bN = b.size(), cN = c.size();
        int mini = i, minj = j, mink = k;
        while (i < aN && j < bN && k < cN) {
            int min = Integer.min(a.get(i), Integer.min(b.get(j), c.get(k)));
            int max = Integer.max(a.get(i), Integer.max(b.get(j), c.get(k)));
            if (max - min < minSoFar) {
                minSoFar = max - min;
                mini = i;
                minj = j;
                mink = k;
            }
            if (minSoFar == 0) {
                break;
            }
            if (a.get(i) == min) {
                i++;
            } else if (b.get(j) == min) {
                j++;
            } else {
                k++;
            }
        }
        return getMin(a.get(mini), b.get(minj), c.get(mink));
    }

    private static int getMin(int a, int b, int c) {
        return Integer.max(Math.abs(a - b), Integer.max(Math.abs(b - c), Math.abs(c - a)));
    }

    public static class Container implements Comparable<Container> {

        int x0, y0;
        int x1, y1;

        public Container(int x0, int y0, int x1, int y1) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
        }

        public int area() {
            return Integer.min(y0, y1) * Math.abs(x0 - x1);
        }

        @Override
        public int compareTo(Container o) {
            if (o == this) {
                return 0;
            }
            int mine = this.area();
            int theirs = o.area();
            if (mine > theirs) {
                return 1;
            }
            if (mine < theirs) {
                return -1;
            }
            return 0;
        }

    }

    public static int maxContainer(List<Integer> a) {
        int N = a.size();
        Container max = new Container(0, a.get(0), N - 1, a.get(N - 1));
        int i = 0, j = N - 1;
        while (i < j) {
            if (a.get(i) > a.get(j)) {
                j--;
            } else {
                i++;
            }
            Container next = new Container(i, a.get(i), j, a.get(j));
            if (next.compareTo(max) > 0) {
                max = next;
            }
        }
        return max.area();
    }

    private static boolean notDup(int l, List<Integer> a) {
        return !(l > 0 && Objects.equals(a.get(l), a.get(l - 1)));
    }

    public static int remDups(List<Integer> a) {
        int l = 0;
        for (int i = 0; i < a.size(); i++) {
            a.set(l, a.get(i));
            if (notDup(l, a)) {
                l++;
            }
        }
        return l;
    }

    public static int diffK(List<Integer> a, int b) {
        int N = a.size();
        if (N == 1) {
            return 0;
        }
        for (int i = 0; i < N; i++) {
            int seek = a.get(i) + b;
            int lo = 0;
            int hi = N - 1;
            for (int mid = (hi - lo) / 2; lo <= hi; mid = lo + (hi - lo) / 2) {
                if ((a.get(mid) == seek) && mid != i) {
                    return 1;
                }
                if (a.get(mid) > seek) {
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            }
        }
        return 0;
    }

    public static List<List<Integer>> combinations(int n, int k) {
        List<List<Integer>> combs = new ArrayList<>();
        List<Integer> prefix = new ArrayList<>();
        buildCombinations(combs, prefix, k, n);
        return combs;
    }

    private static void buildCombinations(List<List<Integer>> combs, List<Integer> prefix, int k, int n) {
        int pN = prefix.size();
        if (k == 1) {
            for (int s = (pN == 0 ? 1 : prefix.get(pN - 1) + 1); s <= n; s++) {
                List<Integer> nextComb = new ArrayList<>(prefix);
                nextComb.add(s);
                combs.add(nextComb);
            }
        } else {
            for (int s = (pN == 0 ? 1 : prefix.get(pN - 1) + 1); s < n; s++) {
                prefix.add(s);
                buildCombinations(combs, prefix, k - 1, n);
                prefix.remove(prefix.size() - 1);
            }
        }
    }

    public static List<List<Integer>> subsets(List<Integer> a) {
        List<List<Integer>> subsets = new ArrayList<>();
        if (a.isEmpty()) {
            return subsets;
        }
        subsets = makeSubsets(new ArrayList<>(), 0, a, subsets);
        Collections.sort(subsets, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                int o1N = o1.size();
                int o2N = o2.size();
                int N = Integer.min(o1N, o2N);
                for (int i = 0; i < N; i++) {
                    if (!Objects.equals(o1.get(i), o2.get(i))) {
                        return o1.get(i) - o2.get(i);
                    }
                }
                return o1N - o2N;
            }
        });
        return subsets;
    }
    
    private static List<List<Integer>> makeSubsets(List<Integer> prefix, int i, List<Integer> n, List<List<Integer>> ssets) {
        if (i == n.size() - 1) {
            List<Integer> withI = new ArrayList<>(prefix);
            List<Integer> withoutI = new ArrayList<>(prefix);
            withI.add(n.get(i));
            ssets.add(withoutI);
            ssets.add(withI);
        } else {
            prefix.add(n.get(i));
            int ii = prefix.size() - 1;
            makeSubsets(prefix, i + 1, n, ssets);
            prefix.remove(ii);
            makeSubsets(prefix, i + 1, n, ssets);
        }
        return ssets;
    }

    public static List<Integer> grayCode(int a) {
        int N = 1 << a;
        List<Integer> numbers = new ArrayList<>(N);
        List<BitSet> codes = new ArrayList<>(N);
        List<BitSet> tmp = new ArrayList<>();
        makeGrayCodes(a, codes, tmp);
        for (BitSet code : codes) {
            int nextNum = 0;
            for (int i = 0; i < 32; i++) {
                if (code.get(i)) {
                    nextNum |= (1 << i);
                }
            }
            numbers.add(nextNum);
        }
        return numbers;
    }

    private static void makeGrayCodes(int n, List<BitSet> codes, List<BitSet> tmps) {
        if (n == 0) {
            BitSet zeroCode = new BitSet(n);
            codes.add(zeroCode);
        } else {
            makeGrayCodes(n - 1, codes, tmps);
            for (int i = codes.size() - 1; i >= 0; i--) {
                BitSet nextCode = new BitSet(n);
                nextCode.or(codes.get(i));
                nextCode.set(n - 1);
                tmps.add(nextCode);
            }
            codes.addAll(tmps);
            tmps.clear();
        }
    }
}
