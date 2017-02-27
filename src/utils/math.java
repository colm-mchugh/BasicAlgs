package utils;

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
                for (int p = 2; p*i <= n; p++) {
                    primesN.remove(p*i);
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
}
