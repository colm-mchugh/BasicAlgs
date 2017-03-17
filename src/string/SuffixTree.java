package string;

import java.util.*;

public class SuffixTree {

    private final static boolean L = true;
    private final static boolean R = false;

    public static class Substr implements Comparable<Substr> {

        public int index;
        public int length;
        private String text;
        private boolean type;

        public Substr(int index, int length, String text) {
            this.index = index;
            this.length = length;
            this.text = text;
        }

        public Substr(int index, String text) {
            this.index = index;
            this.text = text;
            this.length = Integer.MAX_VALUE;
        }

        @Override
        public int compareTo(Substr o) {
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
            if (this.length == Integer.MIN_VALUE) {
                return "";
            }
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

        private boolean contains(char c) {
            for (int i = 0; i < this.length(); i++) {
                if (this.charAt(i) == c) {
                    return true;
                }
            }
            return false;
        }
    }

    private Map<Substr, Set<Substr>> tree;
    private Substr root;
    private Map<Substr, Substr> paths;

    protected void link(Substr from, Substr to) {
        if (!tree.containsKey(from)) { // Substr from is not in the graph
            tree.put(from, new TreeSet<>());
        }
        tree.get(from).add(to);
    }

    private void buildPaths() {
        this.paths = new HashMap<>();
        this.treeBFS(new SuffixAction() {
            public void f(Substr parent, Substr node) {
                paths.put(node, parent);
            }

            public Substr s() {
                return null;
            }
        });
    }

    private void markNodes() {
        Set<Substr> v = new HashSet<Substr>();
        dfs(root, v);
    }

    private void dfs(Substr source, Set<Substr> visited) {
        visited.add(source);
        if (isLeaf(source)) {
            if (source.contains('#')) {
                source.type = L;
            } else if (source.charAt(source.length() - 1) == '$') {
                source.type = R;
            }
        } else {
            for (Substr s : tree.get(source)) {
                if (!visited.contains(s)) {
                    dfs(s, visited);
                }
            }
            boolean type = L;
            for (Substr s : tree.get(source)) {
                if (s.type != L) {
                    type = R;
                    break;
                }
            }
            source.type = type;
        }
    }

