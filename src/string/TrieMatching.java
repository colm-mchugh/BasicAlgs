package string;

import java.util.*;

public class TrieMatching {

    boolean prefixTrieMatching(String text, int offset, List<Map<Character, Integer>> trie) {
        char symbol = text.charAt(offset);
        Map<Character, Integer> node = trie.get(0);
        while (true) {
            if (node.containsKey(symbol)) {
                if (trie.get(node.get(symbol)).isEmpty()) {
                    return true;
                } else {
                    if (++offset == text.length()) {
                        return false;
                    }
                    node = trie.get(node.get(symbol));
                    symbol = text.charAt(offset);
                }
            } else {
                return false;
            }
        }
    }
    
    List<Integer> solve(String text, int n, String[] patterns) {
        List<Integer> result = new ArrayList<>();
        Trie trie = new Trie();
        trie.buildTrie(patterns);
        for (int i = 0; i < text.length(); i++) {
            if (trie.prefixTrieMatching(text, i)) {
                result.add(i);
            }
        }
        return result;
    }

}
