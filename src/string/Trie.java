package string;

import java.util.*;

public class Trie {

    private Set<Integer> terminalNodes;
    private List<Map<Character, Integer>> trie;

    List<Map<Character, Integer>> buildTrie(String[] patterns) {
        this.trie = new ArrayList<>();
        this.terminalNodes = new HashSet<>();
        trie.add(new HashMap<>());
        for (String pattern : patterns) {
            Integer nextNodeIndex = 0;
            Map<Character, Integer> currentNode = trie.get(nextNodeIndex);
            for (int i = 0; i < pattern.length(); i++) {
                char currentSymbol = pattern.charAt(i);
                nextNodeIndex = trie.size();
                if (currentNode.containsKey(currentSymbol)) {
                    nextNodeIndex = currentNode.get(currentSymbol);
                } else {
                    trie.add(new HashMap<>());
                    currentNode.put(currentSymbol, nextNodeIndex);
                }
                currentNode = trie.get(nextNodeIndex);
            }
            terminalNodes.add(nextNodeIndex);
        }

        return trie;
    }
    
    public boolean prefixTrieMatching(String text, int offset) {
        char symbol = text.charAt(offset);
        Map<Character, Integer> node = trie.get(0);
        while (true) {
            if (node.containsKey(symbol)) {
                if (terminalNodes.contains(node.get(symbol))) {
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

}
