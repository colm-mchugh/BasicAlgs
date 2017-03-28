package string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrefixList {

    /**
     * Given a list of words, return the list of prefixes such that each prefix
     * can uniquely identify its matching word.
     *
     * Given words[i], prefixes[i] is the minimum prefix of words[i] such that
     * no other prefix[j] (j != i) has this prefix.
     *
     * No word should be a prefix of any other (otherwise, can't avoid returning
     * two identical prefixes).
     *
     * @param a
     * @return
     */
    public List<String> prefix(List<String> a) {
        int N = a.size();
        Map<String, Integer> offsets = new HashMap<>(N);
        String[] prefixes = new String[N];
        for (int i = 0; i < N; i++) {
            offsets.put(a.get(i), i);
        }
        Collections.sort(a);
        for (int i = 0; i < N; i++) {
            int lcp1 = Integer.MIN_VALUE;
            int lcp2 = Integer.MIN_VALUE;
            String ai = a.get(i);
            if (i > 0) {
                lcp1 = lcp(a.get(i - 1), ai);
            }
            if (i < N - 1) {
                lcp2 = lcp(ai, a.get(i + 1));
            }
            int lcp = Integer.max(lcp1, lcp2) + 1;
            prefixes[offsets.get(ai)] = ai.substring(0, lcp);
        }
        ArrayList<String> rv = new ArrayList<>(N);
        for (String p : prefixes) {
            rv.add(p);
        }
        return rv;
    }

    private int lcp(String s, String p) {
        int N = Integer.min(s.length(), p.length());
        int lcp = 0;
        while (lcp < N && s.charAt(lcp) == p.charAt(lcp)) {
            lcp++;
        }
        return lcp;
    }

    /**
     * Given a string s and an interleaving factor N, return the string that
     * is the concatenation of all N interleavings of s.
     * 
     * @param s
     * @param N
     * @return 
     */
    public String interleaveN(String s, int N) {
        if (N == 1) {
            return s;
        }
        StringBuilder[] rows = new StringBuilder[N];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new StringBuilder(s.length() / N);
        }
        for (int i = 0; i < s.length(); i += 2 * N - 2) {
            int row = 0;
            for (int j = i; j < s.length() && j < i + N; j++) {
                rows[row++].append(s.charAt(j));
            }
            row = N - 2;
            for (int j = i + N; j < s.length() && j < i + 2 * N - 2; j++) {
                rows[row--].append(s.charAt(j));
            }
        }
        StringBuilder result = new StringBuilder(N);
        for (int i = 0; i < rows.length; i++) {
            result.append(rows[i].toString());
        }
        return result.toString();
    }
}
