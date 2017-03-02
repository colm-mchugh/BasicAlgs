package utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
                } else {
                    if ((mid == N - 1) || numbers.get(mid + 1) > n) {
                        break;
                    } else {
                        lo = mid + 1;
                    }
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
}
