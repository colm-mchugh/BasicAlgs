package string;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import sort.QuickSorter;

/**
 * BWT (Burrows Wheeler Transform)
 *
 * transform - Apply Burrows Wheeler Transform to a string reverse - Reverse a
 * Burrows Wheeler Transformed string match - Return number of times pattern
 * occurs in given string
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

    /**
     * Determine the index of the i'th occurrence of c in a. Note there may be 
     * more than one occurrence of c in a; parameter i specifies precisely which
     * occurrence. Binary search is used to find the first occurrence of c in a,
     * then i is applied as an offset.
     * 
     * @param c
     * @param a
     * @param i
     * @return 
     */
    private static int getIndexOf(char c, char[] a, int i) {
        if (c == '$') {
            return 0;
        }
        int lo = 0;
        int hi = a.length - 1;
        int m = (hi) / 2;
        while (a[m] != c) {
            if (a[m] > c) {
                hi = m - 1;
            } else {
                lo = m + 1;
            }
            m = lo + (hi - lo) / 2;
        }
        while (a[m] == c && m >= 0) {
            m--;
        }
        return m + i;
    }

    private static final float COUNTED_SORT_RATIO = 0.1f;

    /**
     * Sort the characters of bwt into the array s_bwt. 
     * Return offset of first occurrence of the distinct characters of bwt.
     * 
     * @param bwt
     * @param s_bwt
     * @return 
     */
    private static Map<Character, Integer> CountedSort(String bwt, char[] s_bwt) {
        Map<Character, Integer> offsets = new HashMap<>(); // sorted bwt character offsets
        
        // freqs gives the frequency of each character in bwt. 
        // freqs also gives the set of distinct characters in bwt.
        Map<Character, Integer> freqs = new HashMap<>(); 
        for (int i = 0; i < bwt.length(); i++) {
            int occurence = 1;
            char c = bwt.charAt(i);
            if (freqs.containsKey(c)) {
                occurence = freqs.get(c) + 1;
            }
            freqs.put(c, occurence);
        }

        // Apply count sorting if the ratio of distinct characters to bwt length
        // is less than the counted sort ratio.
        if (freqs.keySet().size() / (float) bwt.length() < COUNTED_SORT_RATIO) {
            char[] distinct_chars = new char[freqs.keySet().size()];
            int i = 0;
            for (char c : freqs.keySet()) {
                distinct_chars[i++] = c;
            }
            // note that distinct_chars is at least 1/COUNTED_SORT_RATIO times smaller than bwt
            Arrays.sort(distinct_chars); 
            int offset = 0;
            // use frequencies to determine first occurence of each character in
            // the sorted array. 
            for (i = 0; i < distinct_chars.length; i++) {
                offsets.put(distinct_chars[i], offset);
                offset += freqs.get(distinct_chars[i]);
            }
            offset = 0;
            // Populate the sorted array using character offsets. The sorted
            // distinct_chars provides the character ordering.
            for (i = 0; i < distinct_chars.length; i++) {
                int numLetters = freqs.get(distinct_chars[i]);
                for (int j = offset; j < offset + numLetters; j++) {
                    s_bwt[j] = distinct_chars[i];
                }
                offset += numLetters;
            }
        } else {
            // The ratio of distinct characters to bwt lenght does not merit count sorting.
            // Use Arrays library sort. note offsets will be empty in this case.
            Arrays.sort(s_bwt);
        }
        return offsets; 
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
        Map<Character, Integer> offsets = CountedSort(bwt, s_bwt);
        // For keeping track of character occurences 
        Map<Character, Integer> occurences = new HashMap<>();
        for (int i = 0; i < bwt.length(); i++) {
            int occurence = 1;
            char c = bwt.charAt(i);
            if (occurences.containsKey(c)) {
                occurence = occurences.get(c) + 1;
            }
            if (!offsets.isEmpty()) {
                FirstLast[i] = offsets.get(c) + occurence - 1;
            } else {
                FirstLast[i] = getIndexOf(c, s_bwt, occurence);
            }
            occurences.put(c, occurence);
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
     * precondition: bwt is a BWT transform of the original string.
     *
     * @param bwt
     * @param pattern
     * @return
     */
    public static int match(String bwt, String pattern) {
        char[] s_bwt = bwt.toCharArray();
        Map<Character, Integer> offsets = CountedSort(bwt, s_bwt);       
        if (offsets.isEmpty()) { 
            for (int i = 0; i < s_bwt.length; i++) {
                if (!offsets.containsKey(s_bwt[i])) {
                    offsets.put(s_bwt[i], i);
                }
            }
        }
        // count gives, for each character and each index in bwt, the number of times
        // it appears in bwt up to that index.
        Map<Character, int[]> count = new HashMap<>();
        for (Character c : offsets.keySet()) {
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
                    top = offsets.get(c) + cOccurences[top];
                    bottom = offsets.get(c) + cOccurences[bottom + 1] - 1;
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
