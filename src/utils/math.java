package utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

public class math {

    /**
     * Given an N x N matrix M, return a list of all the diagonals in M.
     *
     * @param M
     * @return
     */
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

    /**
     * Given a number, return 1 if it is colorful, 0 otherwise.
     *
     * A number is colorful if all the contiguous subsequences of it's digits
     * produce a different product. If a number has N digits, then it has SUM(N)
     * - 1 subsequences, from 1 digit length to N - 1 digit length. If all of
     * them yield a different product, the number is colorful.
     *
     * @param a
     * @return
     */
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

    /**
     * Given a number, return true if it is prime, false otherwise.
     *
     * @param n
     * @return
     */
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

    /**
     * Input: a list of bulb states: on = 1, off = 0. Switching a bulb b changes
     * all bulb states to the right of b.
     *
     * Output: the minimum number of times the bulbs need to be switched before
     * they are all on.
     *
     * @param a
     * @return
     */
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

    /**
     * Return the greatest common divisor of two numbers. Uses Euclid's
     * algorithm: successively reduce the bigger by the smaller until one of: at
     * least one of them is zero; they're both the same; the smaller wholly
     * divides the bigger.
     *
     * @param a
     * @param b
     * @return
     */
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

    /**
     * Return all prime numbers less than the given number. Uses Erathothenes
     * algorithm: create a list of all numbers up to and including the number,
     * then, for each prime p, remove all its multiples.
     *
     * @param n
     * @return
     */
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

    /**
     * Return true if the given number can be expressed as an exponent x^y.
     * Proceed by brute force: for each possible exponent y in 2..sqrt(N), check
     * if N can be successively divided by y without any remainders.
     *
     * @param n
     * @return
     */
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

    /**
     * Transform the given list into a wave. A wave is a sorted list of numbers
     * with every pair swapped, yielding two interleaved sequences, with the
     * property: a[i] < a[i-1] && a[i] < a[i+1] for odd i,
     *           a[i] > a[i-1] && a[i] > a[i+1] for even i.
     *
     * @param a
     * @return
     */
    public static ArrayList<Integer> wave(ArrayList<Integer> a) {
        Collections.sort(a);
        for (int i = 0; i < a.size() - 1; i += 2) {
            int tmp = a.get(i);
            a.set(i, a.get(i + 1));
            a.set(i + 1, tmp);
        }
        return a;
    }

