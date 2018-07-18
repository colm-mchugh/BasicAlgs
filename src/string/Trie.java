package string;

import java.util.*;

/**
 * Trie is a class that implements an R-way trie.
 *
 * Trie is a recursive, tree-like data structure. The root trie contains the
 * first letters of all words in the trie, and each letter maps to a trie
 * containing the next letters of all words beginning with that letter.
 *
 * All words in the trie are terminated with a full stop. This enables to
 * identify a full word that is a prefix of another word. For example "car" will
 * share the first three letters of "carpet", and the trie at 'r' will contain {
 * '.', 'p' }, enabling us to identify "car" as a word in the trie. A full stop
 * has a null trie.
 *
 * The API includes methods to add a word to the trie, remove a word from the
 * trie, ask if the trie contains a word, return an iterable of all words in the
 * trie, return an iterable of words with a given prefix, and return the word 
 * that is the longest prefix of a given word. The method prefixTrieMatching() 
 * takes a word and an index and if there is a word that matches a prefix of
 * word[index..N] (where N is the length of words) the result is true, else false.
 * 
 * @author Colm
 */
public class Trie<T> {

    private final Map<Character, Trie<T>> root;
    private final T data;
    private static final char FULL_STOP = '#';

    public Trie() {
        root = new HashMap<>();
        data = null;
    }
    
    public Trie(T data) {
        this.data = data;
        root = null;
    }

    /**
     * Add the given text to the trie.
     *
     * If the text is already in the trie no action is taken. It matches a
     * prefix in the trie if there is one, and adds new nodes where necessary.
     * When adding t = "carpet" to a trie that contains "car", the first three
     * letters of t are matched, then nodes are added for r -> p, p -> e, e ->
     * t. Then a full stop is added: t -> .
     *
     * @param text
     * @param data
     */
    public boolean add(String text, T data) {
        Map<Character, Trie<T>> node = root;
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
            node.put(FULL_STOP, new Trie<>(data));
            return true;
        }
        return false;
    }

    /**
     * Add the given character to the trie.
     * 
     * @param c
     * @param data
     * @return 
     */
    public boolean add(char c, T data) {
        Map<Character, Trie<T>> node = root;
        if (!node.containsKey(c)) {
            node.put(c, new Trie<>());
            Trie<T> tmp = node.get(c);
            node = tmp.root;
            node.put(FULL_STOP, new Trie<>(data));
            return true;
        }
        return false;
    }
    
    /**
     * Return true if the trie contains the given text.
     *
     * Matches the ith character of the text with the set of letters at the ith
     * level, returns false if a match is not found, and returns true if all the
     * text and a full stop is matched.
     *
     * @param text
     * @return
     */
    public boolean contains(String text) {
        Map<Character, Trie<T>> node = root;
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
     * Return the value associated with the given text
     * 
     * @param text
     * @return 
     */
    public T lookup(String text) {
        Map<Character, Trie<T>> node = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!node.containsKey(c)) {
                break;
            }
            node = node.get(c).root;
        }
        return (node.containsKey(FULL_STOP)  ? node.get(FULL_STOP).data : null);
    }
    
    /**
     * If the given text is in the trie, remove it and return true, otherwise
     * return false.
     *
     * @param text
     * @return
     */
    public boolean remove(String text) {
        return rec_remove(text, 0, this);
    }

    /**
     * Helper method for removing a text from the given trie.
     *
     * The text is matched in the trie all the way to the full stop, and then
     * characters of the text are removed starting from the full stop and
     * working backwards. If there is a character mismatch, the recursion stops
     * and a false is returned.
     *
     * The code could be simplified by first checking if the text is in the trie
     * (using contains()) but that would mean two passes over the trie instead
     * of one.
     *
     * @param text
     * @param i
     * @param trie
     * @return
     */
    private boolean rec_remove(String text, int i, Trie<T> trie) {
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
     * Return true if there is a match in the trie with the given text and
     * offset. If there is a word in the trie that matches
     * text.substring(offset, i) where i >= offset && text.length() > i, then
     * return true. Otherwise, false.
     *
     * @param text
     * @param offset
     * @return
     */
    public boolean prefixTrieMatching(String text, int offset) {
        char c = text.charAt(offset);
        int i = offset;
        Map<Character, Trie<T>> node = this.root;
        while (true) {
            if (node.containsKey(c)) {
                Map<Character, Trie<T>> nextNode = node.get(c).root;
                if (nextNode.containsKey(FULL_STOP)) {
                    // found a match for text.substring(offset, i)
                    return true;
                }
                if (++i == text.length()) {
                    // end of text without match => return false
                    return false;
                }
                // move to next level in trie and next char in text
                node = nextNode;
                c = text.charAt(i);
            } else {
                // this level in the trie does not contain the current char of text
                return false;
            }
        }
    }

    /**
     * Convenience method to add a list of words to a trie.
     *
     * @param patterns
     */
    public void buildTrie(String[] patterns) {
        for (String text : patterns) {
            this.add(text, null);
        }
    }

    /**
     * Return an iterable of all the words in the trie.
     *
     * @return
     */
    public Iterable<String> words() {
        return wordsStartingWith("");
    }

    /**
     * Return an iterable of all the words in the trie starting with the given
     * prefix.
     *
     * Navigate from the root trie to the trie containing the last character in
     * the prefix. If a character is not matched on the way, return an empty
     * iterable. Otherwise, once the trie for the last character in the prefix
     * is located, collect all the words from that trie on, with prefix at the
     * start of each word.
     *
     * @param prefix
     * @return
     */
    public Iterable<String> wordsStartingWith(String prefix) {
        Queue<String> allWords = new ArrayDeque<>();
        Trie<T> t = this;
        for (int i = 0; i < prefix.length(); i++) {
            if (!t.root.containsKey(prefix.charAt(i))) {
                return allWords;
            }
            t = t.root.get(prefix.charAt(i));
        }
        collect(allWords, prefix, t);
        return allWords;
    }

    /**
     * Recursively collect all the words in the given trie into the given queue.
     *
     * @param allWords
     * @param prefix
     * @param t
     */
    private void collect(Queue<String> allWords, String prefix, Trie<T> t) {
        for (char c : t.root.keySet()) {
            if (c == FULL_STOP) {
                // prefix has been matched as a full word
                allWords.add(prefix);
            } else {
                // prepend c to the prefix and collect words in c's trie
                collect(allWords, prefix + c, t.root.get(c));
            }
        }
    }

    /**
     * Return the word in the trie that is the longest prefix of the given text.
     * 
     * Match the text in the trie, and keep track of the longest word found on 
     * the way by recording the index whenever a full stop is encountered. Stop
     * on a mismatch, or on reaching the end of t. Then, return the prefix that 
     * is the longest word found. This will be "" if no word is found. Note that
     * there is a special case if t is a complete word in the trie, this is 
     * checked for at the end of the matching.
     *  
     * @param t
     * @param offset
     * @return 
     */
    public String longestPrefixOf(String t, int offset) {
        int l = 0;
        Map<Character, Trie<T>> node = this.root;
        boolean matched = true;
        for (int i = offset; i < t.length() && matched; i++) {
            if (node.containsKey(FULL_STOP)) {
                l = i;
            }
            if (!node.containsKey(t.charAt(i))) {
                matched = false;
            } else {
                node = node.get(t.charAt(i)).root;
            }
        }
        if (matched && node.containsKey(FULL_STOP)) {
            // t is a word in the trie - return it
            return t.substring(offset);
        }
        return t.substring(offset, l);
    }
    
    public String longestPrefixOf(String t) {
        return longestPrefixOf(t, 0);
    }    
}
