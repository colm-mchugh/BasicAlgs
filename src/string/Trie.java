package string;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Trie {

    class FastScanner {

        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements()) {
                tok = new StringTokenizer(in.readLine());
            }
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

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

    static public void main(String[] args) throws IOException {
        new Trie().run();
    }

    public void print(List<Map<Character, Integer>> trie) {
        for (int i = 0; i < trie.size(); ++i) {
            Map<Character, Integer> node = trie.get(i);
            for (Map.Entry<Character, Integer> entry : node.entrySet()) {
                System.out.println(i + "->" + entry.getValue() + ":" + entry.getKey());
            }
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        int patternsCount = scanner.nextInt();
        String[] patterns = new String[patternsCount];
        for (int i = 0; i < patternsCount; ++i) {
            patterns[i] = scanner.next();
        }
        List<Map<Character, Integer>> trie = buildTrie(patterns);
        print(trie);
    }
}
