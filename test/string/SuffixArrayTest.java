package string;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class SuffixArrayTest {

    @Test
    public void testLongestRepeatingSubstring() {
        this.validateLongestRepeatingSubstring("ABCZLMNABCZLMNABC" + "$", "ABCZLMNABC");
        this.validateLongestRepeatingSubstring("abcpqrabpqpq$", "ab");
        this.validateLongestRepeatingSubstring("AAAAAAAAAA$", "AAAAAAAAA");
        this.validateLongestRepeatingSubstring("ABABABA$", "ABABA");
        this.validateLongestRepeatingSubstring("ABCDEFG$", "");
        this.validateLongestRepeatingSubstring("ATCGATCGA$", "ATCGA");
    }

    private void validateLongestRepeatingSubstring(String t, String expected) {
        int[] suffixes = SuffixArray.Build(t);
        int[] prefixes = SuffixArray.Lcp(t, suffixes);
        int maxPrefix = prefixes[0];
        int maxSuffix = 0;
        for (int i = 1; i < t.length(); i++) {
            if (prefixes[i] > maxPrefix) {
                maxPrefix = prefixes[i];
                maxSuffix = i;
            }
        }
        String theLongestSubstring = t.substring(suffixes[maxSuffix]).substring(0, maxPrefix);
        assert theLongestSubstring.equals(expected);
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

    @Test
    public void testSuffixTreeFromSuffixArray() {
        String t = "GTAGT$";
        int[] suffixes = SuffixArray.Build(t);
        int[] prefixes = SuffixArray.Lcp(t, suffixes);
        SuffixArray.SuffixTreeNode sfxTree = SuffixArray.STFromSA(t, suffixes, prefixes);
        Map<Integer, List<SuffixArray.Edge>> edges = SuffixArray.SuffixTreeEdges(sfxTree);
        assert edges.size() == 3;
        List<SuffixArray.Edge> rootEdges = edges.get(0);
        assert rootEdges.size() == 4;
        SuffixArray.Edge e1 = rootEdges.get(1);
        assert t.substring(e1.start, e1.end).equals("AGT$");
        
        SuffixArray.Edge e2 = rootEdges.get(2);
        assert t.substring(e2.start, e2.end).equals("GT");
        assert e2.node == 3;
        
        List<SuffixArray.Edge> e2Children = edges.get(e2.node);
        assert e2Children.size() == 2;
        
        SuffixArray.Edge e21 = e2Children.get(1);
        assert t.substring(e21.start, e21.end).equals("AGT$");
        
        SuffixArray.Edge e3 = rootEdges.get(3);
        assert t.substring(e3.start, e3.end).equals("T");
    }
    
    @Test
    public void testSuffixTreeFromSuffixArray2() {
        String t = "ACACAA$"; // expected output: 6 7, 0 1, 6 7, 5 7, 1 3, 5 7, 3 7, 1 3, 5 7, 3 7
    }
    
    @Test
    public void testSuffixTreeFromSuffixArray1() {
        String t = "AAA$";
        int[] suffixes = SuffixArray.Build(t);
        int[] prefixes = SuffixArray.Lcp(t, suffixes);
        SuffixArray.SuffixTreeNode sfxTree = SuffixArray.STFromSA(t, suffixes, prefixes);
        Map<Integer, List<SuffixArray.Edge>> edges = SuffixArray.SuffixTreeEdges(sfxTree);
        assert edges.size() == 3;
        List<SuffixArray.Edge> rootEdges = edges.get(0);
        assert rootEdges.size() == 2;
        
        SuffixArray.Edge e1 = rootEdges.get(1);
        assert t.substring(e1.start, e1.end).equals("A");
        assert e1.start == 0 && e1.end == 1;
        
        List<SuffixArray.Edge> e1Children = edges.get(e1.node);
        assert e1Children.size() == 2;
        
        SuffixArray.Edge e12 = e1Children.get(1);
        assert t.substring(e12.start, e12.end).equals("A");
        assert e1.start == 1 && e1.end == 2;     
    }
    
    
    @Test
    public void testSuffixArrayMatching() {
        int[] AAA = {0,1,2};
        validateMatch(AAA, SuffixArray.match("AAA", "A", SuffixArray.Build("AAA")));
        
        int[] empty = {};
        validateMatch(empty, SuffixArray.match("ATA", "G", SuffixArray.Build("ATA")));
        
        int[] ATA = {0, 2, 4};
        validateMatch(ATA, SuffixArray.match("ATATATA", "ATA", SuffixArray.Build("ATATATA")));
        
        int[] TATA = {1, 3};
        validateMatch(TATA, SuffixArray.match("ATATATA", "TATA", SuffixArray.Build("ATATATA")));
        
        int[] TATAT = {1};
        validateMatch(TATAT, SuffixArray.match("ATATATA", "TATAT", SuffixArray.Build("ATATATA")));
        
    }
    
    private void validateMatch(int[] expected, List<Integer> matches) {
        assert expected.length == matches.size();
        for (int m : expected) {
            assert matches.contains(m);
        }
    }

}
