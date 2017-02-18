package string;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import sort.QuickSorter;

/**
 * BWT (Burrows Wheeler Transform)
 *
 * transform - Apply Burrows Wheeler Transform to a string 
 * reverse - Reverse a Burrows Wheeler Transformed string
 * match - Return number of times pattern occurs in given string
 *
 */
public class BWT {

    public static String transform(String p) {
        Rotation[] rotations = new Rotation[p.length()];

        for (int i = 0; i < p.length(); i++) {
            rotations[i] = new Rotation(p, i);
        }
        QuickSorter sorter = new QuickSorter();
        sorter.sort(rotations);

        StringBuilder bwText = new StringBuilder(p.length());
        for (int i = 0; i < rotations.length; i++) {
            bwText.append(rotations[i].charAt(p.length() - 1));
        }

        return bwText.toString();
    }

    private static int getIndexOf(char c, char[] a, int offset) {
        if (c == '$') {
            return 0;
        }
        int lo = 0;
        int hi = a.length - 1;
        int m = (hi)/2;
        while (a[m] != c) {
            if (a[m] > c) {
                hi = m - 1;
            } else {
                lo = m + 1;
            }
            m = lo + (hi - lo)/2;
        }
        while (a[m] == c && m >= 0) {
            m--;
        }
        return m + offset;
    }

    /**
     * Reverse a Burrows-wheeler transformed String
     *
     * @param bwt
     * @return
     */
    public static String reverse(String bwt) {
        char[] s_bwt = bwt.toCharArray();
        int[] FirstLast = new int[bwt.length()];
        Arrays.sort(s_bwt);

        // For keeping track of character occurences 
        Map<Character, Integer> freqs = new HashMap<>();
        for (int i = 0; i < bwt.length(); i++) {
            int occurence = 1;
            char c = bwt.charAt(i);
            if (freqs.containsKey(c)) {
                occurence = freqs.get(c) + 1;
            }
            FirstLast[i] = getIndexOf(c, s_bwt, occurence);
            freqs.put(c, occurence);
        }

        // Recreate the original string from the last character 
        char[] origChars = new char[bwt.length()];
        int idx = 0;
        for (int i = bwt.length() - 1; i >= 0; i--) {
            origChars[i] = s_bwt[idx];
            idx = FirstLast[idx];
        }

        return new String(origChars);
    }

    /**
     * match: return the number of times pattern occurs in bwt.
     * bwt is a BWT transform of the original string.
     * 
     * @param bwt
     * @param pattern
     * @return 
     */
    public static int match(String bwt, String pattern) {
        char[] s_bwt = bwt.toCharArray();
        Arrays.sort(s_bwt);

        //Construct firstOccurence: 
        //Gives, for each character in bwt, the index of its first occurence in s_bwt
        Map<Character, Integer> firstOccurence = new HashMap<>();
        for (int i = 0; i < s_bwt.length; i++) {
            if (!firstOccurence.containsKey(s_bwt[i])) {
                firstOccurence.put(s_bwt[i], i);
            }
        }

        // Construct count:
        // Gives, for each character and each index in bwt, the number of times
        // it appears in bwt up to that index.
        Map<Character, int[]> count = new HashMap<>();
        for (Character c : firstOccurence.keySet()) {
            int[] car = new int[bwt.length() + 1];
            int occurences = 0;
            car[0] = 0;
            for (int i = 1; i < car.length; i++) {
                if (bwt.charAt(i - 1) == c) {
                    occurences++;
                }
                car[i] = occurences;
            }
            count.put(c, car);
        }

        int top = 0, bottom = bwt.length() - 1;
        int pLen = pattern.length();
        while (top <= bottom) {
            if (pLen > 0) { // pattern is matched from the end, one character at a time
                char c = pattern.charAt(pLen - 1);
                pLen--;
                boolean cOccurence = false;
                for (int i = top; i <= bottom && !cOccurence; i++) {
                    cOccurence = bwt.charAt(i) == c;
                }
                if (cOccurence) {
                    int[] cOccurences = count.get(c);
                    top = firstOccurence.get(c) + cOccurences[top];
                    bottom = firstOccurence.get(c) + cOccurences[bottom + 1] - 1;
                } else {
                    return 0;
                }
            } else {
                return bottom - top + 1;
            }
        }
        return 0;
    }

}
