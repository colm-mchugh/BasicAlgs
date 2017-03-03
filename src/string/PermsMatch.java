package string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermsMatch {

    public List<Integer> permutationsOf(String p, String t) {
        int Np = p.length();
        int Nt = t.length();
        List<Integer> offsets = new ArrayList<>();
        if (Np > Nt) {
            return offsets;
        }
        Map<Character, Integer> pFreqs = getFreqs(p, 0, p.length());
        Map<Character, Integer> tFreqs = getFreqs(t, 0, p.length());
        int i = 0;
        for (i = 0; i < Nt - Np; i++) {
            check(pFreqs, tFreqs, i, offsets);
            reduceChar(tFreqs, t.charAt(i));
            addChar(tFreqs, t.charAt(i + Np));
        }
        check(pFreqs, tFreqs, i, offsets);
        return offsets;
    }

    private static Map<Character, Integer> getFreqs(String p, int start, int end) {
        HashMap<Character, Integer> freqs = new HashMap<>();
        for (int i = start; i < end; i++) {
            addChar(freqs, p.charAt(i));
        }
        return freqs;
    }

    private static void addChar(Map<Character, Integer> freqs, Character c) {
        if (!freqs.containsKey(c)) {
            freqs.put(c, 1);
        } else {
            freqs.put(c, freqs.get(c) + 1);
        }
    }

    private static void reduceChar(Map<Character, Integer> tFreqs, char c) {
        if (tFreqs.containsKey(c)) {
            int f = tFreqs.get(c);
            if (f == 1) {
                tFreqs.remove(c);
            } else {
                tFreqs.put(c, f - 1);
            }
        }
    }

    private void check(Map<Character, Integer> pFreqs, Map<Character, Integer> tFreqs, int i, List<Integer> offsets) {
        if (pFreqs.equals(tFreqs)) {
            offsets.add(i);
        }
    }
}
