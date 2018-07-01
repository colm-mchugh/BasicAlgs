package string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.junit.Test;

public class TrieTest {

    @Test
    public void testTrieMatch1() {
        String[] patterns = {"AA",};
        int[] expected = {0, 1};
        String text = "AAA";
        runMatches(text, patterns, expected);
    }

    @Test
    public void testTrieMatch2() {
        String[] patterns = {"T",};
        int[] expected = {};
        String text = "AA";
        runMatches(text, patterns, expected);
    }

    @Test
    public void testTrieMatch3() {
        String[] patterns = {"ATCG", "GGGT",};
        int[] expected = {1, 4, 11, 15};
        String text = "AATCGGGTTCAATCGGGGT";
        runMatches(text, patterns, expected);
    }

    @Test
    public void testTrieMatchPrefix() throws Exception {
        String[] patterns = {"A", "AT", "A", "AG"};
        int[] expected = {0, 2, 4};
        String text = "ACATA";
        runMatches(text, patterns, expected);

    }

    @Test
    public void testTriePrefix() {
        String[] patterns = {"are", "area", "base", "cat", "cater", "children", "basement"};
        String text = "caterer";
        List<Integer> result = new ArrayList<>();
        Trie trie = new Trie();
        trie.buildTrie(patterns);
        for (int i = 0; i < text.length(); i++) {
            if (trie.prefixTrieMatching(text, i)) {
                result.add(i);
            }
        }
        System.out.println(result);

        Set<String> pish = new HashSet<>(patterns.length);
        pish.addAll(Arrays.asList(patterns));
        Iterable<String> words = trie.words();
        int cnt = 0;
        for (String word : words) {
            assert pish.contains(word);
            pish.remove(word);
            cnt++;
        }
        assert cnt == patterns.length;
        assert pish.isEmpty();
    }

    @Test
    public void testContains() {
        String[] text = {"she", "she", "sells", "sea", "shells", "by", "the", "sea", "shore"};
        Trie t = new Trie();
        t.buildTrie(text);

        assert t.contains("shore");
        assert !t.contains("shelter");
        assert t.contains("sea");
        assert !t.contains("seas");
        assert !t.contains("sell");
        assert t.contains("sells");
        assert t.contains("shells");
        assert t.contains("by");

        assert t.remove("sea");
        assert !t.remove("shell");
        assert !t.contains("sea");
        assert t.contains("sells");
        assert t.contains("shells");
        assert t.contains("by");
        assert t.remove("shells");
        assert t.contains("sells");
        assert !t.contains("shells");
    }

    @Test
    public void testBeginsWith() {
        String[] text = {"she", "sells", "sea", "shells", "by", "the", "shore",
            "sharing", "shelter", "with", "a", "lonely", "hippopotamus", "staring",
            "vacantly", "at", "surf", "and", "shingle"
        };
        Trie t = new Trie();
        t.buildTrie(text);

        String[] prefixes = {"sh", "she", "shel", "shelf"};
        String[][] expected = {{"she", "shells", "shore", "sharing", "shelter", "shingle"},
        {"she", "shells", "shelter"}, {"shells", "shelter"}, {}};
        for (int i = 0; i < prefixes.length; i++) {
            this.runBeginsWith(prefixes[i], expected[i], t);
        }
        // words beginning with empty prefix is all words in the trie:
        this.runBeginsWith("", text, t);
    }

    void runBeginsWith(String prefix, String[] expected, Trie t) {
        Set<String> expectedWords = new HashSet<>(expected.length);
        expectedWords.addAll(Arrays.asList(expected));
        Iterable<String> words = t.wordsStartingWith(prefix);
        int cnt = 0;
        for (String word : words) {
            assert expectedWords.contains(word);
            expectedWords.remove(word);
            cnt++;
        }
        assert cnt == expected.length;
        assert expectedWords.isEmpty();
    }

    @Test
    public void testLongestPrefixOf() {
        String[] text = {"she", "sells", "sea", "shells", "by", "the", "shore",
            "sharing", "shelter", "with", "a", "lonely", "hippopotamus", "staring",
            "vacantly", "at", "surf", "and", "shingle"
        };
        Trie t = new Trie();
        t.buildTrie(text);
        
        String m = t.longestPrefixOf("she");
        assert m.equalsIgnoreCase("she");
        
        m = t.longestPrefixOf("shell");
        assert m.equalsIgnoreCase("she");

        m = t.longestPrefixOf("shellsort");
        assert m.equalsIgnoreCase("shells");
        
        m = t.longestPrefixOf("shazam");
        assert m.equalsIgnoreCase("");
        
        m = t.longestPrefixOf("staringly");
        assert m.equalsIgnoreCase("staring");
    }

    void runMatches(String text, String[] patterns, int[] expected) {
        List<Integer> result = new ArrayList<>();
        Trie trie = new Trie();
        trie.buildTrie(patterns);
        for (int i = 0; i < text.length(); i++) {
            if (trie.prefixTrieMatching(text, i)) {
                result.add(i);
            }
        }
        for (int e : expected) {
            assert result.contains(e);
        }
        assert result.size() == expected.length;
    }

    class ListNode {

        int val;
        ListNode next;

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

    }

    ListNode reverse(ListNode l) {
        ListNode reversedSoFar = null;
        ListNode curr = l;
        ListNode remainder;
        while (curr != null) {
            remainder = curr.next;
            curr.next = reversedSoFar;
            reversedSoFar = curr;
            curr = remainder;
        }
        return reversedSoFar;
    }

    @Test
    public void testReverse() {
        ListNode l = new ListNode(0, new ListNode(1, new ListNode(2, new ListNode(3, null))));
        ListNode r = reverse(l);
        assert r.val == 3;
    }

