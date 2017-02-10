package string;

import java.io.*;
import java.util.*;

class Node {

    public static final int Letters = 4;
    public static final int NA = -1;
    public int next[];

    Node() {
        next = new int[Letters];
        Arrays.fill(next, NA);
    }
}

public class TrieMatching implements Runnable {

    int letterToIndex(char letter) {
        switch (letter) {
            case 'A':
                return 0;
            case 'C':
                return 1;
            case 'G':
                return 2;
            case 'T':
                return 3;
            default:
                assert (false);
                return Node.NA;
        }
    }

    

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
        List<Integer> result = new ArrayList<Integer>();
        Trie trie = new Trie();
        trie.buildTrie(patterns);
        for (int i = 0; i < text.length(); i++) {
            if (trie.prefixTrieMatching(text, i)) {
                result.add(i);
            }
        }
        return result;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String text = in.readLine();
            int n = Integer.parseInt(in.readLine());
            String[] patterns = new String[n];
            for (int i = 0; i < n; i++) {
                patterns[i] = in.readLine();
            }

            List<Integer> ans = solve(text, n, patterns);

            for (int j = 0; j < ans.size(); j++) {
                System.out.print("" + ans.get(j));
                System.out.print(j + 1 < ans.size() ? " " : "\n");
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new Thread(new TrieMatching()).start();
    }
}
