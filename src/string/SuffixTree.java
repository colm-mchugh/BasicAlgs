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

    private Map<Suffix, Set<Suffix>> tree;
    private Suffix root;

    protected void link(Suffix from, Suffix to) {
        if (!tree.containsKey(from)) { // Vertex from is not in the graph
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
                    nextSuffix.index = nextSuffix.index + lcpMax;
                    level = tree.get(parent);
                }
            }
            if (lcpMax == 0) {
                // simple case: 
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