    String zigzag(String a, int b) {
        if (b == 1) {
            return a;
        }
        StringBuilder[] rows = new StringBuilder[b];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new StringBuilder(a.length() / b);
        }
        for (int i = 0; i < a.length(); i += 2 * b - 2) {
            int row = 0;
            for (int j = i; j < a.length() && j < i + b; j++) {
                rows[row++].append(a.charAt(j));
            }
            row = b - 2;
            for (int j = i + b; j < a.length() && j < i + 2 * b - 2; j++) {
                rows[row--].append(a.charAt(j));
            }
        }
        StringBuilder result = new StringBuilder(b);
        for (StringBuilder row : rows) {
            result.append(row.toString());
        }
        return result.toString();
    }

    @Test
    public void test0() {
        String input = "ABCD";
        String output = "ACBD";
        assert zigzag(input, 2).equals(output);
    }

    @Test
    public void test1() {
        String input = "PAYPALISHIRING";
        String output = "PAHNAPLSIIGYIR";
        assert zigzag(input, 3).equals(output);
    }

    @Test
    public void test2() {
        String input = "PAYPALISHIRING";
        String output = "PRAIIYHNPSGAIL";
        assert zigzag(input, 6).equals(output);

        assert zigzag(input, input.length()).equals(input);
    }

    @Test
    public void test3() {
        String input = "B";
        String output = "B";
        assert zigzag(input, 1).equals(output);
    }

    int lengthlast(String a) {
        int N = a.length();
        int i = N - 1;
        int len = 0;
        while (i >= 0 && a.charAt(i) == ' ') {
            i--;
        }
        if (i < 0) {
            return 0;
        }
        while (i >= 0 && a.charAt(i) != ' ') {
            i--;
            len++;
        }
        return len;
    }

    @Test
    public void testLengthLast0() {
        assert lengthlast("HelloWorld ") == 10;
    }

    @Test
    public void testLengthLast() {
        assert lengthlast("HelloWorld") == 10;
    }

    @Test
    public void testHelloWorld() {
        assert lengthlast("Hello World") == 5;
    }

    int removeElement(List<Integer> a, int b) {
        int sz = 0;
        for (int i = 0; i < a.size(); i++) {
            a.set(sz, a.get(i));
            if (a.get(sz) != b) {
                sz++;
            }
        }
        return sz;
    }

    @Test
    public void removeEls() {
        List<Integer> l = new ArrayList<>();
        l.add(4);
        l.add(1);
        l.add(1);
        l.add(2);
        l.add(1);
        l.add(3);
        assert removeElement(l, 1) == 3;
        assert l.get(0) == 4;
        assert l.get(1) == 2;
        assert l.get(2) == 3;
    }

    @Test
    public void remDups1() {
        int[] a1 = {1, 2, 2, 3, 4, 4, 4, 5, 6, 6, 6, 6, 6, 7, 8, 8, 8, 8, 9, 10, 10, 10, 10, 10, 10};
        int[] expected = {1, 2, 2, 3, 4, 4, 5, 6, 6, 7, 8, 8, 9, 10, 10};
        validate(a1, expected);
    }

    @Test
    public void remDups2() {
        int[] a1 = {};
        int[] expected = {};
        validate(a1, expected);
    }

    @Test
    public void remDups3() {
        int[] a1 = {1, 2, 2, 3, 4, 4, 4, 5};
        int[] expected = {1, 2, 2, 3, 4, 4, 5};
        validate(a1, expected);
    }

    @Test
    public void remDups4() {
        int[] a1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] expected = a1;
        validate(a1, expected);
    }

    @Test
    public void remDups5() {
        int[] a1 = {0, 0, 0, 0, 1, 2, 3, 3, 4, 10};
        int[] expected = {0, 0, 1, 2, 3, 3, 4, 10};
        validate(a1, expected);
    }

    @Test
    public void remDups6() {
        int[] a1 = {1000, 1000, 1000, 1000, 1001, 1002, 1003, 1003, 1004, 1010};
        int[] expected = {1000, 1000, 1001, 1002, 1003, 1003, 1004, 1010};
        validate(a1, expected);
    }

    List<Integer> fillList(int[] a) {
        List<Integer> l = new ArrayList<>(a.length);
        for (int i : a) {
            l.add(i);
        }
        return l;
    }

    void validate(int[] a1, int[] e) {
        List<Integer> l = fillList(a1);
        int d = removeDups(l);
        assert e.length == d;
        for (int i = 0; i < d; i++) {
            assert l.get(i) == e[i];
        }
    }

    int removeDups(List<Integer> a) {
        int D = 0;
        for (int i = 0; i < a.size(); i++) {
            a.set(D, a.get(i));
            if (notADup(D, a)) {
                D++;
            }
        }
        return D;
    }

    private boolean notADup(int D, List<Integer> a) {
        return !(D > 1 && Objects.equals(a.get(D), a.get(D - 1)) && Objects.equals(a.get(D), a.get(D - 2)));
    }

    @Test
    public void testIP() {
        System.out.println(findIPs("25525511135", 3));
    }

    public static List<String> findIPs(String s, int dots) {
        List<String> ips = new ArrayList<>();
        for (int i = 1; i <= 3 && i < s.length(); i++) {
            String cip = s.substring(0, i);
            if (Integer.parseInt(cip) < 256) {
                if (dots == 1) {
                    if (Integer.parseInt(s.substring(i)) < 256) {
                        ips.add(cip + "." + s.substring(i));
                    }
                } else {
                    for (String ip : findIPs(s.substring(i), dots - 1)) {
                        ips.add(cip + "." + ip);
                    }
                }
            }
        }
        return ips;
    }

}
