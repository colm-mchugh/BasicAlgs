package string;

import java.util.*;
import java.io.*;
import java.util.zip.CheckedInputStream;

public class SuffixTree {

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

    public static class Suffix implements Comparable<Suffix> {

        public int index;
        public int length;
        private String text;

        public Suffix(int index, int length, String text) {
            this.index = index;
            this.length = length;
            this.text = text;
        }

        public Suffix(int index, String text) {
            this.index = index;
            this.text = text;
            this.length = Integer.MAX_VALUE;
        }

        @Override
        public int compareTo(Suffix o) {
            if (this == o) {
                return 0;
            }
            int len = Integer.min(o.length(), this.length());
            for (int i = 0; i < len; i++) {
                if (this.charAt(i) < o.charAt(i)) {
                    return -1;
                }
                if (this.charAt(i) > o.charAt(i)) {
                    return 1;
                }
            }
            return this.length() - o.length();
        }

        public String text() {
            return text;
        }

        public Character charAt(int i) {
            return text.charAt(index + i);
        }

        @Override
        public String toString() {
            if (this.length == Integer.MAX_VALUE) {
                return text.substring(index);
            }
            return text.substring(index, index + length);
        }

        public int length() {
            if (this.length == Integer.MAX_VALUE) {
                return text.length() - index;
            }
            return length;
        }
    }

    private Map<Suffix, Set<Suffix>> tree;
    private Suffix root;

    protected void link(Suffix from, Suffix to) {
        if (!tree.containsKey(from)) { // Suffix from is not in the graph
            tree.put(from, new HashSet<>());
        }
        tree.get(from).add(to);
    }

    private void makeTree(String text) {
        root = new Suffix(-1, text);
        Suffix allText = new Suffix(0, text);
        tree = new HashMap<>();
        link(root, allText);
        for (int i = 1; i < text.length(); i++) {
            Suffix nextSuffix = new Suffix(i, text);
            Set<Suffix> level = tree.get(root);
            Suffix parent = root;
            int lcpMax = 0;
            boolean interiorSplit = false;
            for (boolean done = false; !done;) {
                lcpMax = 0;
                for (Suffix s : level) {
                    int lcp = lcp(s, nextSuffix);
                    if (lcp > 0 && lcp > lcpMax) {
                        lcpMax = lcp;
                        parent = s;
                    }
                }
                if ((lcpMax == 0) || (tree.get(parent) == null)) {
                    done = true;
                } else {
                    if ((lcpMax > 0) && (tree.get(parent) != null) && (lcpMax < parent.length)) {
                        // tricky case: the lcp is a prefix of an interior (non leaf) node.
                        Suffix parentSplit = new Suffix(parent.index + lcpMax,
                                parent.length - lcpMax, text);
                        tree.put(parentSplit, tree.get(parent));
                        Set<Suffix> parentSet = new HashSet<>();
                        parentSet.add(parentSplit);
                        tree.put(parent, parentSet);
                        parent.length = lcpMax;
                        done = interiorSplit = true;
                    }
                    nextSuffix.index = nextSuffix.index + lcpMax;
                    level = tree.get(parent);
                }
            }
            if ((lcpMax == 0) || interiorSplit) {
                // simple case: the next suffix doesn't match any prefix, 
                // or caused an interior split. 
                link(parent, nextSuffix);
            } else {
                // tricky case: parent and nextSuffix share a common prefix of length lcpMax
                // that needs to be refactored into a suffix which will be the parent of
                // nextSuffix and a new sibling
                Suffix newSibling = new Suffix(parent.index + lcpMax, text);
                nextSuffix.index += lcpMax;
                parent.length = lcpMax;
                link(parent, nextSuffix);
                link(parent, newSibling);

            }
        }
    }

    public void collectSuffixEdges(Suffix source, Set<Suffix> visited, List<String> edges) {
        visited.add(source);
        if (this.tree.get(source) != null) {
            for (Suffix s : this.tree.get(source)) {
                if (!visited.contains(s)) {
                    collectSuffixEdges(s, visited, edges);
                    edges.add(s.toString());
                }
            }
        }
    }

    public void print() {
        Queue<Suffix> queue = new ArrayDeque<>();
        Set<Suffix> visited = new HashSet<>();
                
        String level = "\t";
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            Suffix next = queue.remove();
            if (tree.get(next) != null) {
                for (Suffix s : tree.get(next)) {
                    if (!visited.contains(s)) {
                        queue.add(s);
                        visited.add(s);
                        System.out.println(s.toString());
                    }
                }
            }
        }
    }

    // Build a suffix tree of the string text and return a list
    // with all of the labels of its edges (the corresponding 
    // substrings of the text) in any order.
    public List<String> computeSuffixTreeEdges(String text) {
        List<String> result = new ArrayList<>();
        this.makeTree(text);
        Set<Suffix> visited = new HashSet<>();
        this.collectSuffixEdges(root, visited, result);
        return result;
    }

    /**
     * Return the longest common prefix (lcp) of given suffixes.
     *
     * @param s1
     * @param s2
     * @return
     */
    static private int lcp(Suffix s1, Suffix s2) {
        int n = Integer.min(s1.length(), s2.length());
        for (int i = 0; i < n; i++) {
            if (!Objects.equals(s1.charAt(i), s2.charAt(i))) {
                return i;
            }
        }
        return n;
    }

    static public void main(String[] args) throws IOException {
        new SuffixTree().run();
    }

    public void print(List<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        List<String> edges = computeSuffixTreeEdges(text);
        print(edges);
    }
}
