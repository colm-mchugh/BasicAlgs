package string;

import java.util.ArrayList;
import java.util.List;

/**
 * KMP (Knuth-Morris-Pratt) algorithm for finding the occurrences of a pattern in
 * a text. 
 * 
 */
public class KMP {

    private final static int[] ZERO_OCCURENCES = {};
    
    /**
     * The border of a string S is a prefix of S that is equal to a suffix of S,
     * And is also not equal to S.
     * 
     * @param s
     * @return 
     */
    public static int[] bordersOf(String s) {
        int border = 0;
        int[] borders = new int[s.length()];
        borders[0] = 0;
        for (int i = 1; i < borders.length; i++) {
            while ((border > 0) && s.charAt(border) != s.charAt(i)) {
                border = borders[border - 1];
            }
            if (s.charAt(border) == s.charAt(i)) {
                border = border + 1;
            } else {
                border = 0;
            }
            borders[i] = border;
        }
        return borders;
    }
    
    public static int[] occurences(String s, String p) {
        if (p.length() > s.length()) {
            return ZERO_OCCURENCES;
        }
        StringBuilder sb = new StringBuilder(s.length() + p.length() + 1);
        String text = sb.append(p).append('$').append(s).toString();
        int[] borders = bordersOf(text);
        List<Integer> offsets = new ArrayList<>();
        int len = p.length();
        int adjustment = len *2;
        for (int i = len + 1; i < borders.length; i++) {
            if (borders[i] == len) {
                offsets.add(i - adjustment);
            }
        }
        if (offsets.isEmpty()) {
            return ZERO_OCCURENCES;
        } else {
            int[] szs = new int[offsets.size()];
            for (int i = 0; i < szs.length; i++) {
                szs[i] = offsets.get(i);
            }
            return szs;
        }
    }
    
}