    private void addTextToTree(String text, int startIndex) {
        for (int i = startIndex; i < text.length(); i++) {
            Substr nextSuffix = new Substr(i, text);
            Set<Substr> level = tree.get(root);
            Substr parent = root;
            int lcpMax = 0;
            boolean interiorSplit = false;
            for (boolean done = false; !done;) {
                lcpMax = 0;
                for (Substr s : level) {
                    int lcp = lcp(s, nextSuffix);
                    if (lcp > 0 && lcp > lcpMax) {
                        lcpMax = lcp;
                        parent = s;
                    }
                }
                if ((lcpMax == 0) || (isLeaf(parent))) {
                    done = true;
                } else {
                    if ((lcpMax > 0) && (tree.get(parent) != null) && (lcpMax < parent.length)) {
                        // tricky case: the lcp is a prefix of an interior (non leaf) node.
                        Substr parentSplit = new Substr(parent.index + lcpMax,
                                parent.length - lcpMax, parent.text);
                        tree.put(parentSplit, tree.get(parent));
                        Set<Substr> parentSet = new TreeSet<>();
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
                Substr newSibling = new Substr(parent.index + lcpMax, parent.text);
                nextSuffix.index += lcpMax;
                parent.length = lcpMax;
                link(parent, nextSuffix);
                link(parent, newSibling);

            }
        }
    }

    private void initRoot(String text) {
        this.root = new Substr(-1, Integer.MIN_VALUE, text);
        Substr allText = new Substr(0, text);
        this.tree = new HashMap<>();
        this.link(this.root, allText);
    }

    private void makeTree(String text) {
        this.initRoot(text);
        this.addTextToTree(text, 1);
    }

    public void collectSuffixEdges(Substr source, Set<Substr> visited, List<String> edges) {
        visited.add(source);
        if (this.tree.get(source) != null) {
            for (Substr s : this.tree.get(source)) {
                if (!visited.contains(s)) {
                    collectSuffixEdges(s, visited, edges);
                    edges.add(s.toString());
                }
            }
        }
    }

    public String shortestNoncommonSubstring(String p, String q) {
        StringBuilder sb = new StringBuilder(p.length() + q.length() + 2);
        String text = sb.append(p).append('#').append(q).append('$').toString();
        this.initRoot(text);
        this.addTextToTree(text, 1);
        this.buildPaths();
        this.markNodes();
        return this.findShortestNoncommonSubstring();
    }

    private boolean isLeaf(Substr s) {
        return this.tree.get(s) == null;
    }

    public String findShortestNoncommonSubstring() {
        SuffixAction x = new SuffixAction() {
            Substr minSuffix;
            int minLen = Integer.MAX_VALUE;

            public void f(Substr p, Substr n) {
                if (n.type == L && n.charAt(0) != '#') {
                    int len = 0;
                    for (Substr t = paths.get(n); t != root; t = paths.get(t)) {
                        len += t.length();
                    }
                    if (isLeaf(n)) {
                        len += 1;
                    }
                    if (len < minLen) {
                        minLen = len;
                        minSuffix = n;
                    }
                }
            }

            public Substr s() {
                return minSuffix;
            }
        };

        this.treeBFS(x);
        StringBuilder sb = new StringBuilder();
        Substr n = x.s();
        if (isLeaf(n)) {
            sb.append(n.charAt(0));
            n = paths.get(n);
        }
        for (; n != root; n = paths.get(n)) {
            sb.insert(0, n.toString());
        }
        return sb.toString();
    }

    interface SuffixAction {

        public void f(Substr p, Substr n);

        public Substr s();
    }

    private void treeBFS(SuffixAction a) {
        Queue<Substr> queue = new ArrayDeque<>();
        Set<Substr> visited = new HashSet<>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            Substr next = queue.remove();
            if (tree.get(next) != null) {
                for (Substr s : tree.get(next)) {
                    if (!visited.contains(s)) {
                        queue.add(s);
                        visited.add(s);
                        a.f(next, s);
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
        Set<Substr> visited = new HashSet<>();
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
    static private int lcp(Substr s1, Substr s2) {
        int n = Integer.min(s1.length(), s2.length());
        for (int i = 0; i < n; i++) {
            if (!Objects.equals(s1.charAt(i), s2.charAt(i))) {
                return i;
            }
        }
        return n;
    }

    public static int[] suffixArray(String text) {
        int[] sfxIndices = new int[text.length()];
        Substr[] suffixes = new Substr[text.length()];
        for (int i = 0; i < text.length(); i++) {
            suffixes[i] = new Substr(i, text);
        }
        Arrays.sort(suffixes);
        for (int i = 0; i < suffixes.length; i++) {
            sfxIndices[i] = suffixes[i].index;
        }
        return sfxIndices;
    }

    /**
     * Same functionality as suffixArray() but so called because it returns a
     * boxed integer array.
     *
     * @param text
     * @return
     */
    public static Integer[] suffixArrayBoxed(String text) {
        final int len = text.length();
        Integer[] sfxIndices = new Integer[len];
        for (int i = 0; i < sfxIndices.length; i++) {
            sfxIndices[i] = i;
        }
        Arrays.sort(sfxIndices, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (Objects.equals(o1, o2)) {
                    return 0;
                }
                int n = Integer.min(len - o1, len - o2);
                for (int i = 0; i < n; i++) {
                    if (text.charAt(o1 + i) < text.charAt(o2 + i)) {
                        return -1;
                    }
                    if (text.charAt(o1 + i) > text.charAt(o2 + i)) {
                        return 1;
                    }
                }
                return o1 - o2;
            }
        });
        return sfxIndices;
    }

    public void print() {
        Queue<Substr> q1 = new ArrayDeque<>();
        Queue<Substr> q2 = new ArrayDeque<>();
        q1.add(root);
        int level = 1;
        while (!q1.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(level++).append(": ");
            while (!q1.isEmpty()) {
                Substr n = q1.remove(); 
                sb.append(n.toString()).append(' ');
                if (tree.containsKey(n)) {
                    for (Substr c : tree.get(n)) {
                        q2.add(c);
                    }
                }
            }
            System.out.println(sb.toString());
            Queue<Substr> tmp = q1;
            q1 = q2;
            q2 = tmp;
        }
    }
}
