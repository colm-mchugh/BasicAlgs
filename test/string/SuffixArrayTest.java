package string;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuffixArrayTest {

    @Test
    public void testLongestRepeatingSubstring() {
        this.validateLongestRepeatingSubstring("ABCZLMNABCZLMNABC" + "$", "ABCZLMNABC");
        this.validateLongestRepeatingSubstring("abcpqrabpqpq$", "ab");
        this.validateLongestRepeatingSubstring("AAAAAAAAAA$", "AAAAAAAAA");
        this.validateLongestRepeatingSubstring("ABABABA$", "ABABA");
        this.validateLongestRepeatingSubstring("ABCDEFG$", "");
        this.validateLongestRepeatingSubstring("ATCGATCGA$", "ATCGA");
    }

    private void validateLongestRepeatingSubstring(String t, String expected) {
        int[] suffixes = SuffixArray.Build(t);
        int[] prefixes = SuffixArray.Lcp(t, suffixes);
        int maxPrefix = prefixes[0];
        int maxSuffix = 0;
        for (int i = 1; i < t.length(); i++) {
            if (prefixes[i] > maxPrefix) {
                maxPrefix = prefixes[i];
                maxSuffix = i;
            }
        }
        String theLongestSubstring = t.substring(suffixes[maxSuffix]).substring(0, maxPrefix);
        assert theLongestSubstring.equals(expected);
    }
    
    @Test
    public void testSortCharacters() {
        String s = "banana$";
        int[] expectedOrder = {6, 1, 3, 5, 0, 2, 4};
        int[] order = SuffixArray.SortCharacters(s);
        for (int i = 0; i < expectedOrder.length; i++) {
            assert expectedOrder[i] == order[i];
        }
        int[] expectedClasses = {2, 1, 3, 1, 3, 1, 0};
        int[] classes = SuffixArray.ComputeCharClasses(s, order);
        for (int i = 0; i < expectedClasses.length; i++) {
            assert expectedClasses[i] == classes[i];
        }
    }

    @Test
    public void testSuffixArrayBuild() {
        int[] gac = {3, 1, 2, 0};
        int[] gagagaga = {8, 7, 5, 3, 1, 6, 4, 2, 0};
        int[] aacgatagcggtaga = {15, 14, 0, 1, 12, 6, 4, 2, 8, 13, 3, 7, 9, 10, 11, 5};
        long now = System.nanoTime();
        compareRawIntArrays(gac, SuffixArray.Build("GAC$"));
        compareRawIntArrays(gagagaga, SuffixArray.Build("GAGAGAGA$"));
        compareRawIntArrays(aacgatagcggtaga, SuffixArray.Build("AACGATAGCGGTAGA$"));
        long timeTaken = System.nanoTime() - now;
        System.out.println("Raw microsecs: " + timeTaken / 1000);
    }

    private void compareRawIntArrays(int[] a1, int[] a2) {
        assert a1.length == a2.length;
        for (int i = 0; i < a1.length; i++) {
            assert a1[i] == a2[i];
        }
    }

    @Test
    public void testSuffixArrayMatching() {
        int[] AAA = {0,1,2};
        validateMatch(AAA, SuffixArray.match("AAA", "A", SuffixArray.Build("AAA")));
        
        int[] empty = {};
        validateMatch(empty, SuffixArray.match("ATA", "G", SuffixArray.Build("ATA")));
        
        int[] ATA = {0, 2, 4};
        validateMatch(ATA, SuffixArray.match("ATATATA", "ATA", SuffixArray.Build("ATATATA")));
        
        int[] TATA = {1, 3};
        validateMatch(TATA, SuffixArray.match("ATATATA", "TATA", SuffixArray.Build("ATATATA")));
        
        int[] TATAT = {1};
        validateMatch(TATAT, SuffixArray.match("ATATATA", "TATAT", SuffixArray.Build("ATATATA")));
        
    }
    
    private void validateMatch(int[] expected, List<Integer> matches) {
        assert expected.length == matches.size();
        for (int m : expected) {
            assert matches.contains(m);
        }
    }

    public int bulbs(ArrayList<Integer> a) {
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

    @Test
    public void testSwitches() {
        int[] a1Data = {0, 1, 0, 1};
        ArrayList<Integer> a1 = new ArrayList<>();
        a1.add(1);
        a1.add(1);
        a1.add(1);
        a1.add(1);
        assert bulbs(a1) == 0;
    }

    ArrayList<Integer> populate(int[] data) {
        ArrayList<Integer> a1 = new ArrayList<>(data.length);
        for (int i = 0; i < data.length; i++) {
            a1.add(i, data[i]);
        }
        return a1;
    }

    private boolean isPrime(int n) {
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

    static class primesum implements Comparable<primesum> {

        int a, b;

        public primesum(int a, int b) {
            this.a = (a <= b ? a : b);
            this.b = (b > a ? b : a);
        }

        @Override
        public int compareTo(primesum o) {
            if ((this == o) || (o.a == this.a && o.b == this.b)) {
                return 0;
            }
            if ((this.a < o.a) || (this.a == o.a && this.b < o.b)) {
                return -1;
            }
            return 1;
        }

    }

    private ArrayList<Integer> primesums(int a) {
        ArrayList<primesum> sums = new ArrayList<>();
        for (int i = 1; i <= a / 2; i++) {
            int j = a - i;
            if (isPrime(i) && isPrime(j)) {
                sums.add(new primesum(i, j));
            }
        }
        ArrayList<Integer> s = new ArrayList<>();
        if (sums.isEmpty()) {
            return s;
        }
        primesum smallest = sums.get(0);
        for (int i = 1; i < sums.size(); i++) {
            if (sums.get(i).compareTo(smallest) < 0) {
                smallest = sums.get(i);
            }
        }

        s.add(smallest.a);
        s.add(smallest.b);
        return s;
    }

    @Test
    public void testPrimes() {
        ArrayList<Integer> t4 = primesums(4);
        assert t4.contains(2) && t4.contains(2);
        ArrayList<Integer> t0 = primesums(100);
        ArrayList<Integer> t1 = primesums(10003292);
        assert t1.contains(349) && t1.contains(10002943);
        ArrayList<Integer> t2 = primesums(10000001);
        assert t2.isEmpty();
        ArrayList<Integer> t3 = primesums(10000021);
        assert t3.contains(2) && t3.contains(10000019);  
    }
    
    int pow(int n, int a) {
        int p = 1;
        for (int i = 1; i <= a; i++) {
            p = p * n;
        }
        return p;
    }
    
    boolean isPower(int a) {
        if (a <= 1) {
            return true;
        }
        for (int i = 2; i <= Math.sqrt(a); i++) {
            boolean found = true;
            for (int p = a; p > 1; p = p / i) {
                if (p%i > 0) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return true;
            }
        }
        return false;
    }
    
    
    
}
