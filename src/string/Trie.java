package string;

import java.util.*;

/**
 * Trie is a class that implements an R-way trie.
 * 
 * The root level contains the first letters of all words in the trie.
 * Each letter l maps to a trie containing the next letters of all words 
 * beginning with l. The trie is a recursive, tree-like data structure.
 * 
 * All words in the trie are terminated with a full stop. This enables to 
 * identify a full word that is also a prefix of another. For example "car"
 * will share the first three letters of "carpet", and the map for 'r' will
 * contain { '.', 'p' }, enabling us to identify "car" as a word in the trie.
 * 
 * The API includes methods to add a word to the trie, remove a word from the
 * trie and ask if the trie contains a word. In addition, there is a method 
 * prefixTrieMatching() that takes a word and an index and if there is a word
 * in the trie that matches a prefix of word[index..N] (where N is the length
 * of word) it returns true, otherwise returns false.
 * 
 * @author Colm
 */
public class Trie {
    
    private final Map<Character, Trie> root;
    private static final char FULL_STOP = '.';

    public Trie() {
        root = new HashMap<>();
    }
  
    /**
     * Add the given text to the trie.
     * 
     * If the text is already in the trie no action is taken.
     * It works by matching a prefix in the trie if there is one,
     * and adding new nodes where necessary. When adding t = "carpet"
     * to a trie that contains "car", the first three letters of t
     * will be matched, then nodes will be added for r -> p, p -> e,
     * e -> t. Then a full stop will be added to the node for t.
     * 
     * @param text 
     */
    public void add(String text) {
        Map<Character, Trie> node = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (node.containsKey(c)) {
                Trie tmp = node.get(c);
                node = tmp.root;
            } else {
                node.put(c, new Trie());
                node = node.get(c).root;
            }
        }
        if (!node.containsKey(FULL_STOP)) {
            node.put(FULL_STOP, null);
        }
    }

    /**
     * Return true if the trie contains the given text.
     * Works by matching the ith character of the text with the
     * set of characters at the ith level, returning false if 
     * there is no match, and returning true if the end of the
     * text is reached and there is a full stop in the corresponding
     * node.
     * 
     * @param text
     * @return 
     */
    public boolean contains(String text) {
        Map<Character, Trie> node = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!node.containsKey(c)) {
                return false;
            }
            node = node.get(c).root;
        }
        return node.containsKey(FULL_STOP);
    }

    /**
     * If the given text is in the trie, remove it and return true,
     * otherwise return false.
     * 
     * @param text
     * @return 
     */
    public boolean remove(String text) {
        return rec_remove(text, 0, this);
    }

    /**
     * Helper method for removing a text from the given trie.
     * The text is matched in the trie all the way to the full stop,
     * and then characters of the text are removed starting from the
     * full stop. If there is a character mismatch, the recursion
     * stops and a false is returned. 
     * 
     * The code could be simplified by first checking if the text is
     * in the trie (using contains()) but that would mean two passes
     * over the trie instead of one.
     * 
     * @param text
     * @param i
     * @param trie
     * @return 
     */
    private boolean rec_remove(String text, int i, Trie trie) {
        boolean rv = false;
        if (i < text.length()) {
            char c = text.charAt(i);
            rv = trie.root.containsKey(c);
            rv = rv && rec_remove(text, i + 1, trie.root.get(c));
            if (rv && trie.root.get(c).root.isEmpty()) {
                // only remove the character if there are no other mappings
                trie.root.remove(text.charAt(i));
            }
        } else if (i == text.length() && trie.root.containsKey(FULL_STOP)) {
            trie.root.remove(FULL_STOP);
            rv = true;
        }
        return rv;
    }

    /**
     * Return true if there is a match in the trie with text.substring(offset),
     * i.e. there is a word in the trie that matches text.substring(offset, n)
     * where n > offset && text.length() > n.
     * 
     * @param text
     * @param offset
     * @return 
     */
    public boolean prefixTrieMatching(String text, int offset) {
        char c = text.charAt(offset);
        Map<Character, Trie> node = this.root;
        while (true) {
            if (node.containsKey(c)) {
                Map<Character, Trie> nextNode = node.get(c).root;
                if (nextNode.containsKey(FULL_STOP)) {
                    return true;
                }
                if (++offset == text.length()) {
                    return false;
                }
                node = nextNode;
                c = text.charAt(offset);
            } else {
                return false;
            }
        }
    }

    /**
     * Convenience method to add a list of words to a trie.
     * 
     * @param patterns 
     */
    void buildTrie(String[] patterns) {
        for (String text : patterns) {
            this.add(text);
        }
    }

}
