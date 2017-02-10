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
        String[] patterns = { "AA", };
        int[] expected = { 0, 1 };
        String text = "AAA";
        runMatches(text, patterns, expected);
    }

    @Test
    public void testTrieMatch2() {
        String[] patterns = { "T", };
        int[] expected = {  };
        String text = "AA";
        runMatches(text, patterns, expected);
    }
    
    @Test
    public void testTrieMatch3() {
        String[] patterns = { "ATCG", "GGGT", };
        int[] expected = { 1, 4, 11, 15 };
        String text = "AATCGGGTTCAATCGGGGT";
        runMatches(text, patterns, expected);
    }
    
    @Test
    public void testTrieMatchPrefix() throws Exception {
        String[] patterns = { "AT", "A", "AG"};
        int[] expected = { 0, 2, 4};
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
        for(int e : expected) {
            assert result.contains(e);
        }
        assert result.size() == expected.length;
    }
}
