package string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SuffixArray {

    public static int[] SortCharacters(String s) {
        // freqs gives the frequency of each character in bwt. 
        // freqs also gives the set of distinct characters in bwt.
        Map<Character, Integer> freqs = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            int occurence = 1;
            char c = s.charAt(i);
            if (freqs.containsKey(c)) {
                occurence = freqs.get(c) + 1;
            }
            freqs.put(c, occurence);
        }

        char[] distinct_chars = new char[freqs.keySet().size()];
        int i = 0;
        for (char c : freqs.keySet()) {
            distinct_chars[i++] = c;
        }
        // note that distinct_chars is at least s.length()/freqs.keySet().size() times smaller than s
        Arrays.sort(distinct_chars);

        // Replace each mapping in freqs with the last index
        for (i = 1; i < distinct_chars.length; i++) {
            char c = distinct_chars[i];
            int last_idx = freqs.get(c) + freqs.get(distinct_chars[i - 1]);
            freqs.put(c, last_idx);
        }

        // Determine the ordering of each character S(i) in the sorted array:
        int[] order = new int[s.length()];
        for (i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            int next_idx = freqs.get(c) - 1;
            order[next_idx] = i;
            freqs.put(c, next_idx);
        }
        return order;
    }

    public static int[] ComputeCharClasses(String s, int[] order) {
        int[] classes = new int[s.length()];
        classes[0] = 0;
        for (int i = 1; i < classes.length; i++) {
            if (s.charAt(order[i]) == s.charAt(order[i - 1])) {
                classes[order[i]] = classes[order[i - 1]];
            } else {
                classes[order[i]] = classes[order[i - 1]] + 1;
            }
        }
        return classes;
    }

    public static int[] SortDoubled(String s, int L, int[] order, int[] classes) {
        int[] count = new int[s.length()];
        int N = s.length();
        int[] newOrder = new int[N];
        for (int i = 0; i < N; i++) {
            count[classes[i]] = count[classes[i]] + 1;
        }
        for (int i = 1; i < N; i++) {
            count[i] = count[i] + count[i - 1];
        }
        for (int i = N - 1; i >= 0; i--) {
            int start = (order[i] - L + N) % N;
            int cl = classes[start];
            count[cl] = count[cl] - 1;
            newOrder[count[cl]] = start;
        }
        return newOrder;
    }

    public static int[] UpdateClasses(int[] newOrder, int[] classes, int L) {
        int N = newOrder.length;
        int[] newClasses = new int[N];
        newClasses[newOrder[0]] = 0;
        for (int i = 1; i < N; i++) {
            int cur = newOrder[i];
            int prev = newOrder[i - 1];
            int mid = (cur + L) % N;
            int midPrev = (prev + L) % N;
            if ((classes[cur] == classes[prev]) && (classes[mid] == classes[midPrev])) {
                newClasses[cur] = newClasses[prev];
            } else {
                newClasses[cur] = newClasses[prev] + 1;
            }
        }
        return newClasses;
    }

    public static int[] Build(String s) {
        int[] order = SortCharacters(s);
        int[] classes = ComputeCharClasses(s, order);
        for (int L = 1; L < s.length(); L *= 2) {
            order = SortDoubled(s, L, order, classes);
            classes = UpdateClasses(order, classes, L);
        }
        return order;
    }

    private static int[] invertSuffixArray(int[] suffixes) {
        int N = suffixes.length;
        int[] inversions = new int[N];
        for (int i = 0; i < N; i++) {
            inversions[suffixes[i]] = i;
        }
        return inversions;
    }

    public static int[] Lcp(String s, int[] order) {
        int N = s.length();
        int[] lcps = new int[N];
        int lcp = 0;
        int[] positionInOrder = invertSuffixArray(order);
        int suffix = order[0];
        for (int i = 0; i < N; i++) {
            int orderIndex = positionInOrder[suffix];
            if (orderIndex == N - 1) {
                lcp = 0;
                suffix = (suffix + 1) % N;
                continue;
            }
            int nextSuffix = order[orderIndex + 1];
            lcp = SuffixLCP(s, suffix, nextSuffix, lcp - 1);
            lcps[orderIndex] = lcp;
            suffix = (suffix + 1) % N;
        }
        return lcps;
    }

    private static int SuffixLCP(String s, int s1, int s2, int i) {
        int N = s.length();
        int lcp = Math.max(0, i);
        while (s1 + lcp < N && s2 + lcp < N) {
            if (s.charAt(s1 + lcp) == s.charAt(s2 + lcp)) {
                lcp++;
            } else {
                break;
            }
        }
        return lcp;
    }

    public static class SuffixTreeNode {

        public SuffixTreeNode parent;
        public Map<Character, SuffixTreeNode> children;
        public int depth;
        public int start;
        public int end;

        public SuffixTreeNode(SuffixTreeNode parent, int depth, int start, int end) {
            this.parent = parent;
            this.depth = depth;
            this.start = start;
            this.end = end;
            this.children = new TreeMap<>();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(5);
            sb.append(start).append(',').append(end);
            if (!this.children.isEmpty()) {
                sb.append(" (").append(this.children.size()).append(')');
            }
            return sb.toString();
        }

    }

    private static SuffixTreeNode newLeaf(SuffixTreeNode p, String s, int sffx) {
        int N = s.length();
        SuffixTreeNode leaf = new SuffixTreeNode(p, N - sffx, sffx + p.depth, N - 1);
        p.children.put(s.charAt(leaf.start), leaf);
        return leaf;
    }

    private static SuffixTreeNode breakEdge(SuffixTreeNode parent, String s, int start, int offset) {
        char startChar = s.charAt(start);
        char midChar = s.charAt(start + offset);
        SuffixTreeNode mid = new SuffixTreeNode(parent, parent.depth + offset, start, start + offset - 1);
        SuffixTreeNode newChild = parent.children.get(startChar);
        mid.children.put(midChar, newChild);
        newChild.parent = mid;
        newChild.start += offset;
        parent.children.put(startChar, mid);
        return mid;
    }

    public static SuffixTreeNode STFromSA(String s, int[] order, int[] lcp) {
        SuffixTreeNode root = new SuffixTreeNode(null, 0, -1, -1);
        int lcpPrev = 0;
        int N = s.length();
        SuffixTreeNode curNode = root;
        for (int i = 0; i < N; i++) {
            int suffix = order[i];
            while (curNode.depth > lcpPrev) {
                curNode = curNode.parent;
            }
            if (curNode.depth == lcpPrev) {
                curNode = newLeaf(curNode, s, suffix);
            } else {
                int start = order[i - 1] + curNode.depth;
                int offset = lcpPrev - curNode.depth;
                SuffixTreeNode mid = breakEdge(curNode, s, start, offset);
                curNode = newLeaf(mid, s, suffix);
            }
            if (i < N - 1) {
                lcpPrev = lcp[i];
            }
        }
        return root;
    }

    public static class Edge {

        int node;
        int start;
        int end;

        Edge(int node, int start, int end) {
            this.node = node;
            this.start = start;
            this.end = end;
        }
    }

    public static Map<Integer, List<Edge>> SuffixTreeEdges(SuffixTreeNode root) {
        List<SuffixTreeNode> queue = new ArrayList<>();
        int nodeId = 0;
        int nextId = nodeId + 1;
        queue.add(root);
        Map<Integer, List<Edge>> tree = new HashMap<>();
        while (!queue.isEmpty()) {
            SuffixTreeNode next = queue.remove(0);
            if (!next.children.isEmpty()) {
                Set<Character> childChars = next.children.keySet();
                List<Edge> edgeChildren = new ArrayList<>(childChars.size());
                for (Character c : childChars) {
                    SuffixTreeNode childNode = next.children.get(c);
                    edgeChildren.add(new Edge(nextId++, childNode.start, childNode.end + 1));
                    queue.add(childNode);
                }
                tree.put(nodeId, edgeChildren);
            }
            nodeId++;
        }
        return tree;
    }

    public static List<Integer> match(String s, String p, int[] suffixes) {
        List<Integer> matches = new ArrayList<>();
        int N = s.length();
        int min = 0;
        int max = N;
        while (min < max) {
            int mid = (min + max) / 2;
            if (compare(s, suffixes[mid], p) > 0) {
                min = mid + 1;
            } else {
                max = mid;
            }
        }
        int start = min;
        max = N;
        while (min < max) {
            int mid = (min + max) / 2;
            if (compare(s, suffixes[mid], p) < 0) {
                max = mid;
            } else {
                min = mid + 1;
            }
        }
        int end = max;
        for (int suffix = start; suffix < end; suffix++) {
            matches.add(suffixes[suffix]);
        }
        return matches;
    }

    private static int compare(String s, int suffix, String pattern) {
        int sfxN = s.length() - suffix;
        int pN = pattern.length();
        int N = Math.min(sfxN, pN);
        for (int i = 0; i < N; i++) {
            if (pattern.charAt(i) > s.charAt(suffix + i)) {
                return 1;
            }
            if (pattern.charAt(i) < s.charAt(suffix + i)) {
                return -1;
            }
        }
        return pN - N;
    }

}
