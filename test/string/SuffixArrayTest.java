package string;

import org.junit.Test;
import static org.junit.Assert.*;

public class SuffixArrayTest {

    @Test
    public void testLongestRepeatingSubstring() {
        String t = "ABCZLMNABCZLMNABC" + "$";
        int[] suffixes = SuffixArray.Build(t);
        for (int i = 0; i < suffixes.length; i++) {
            System.out.println( "" + i + ": " + t.substring(i));
        }
        int[] prefixes = SuffixArray.Lcp(t, suffixes);
        int maxPrefix = prefixes[0];
        int maxSuffix = 0;
        for (int i = 1; i < t.length(); i++) {
            if (prefixes[i] > maxPrefix) {
                maxPrefix = prefixes[i];
                maxSuffix = i;
            }
        }
        String theLongestSubstring = t.substring(suffixes[maxSuffix], maxPrefix);
        assert theLongestSubstring.equals("ABCZLMN");
    }

    @Test
    public void testSortCharacters() {
        String s = "banana$";
        int[] expectedOrder = {6, 1, 3, 5, 0, 2, 4};
        int[] order = SuffixArray.SortCharacters(s);
        for (int i = 0; i < expectedOrder.length; i++) {
            assert expectedOrder[i] == order[i];
        }
        int[] expectedClasses = {2, 1, 3, 1, 3, 1, 0};
        int[] classes = SuffixArray.ComputeCharClasses(s, order);
        for (int i = 0; i < expectedClasses.length; i++) {
            assert expectedClasses[i] == classes[i];
        }
    }

    @Test
    public void testSuffixArrayBuild() {
        int[] gac = {3, 1, 2, 0};
        int[] gagagaga = {8, 7, 5, 3, 1, 6, 4, 2, 0};
        int[] aacgatagcggtaga = {15, 14, 0, 1, 12, 6, 4, 2, 8, 13, 3, 7, 9, 10, 11, 5};
        long now = System.nanoTime();
        compareRawIntArrays(gac, SuffixArray.Build("GAC$"));
        compareRawIntArrays(gagagaga, SuffixArray.Build("GAGAGAGA$"));
        compareRawIntArrays(aacgatagcggtaga, SuffixArray.Build("AACGATAGCGGTAGA$"));
        long timeTaken = System.nanoTime() - now;
        System.out.println("Raw microsecs: " + timeTaken / 1000);
    }

    private void compareRawIntArrays(int[] a1, int[] a2) {
        assert a1.length == a2.length;
        for (int i = 0; i < a1.length; i++) {
            assert a1[i] == a2[i];
        }
    }

}