    /**
     * Return true if the number in the given string can be expressed as a power
     * of 2, false otherwise.
     *
     * @param ns
     * @return
     */
    public static boolean isPowerOf2(String ns) {
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

    /**
     * Given a number N, return two prime numbers that sum to N. Start from i=2,
     * incrementally test if i and (N-i) are both primes. If so, we've found two
     * primes that sum to the target.
     *
     * @param n
     * @return
     */
    public static int[] primesums(int n) {
        int[] primesum = null;
        for (int i = 2; i <= n / 2; i++) {
            if (isPrime(i) && isPrime(n - i)) {
                primesum = new int[2];
                primesum[0] = i;
                primesum[1] = n - i;
                return primesum;
            }
        }
        return primesum;
    }

    /**
     * Return x to the power of e. Use recursion, saving the result of x to the
     * power of e/2, to give log(X) running time.
     *
     * @param x
     * @param e
     * @return
     */
    public static int pow(int x, int e) {
        if (e == 0) {
            return 1;
        }
        if (e == 1) {
            return x;
        } else {
            int t = pow(x, e / 2);
            if (e % 2 == 1) {
                return t * t * e;
            } else {
                return t * t;
            }
        }
    }

    /**
     * Return a list of all stepping numbers between a and b, inclusive.
     *
     * A stepping number is a number whose digits differ by at most 1.
     *
     * @param a
     * @param b
     * @return
     */
    public ArrayList<Integer> stepnum(int a, int b) {
        ArrayList<Integer> steppingNumbers = new ArrayList<>();
        for (int N = a; N <= b; N++) {
            boolean isStepping = true;
            for (int x = N, dPrev = 0, d = -1; x != 0 && isStepping; x = x / 10) {
                dPrev = d;
                d = x % 10;
                if ((dPrev != -1) && (Math.abs(d - dPrev) != 1)) {
                    isStepping = false;
                }
            }
            if (isStepping) {
                steppingNumbers.add(N);
            }
        }
        return steppingNumbers;
    }

    /**
     * Given a list of positive and negative numbers, return the list of
     * positive numbers within that list that has the maximum sum.
     *
     * Works by successively locating sequences of positive numbers, noting
     * their sum, and keeping track of the sequence with the largest sum found.
     *
     * @param a
     * @return
     */
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

    public static class subarray implements Comparable<subarray> {

        int start;
        int end;
        long sum;

        /**
         * subarray is the sum of a contiguous subsequence of a list of numbers.
         *
         * @param s
         * @param e
         * @param a
         */
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
                // Break ties by comparing on length
                if (this.length() == s.length()) {
                    return s.start - this.start;
                }
                return this.length() - s.length();
            }
            return (this.sum > s.sum ? 1 : -1);
        }

    }

    /**
     * Given a list of numbers and a number n, return the range {s,t} of that
     * number in the list, where s is the index of the first occurrence of n and
     * t is the index of the last occurrence of n. If s and t are -1, n is not
     * in the list.
     *
     * @param numbers
     * @param n
     * @return
     */
    public static int[] findRange(List<Integer> numbers, int n) {
        int[] range = {-1, -1}; // -1 means n is not in the list
        int N = numbers.size();
        int mid = getIndex(numbers, n, true);
        if (numbers.get(mid) == n) { // number of interest is not in the list
            range[0] = (mid);
            range[1] = (getIndex(numbers, n, false));
        }
        return range;
    }

    /**
     * Locate the number n in numbers using binary search. The flag 'lowEnd'
     * means locate the first occurrence of n; if false, the index returned is
     * that of the last occurrence of n. In the case where n appears exactly
     * once in numbers, lowEnd and !lowEnd will return the same index.
     *
     * @param numbers
     * @param n
     * @param lowEnd
     * @return
     */
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

    /**
     * Determine the maximum sum that can be produced by alternately selecting
     * from the start or end of the given list of numbers.
     *
     * @param a list of integers
     * @return
     */
    public static int maxcoin(List<Integer> a) {
        if (a.isEmpty()) {
            return 0;
        }
        int[][] mem = new int[a.size()][a.size()];
        return max(a, 0, a.size() - 1, mem);
    }

    /**
     * Determine the maximum possible sum that can be made from the segment
     * [start,end] of the coins array. The recurrence relation is as follows:
     * Empty coins => 0 One coin => that coin value Otherwise, determine the
     * maximum of taking from the start or end of the coins. Both (start, end),
     * need to include the minimum of their next choice, assuming that their
     * opponent will choose the next best possible coin. Record the maximum in
     * the memo.
     *
     * @param coins list of numbers
     * @param start start index
     * @param end end index
     * @param mem memo, to avoid re-computing
     * @return
     */
    private static int max(List<Integer> coins, int start, int end, int[][] mem) {
        if (start > end) {
            return 0;
        } else if (start == end) {
            return coins.get(start);
        }
        if (mem[start][end] == 0) {
            int vi = coins.get(start) + Math.min(max(coins, start + 2, end, mem), max(coins, start + 1, end - 1, mem));
            int vj = coins.get(end) + Math.min(max(coins, start + 1, end - 1, mem), max(coins, start, end - 2, mem));
            mem[start][end] = Math.max(vi, vj);
        }
        return mem[start][end];
    }

    /**
     * Reverse the bits of a 32 bit, unsigned integer.
     *
     * Note: java doesn't have 32 bit unsigned ints, so long is used.
     *
     * @param a
     * @return
     */
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

    /**
     * Given 3 sorted arrays A, B, C, return the minimum of: Max(|A[i] - B[j]|
     * |B[j] - C[k]|, |C[k] - A[i]|)
     *
     * Start with the minimum elements as: A[0], B[0], C[0] and i,j,k = 0.
     *
     * Determine the min and max of A[i], B[j], C[k] on each loop iteration,
     * only incrementing the index that contains the min of A[i], B[j], C[k]. If
     * a |max - min| is found that is smaller than the current overall minium,
     * record the current indices in the minimum indices; i.e. minimum elements
     * = A[i], B[j], C[k] whenever a smaller |max - min| is found.
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
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

    /**
     * Given a list of positive numbers [n0, n1, ... nN], where each ni
     * represents a coordinate (i, Ni), return the maximum possible area made by
     * any two of the numbers in the list.
     *
     * Works by assuming the largest possible container is with n0, nN and
     * working from both ends of the list to seek larger possible container
     * areas.
     *
     * @param a
     * @return
     */
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

    /**
     * Utility class that represents a rectangle with given coordinates
     */
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

    /**
     * Remove duplicates from a sorted list by overwriting duplicate entries with
     * the next non duplicate. Return the length of the list with all duplicates
     * removed.
     *
     * @param a
     * @return
     */
    public static int remDups(List<Integer> a) {
        int l = 1;
        for (int i = 1; i < a.size(); i++) {
            a.set(l, a.get(i));
            // Don't increment l if a[l] == previous element. 
            // The duplicate will get overwritten by a[i]/
            l = l + (Objects.equals(a.get(l), a.get(l - 1))? 0 : 1);
        }
        return l;
    }

    /**
     * Given a list N and an element that may be in that list 0 or more times,
     * remove all occurrences of the element from the list. Return the logical
     * size of the list after all occurrences have been removed.
     *
     * @param N
     * @param el
     * @return
     */
    public int filterElement(List<Integer> N, int el) {
        int sz = 0;
        for (int i = 0; i < N.size(); i++) {
            N.set(sz, N.get(i));
            if (N.get(sz) != el) {
                sz++;
            }
        }
        return sz;
    }

    /**
     * Given a sorted list of numbers and a target number, find if there are two
     * elements of the list i, j such that: a[i] - a[j] = target (i!=j)
     *
     * Proceeds by taking each element a[i], and then seeking, via binary
     * search, for an element with value a[i] + target.
     *
     * @param a
     * @param target
     * @return
     */
    public static int diffK(List<Integer> a, int target) {
        int N = a.size();
        if (N == 1) {
            return 0;
        }
        for (int i = 0; i < N; i++) {
            int seek = a.get(i) + target;
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

    /**
     * Given positive numbers n, k return an ordered list of the possible
     * combinations of k numbers from the range [1,n], i.e. all the possible
     * combinations of n choose k.
     *
     * @param n
     * @param k
     * @return
     */
    public static List<List<Integer>> combinations(int n, int k) {
        List<List<Integer>> combs = new ArrayList<>();
        List<Integer> prefix = new ArrayList<>();
        buildCombinations(combs, prefix, k, n);
        return combs;
    }

    /**
     * Maintain a prefix of the current possible combination. Works by
     * successively adding the next possible number to the prefix, (an increment
     * of the last number in the prefix, or 1 if the prefix is empty), and then
     * either adding it to the current list of combinations, or, if there are
     * still more numbers to add to the prefix, passing it on to the next (k -
     * 1) level.
     *
     * @param combs
     * @param prefix
     * @param k
     * @param n
     */
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

    private static List<List<Integer>> makeSubsets(List<Integer> prefix, int i,
            List<Integer> n, List<List<Integer>> ssets) {
        if (i == n.size() - 1) {
            ssets.add(new ArrayList<>(prefix)); // generate a set without N[i]
            List<Integer> withI = new ArrayList<>(prefix);
            withI.add(n.get(i));
            ssets.add(withI); // generate a set with N[i]
        } else {
            prefix.add(n.get(i)); // include element N[i] in the prefix
            makeSubsets(prefix, i + 1, n, ssets);
            prefix.remove(prefix.size() - 1); // don't include element N[i] in the prefix
            makeSubsets(prefix, i + 1, n, ssets);
        }
        return ssets;
    }

    /**
     * Given a number of bits n, return a gray code sequence of length 2^n
     * @param n
     * @return 
     */
    public static List<Integer> grayCode(int n) {
        int N = 1 << n;
        List<Integer> numbers = new ArrayList<>(N);
        List<BitSet> codes = new ArrayList<>(N);
        List<BitSet> tmp = new ArrayList<>();
        makeGrayCodes(n, codes, tmp);
        // Convert each bit set to its corresponding integer.
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

    /**
     * Use the following recurrence relation to generate a gray code sequence:
     * Given a gray code sequence codes of length N, produce the next N codes
     * by reversing the given codes and prepending a one bit to each code.
     * 
     * @param n number of bits; => sequence will be 2^n in length
     * @param codes currently generated gray codes
     * @param tmps for temporary processing, used in generating the second half of each sequence.
     * 
     */
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

    /**
     * Given a list of distinct numbers, return all possible permutations of 
     * these numbers.
     * 
     * @param a
     * @return 
     */
    public static List<List<Integer>> permutations(List<Integer> a) {
        return buildPermutations(a, 0, new ArrayList<>());
    }

    /**
     * Build all possible permutations of the given list of numbers.
     * 
     * Works by ascending levels k from 0 to N - 1, and at each level swapping
     * an element of the list and building the next permutation from this.
     * 
     * The element must be swapped back to enable subsequent permutation to be
     * generated.
     * 
     * @param a
     * @param k
     * @param perms
     * @return 
     */
    private static List<List<Integer>> buildPermutations(List<Integer> a, int k, List<List<Integer>> perms) {
        if (k >= a.size()) {
            return perms;
        }
        for (int i = k; i < a.size(); i++) {
            Collections.swap(a, k, i);
            buildPermutations(a, k + 1, perms);
            Collections.swap(a, i, k);
        }
        if (k == a.size() - 1) {
            perms.add(new ArrayList<>(a));
        }
        return perms;
    }

    /** 
     * Given a list of numbers N and a target number, find all sub-lists of N
     * such that each sub-list sums to the target. A number may be selected from
     * N any number of times.
     * 
     * @param N
     * @param target
     * @return 
     */
    public static List<List<Integer>> combinations(List<Integer> N, int target) {
        List<List<Integer>> combos = new ArrayList<>();
        if (N == null || N.isEmpty()) {
            return combos;
        }
        Collections.sort(N);
        int nr = N.size() - remDups(N);
        for (int i = 0; i < nr; i++) {
            N.remove(N.size() - 1);
        }
        genCombos(combos, N, target, 0, new ArrayList<>());
        return combos;
    }

    private static void genCombos(List<List<Integer>> combos, List<Integer> a, int N, int i, List<Integer> prefix) {
        if (N == 0) {
            List<Integer> comb = new ArrayList<>(prefix);
            combos.add(comb);
            return;
        }
        for (int j = i; j < a.size(); j++) {
            if (N < a.get(j)) {
                return;
            }
            prefix.add(a.get(j));
            genCombos(combos, a, N - a.get(j), j, prefix);
            prefix.remove(prefix.size() - 1);
        }
    }

    // Return n/N as a percentage
    public static int asPercentage(int n, int N) {
        return (int) Math.round((100.0 * n) / N);
    }

    /**
     * Given a list of N numbers, find the majority number.
     * 
     * A majority number is one that appears more than floor(N/2) times.
     * @param a
     * @return 
     */
    public static int majority(List<Integer> a) {
        int occurrences = Math.floorDiv(a.size(), 2);
        Map<Integer, Integer> freqs = new HashMap<>();
        for (int i : a) {
            if (!freqs.containsKey(i)) {
                freqs.put(i, 1);
            } else {
                freqs.put(i, freqs.get(i) + 1);
            }
            if (freqs.get(i) > occurrences) {
                return i;
            }
        }
        return -1;
    }

    public static void pascalIt(int N) {
        List<Integer> curr = new ArrayList<>();
        List<Integer> prev = new ArrayList<>();
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    curr.add(1);
                } else {
                    curr.add(prev.get(j - 1) + prev.get(j));
                }
            }
            System.out.println(curr);
            prev.clear();
            prev.addAll(curr);
            curr.clear();
        }
    }

    public static void pascal(int N) {
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(pascalNum(i, j));
            }
            System.out.println();
        }
    }
    
    public static List<Integer> pascalK(int k) {
        List<Integer> kthRow = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(pascalNum(i, j));
            }
            System.out.println();
        }
        
        return kthRow;
    }
    
    private static int pascalNum(int N, int j) {
        if (j == 1 || j == N) {
            return 1;
        }
        // naive recursion: memosizing required to reduce time to O(N*N)
        return pascalNum(N - 1, j - 1) + pascalNum(N - 1, j);
    }

    public static int trailingZerosInFactorialOf(int N) {
        int numZeros = 0;
        int fiveMultiple = 5;
        while (N / fiveMultiple >= 1) {
            numZeros += N / fiveMultiple;
            fiveMultiple *= 5;
        }
        return numZeros;
    }

    int getNext(int i, int j, List<Integer> p) {
        if (i - 1 == 0 || i >= p.size()) {
            return 1;
        }
        return p.get(i - 2) + p.get(i - 1);
    }

    public static int excel1(String a) {
        int sum = 0;
        for (int i = a.length() - 1; i >= 0; i--) {
            int n = (a.charAt(i) - 'A') + math.pow(26, a.length() - 1 - i);
            sum += n;
        }
        return sum;
    }

    public static String excel2(int a) {
        StringBuilder sb = new StringBuilder();
        while (a > 0) {
            sb.append((char) ('A' + (a - 1) % 26));
            a = (a - 1) / 26;
        }
        return sb.reverse().toString();
    }

    /**
     * Reverse the digits of the given number.
     *
     * revint(123) = 321 revint(-123) = -321
     *
     * return 0 if revint(n) > Integer.MAX_VALUE
     *
     * @param a
     * @return
     */
    public static int revint(int a) {
        Stack<Integer> digits = new Stack<>();
        for (int n = Math.abs(a); n > 0; n = n / 10) {
            digits.push(n % 10);
        }
        int tenMultiple = 1;
        int rev = 0;
        while (!digits.isEmpty()) {
            int nextDigit = digits.pop();
            if (((long) (nextDigit) * tenMultiple) + rev > Integer.MAX_VALUE) {
                return 0;
            }
            rev = rev + nextDigit * tenMultiple;
            tenMultiple = tenMultiple * 10;
        }
        return (a < 0 ? -rev : rev);
    }

    public static ArrayList<Integer> plus1(ArrayList<Integer> a) {
        int lsb = a.size() - 1;
        int msb = 0;
        while (msb < a.size() && a.get(msb) == 0) {
            msb++;
        }
        ArrayList<Integer> aPlus1 = new ArrayList<>();
        int i = lsb;
        int c = 1;
        while ((c != 0) && i >= msb) {
            int n = a.get(i) + c;
            if (n < 10) {
                c = 0;
                aPlus1.add(n);
            } else {
                aPlus1.add(n % 10);
            }
            i--;
        }
        if (c == 1) {
            aPlus1.add(1);
        } else {
            while (i >= msb) {
                aPlus1.add(a.get(i--));
            }
        }
        for (int k = 0, j = aPlus1.size() - 1; j > k; k++, j--) {
            int tmp = aPlus1.get(k);
            aPlus1.set(k, aPlus1.get(j));
            aPlus1.set(j, tmp);
        }
        return aPlus1;
    }

    public static int positive(List<Integer> a) {
        int j = 0;
        int N = a.size();
        for (int i = 0; i < N; i++) {
            if (a.get(i) <= 0) {
                int tmp = a.get(i);
                a.set(i, a.get(j));
                a.set(j, tmp);
                j++;
            }
        }
        if (j == N) {
            return 1;
        }
        List<Integer> positives = a.subList(j, N);
        int pN = positives.size();
         for (int i = 0; i < pN; i++) {
            int next = Math.abs(positives.get(i)) - 1;
            if (next < pN && positives.get(next) > 0) {
                positives.set(next, -positives.get(next));
            }
        }
        for (int i = 0; i < pN; i++) {
            if (positives.get(i) > 0) {
                return i + 1;
            }
        }
        return N + (j == 0? 1 : 0);
    }
    
    
    public static  List<Integer> repeatedNumber(final List<Integer> a)  {
        int dup = -1;
        int missing = -1;
        int N = a.size();
        Set<Integer> s = new HashSet<>(N);
        for (int x : a) {
            if (s.contains(x)) {
                dup = x;
            }
            s.add(x);
        }
        for (int i = 1; i <= N; i++) {
            if (!s.contains(i)) {
                missing = i;
                break;
            }
        }
        List<Integer> dupMissing = new ArrayList<>(2);
        dupMissing.add(dup);
        dupMissing.add(missing);
        return dupMissing;
    }

    public static class Node {

        int val;
        Node next;

        public Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }

    }

    private void remDups(Node list) {
        Node current = list;
        while (current != null) {
            if (current.next != null) {
                if (current.val < current.next.val) {
                    current = current.next;
                } else {
                    Node tmp = current.next;
                    current.next = current.next.next;
                    tmp.next = null;
                }
            } else {
                current = current.next; // current is null
            }
        }
    }

    public static void partitionL(Node list, int x) {
        int i = 0;
        int iB = -1;
        Node l = list;
        Node p = list;
        Node lB = null, pB = null;
        while (l != null) {
            if (l.val >= x && iB == -1) {
                lB = l;
                pB = p;
                iB = i;
                p = l;
                l = l.next;
            } else {
                if (l.val < x && iB > -1 && i > iB) {
                    Node tmp = l;
                    p.next = l.next;
                    l = l.next;
                    if (pB == null) {
                        pB = tmp;
                        tmp.next = lB;
                    } else {
                        pB.next = tmp;
                        tmp.next = lB;
                        pB = tmp;
                    }
                } else {
                    p = l;
                    l = l.next;
                }
            }
            i++;
        }
    }
    
    public static Node nthEnd(Node list, int n) {
        int N = 0;
        for (Node l = list; l != null; l = l.next) {
            N++;
        }
        if (n > N || n == 1) {
            Node r = list;
            list = r.next;
            r.next = null;
        } else {
            int np = 0;
            Node p = list;
            Node l = list;
            while (np != N - n) {
                np++;
                p = l;
                l = l.next;
            }
            Node r = p.next;
            p.next = r.next;
            r.next = null;   
        }
        return list;
    }
}
