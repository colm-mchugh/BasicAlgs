package string;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class TrieTest {

    @Test
    public void testBuildTrie() {
        String[] patterns = {"AT", "AG", "AC"};
        Trie trie = new Trie();
        List<Map<Character, Integer>> trieData = trie.buildTrie(patterns);

        assert trieData.get(0).size() == 1 && trieData.get(0).containsKey('A');
        assert trieData.get(1).size() == 3 && trieData.get(1).containsKey('C');
        assert trieData.get(1).size() == 3 && trieData.get(1).containsKey('G');
        assert trieData.get(1).size() == 3 && trieData.get(1).containsKey('T');

    }

    @Test
    public void testBuildTrie2() {
        String[] patterns = {"ATAGA", "ATC", "GAT"};
        Trie trie = new Trie();
        List<Map<Character, Integer>> trieData = trie.buildTrie(patterns);

        assert trieData.get(0).size() == 2 && trieData.get(0).containsKey('A') && trieData.get(0).containsKey('G');
        assert trieData.get(2).size() == 2 && trieData.get(2).containsKey('A') && trieData.get(2).containsKey('C');
        assert trieData.get(1).size() == 1 && trieData.get(1).containsKey('T');
        assert trieData.get(7).size() == 1 && trieData.get(7).containsKey('A');

    }

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
        String[] patterns = {"AT", "A", "AG"};
        int[] expected = {0, 2, 4};
        String text = "ACATA";
        runMatches(text, patterns, expected);

    }

    void runMatches(String text, String[] patterns, int[] expected) {
        List<Integer> result = new ArrayList<Integer>();
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
        for (int i = 0; i < rows.length; i++) {
            result.append(rows[i].toString());
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
        l.add(4); l.add(1); l.add(1); l.add(2); l.add(1); l.add(3);
        assert removeElement(l, 1) == 3;
        assert l.get(0) == 4;
        assert l.get(1) == 2;
        assert l.get(2) == 3;
    }
}
