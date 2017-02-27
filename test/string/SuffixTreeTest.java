package string;

import java.util.List;
import java.util.Objects;
import org.junit.Test;

public class SuffixTreeTest {
    
    @Test
    public void suffixProperties() {
        String text = "ATAAATG$";
        SuffixTree.Suffix s1 = new SuffixTree.Suffix(2, 1, text);
        SuffixTree.Suffix s2 = new SuffixTree.Suffix(4, 3, text);
        
        assert s1.toString().equals("A");
        assert s2.toString().equals("ATG");
        
    }
    
    @Test
    public void testTreeBuild1() {
        String[] texts = { "ATAAATG$", "A$", "ACA$",  };
        String[][] expecteds = { 
            { "AAATG$", "G$", "T", "ATG$", "TG$", "A", "A", "AAATG$", "G$", "T", "G$", "$"}, 
            { "A$", "$"}, {"$", "A", "$", "CA$", "CA$"}, 
        };
        for (int i = 0; i < texts.length; i++) {
            validate(texts[i], expecteds[i]);
        }
    }

    @Test
    public void testMississippi() {
        String text = "mississippi$";
        String[] expected = { "mississippi$",
            "i", "ssi", "ssippi$", "ppi$", "ppi$",
            "s", "si", "ssippi$", "ppi$", "i",
            "ssippi$", "ppi$", "p", "pi$", "i$", "$"};
        validate(text, expected);
    }
    
    @Test
    public void testPanamaBananas() {
        String text = "panamabananas$";
        String[] expected = { "panamabananas$",
            "s$", "na", "s$", "nas$", "mabananas$", "mabananas$", "bananas$",
            "a", "bananas$", "mabananas$", "na", "s$", "mabananas$", "nas$", "s$"};
        validate(text, expected);
    }
    
    private void validate(String text, String[] expected) {
        SuffixTree st = new SuffixTree();
        List<String> suffixes = st.computeSuffixTreeEdges(text);
        for (String s : expected) {
            assert suffixes.contains(s);
        }
    }
    
    @Test
    public void testSuffixArrayBoxed() {
        Integer[] gac = {3, 1, 2, 0};
        Integer[] gagagaga = {8,7,5,3,1,6,4,2,0};
        Integer[] aacgatagcggtaga = {15,14,0,1,12,6,4,2,8,13,3,7,9,10,11,5};
        long now = System.nanoTime();
        compareBoxedIntArrays(gac, SuffixTree.suffixArrayBoxed("GAC$"));
        compareBoxedIntArrays(gagagaga, SuffixTree.suffixArrayBoxed("GAGAGAGA$"));
        compareBoxedIntArrays(aacgatagcggtaga, SuffixTree.suffixArrayBoxed("AACGATAGCGGTAGA$"));
        long timeTaken = System.nanoTime() - now;
        System.out.println("Boxed microsecs: " + timeTaken /1000);
    }
    
    @Test
    public void testSuffixArrayRaw() {
        int[] gac = {3, 1, 2, 0};
        int[] gagagaga = {8,7,5,3,1,6,4,2,0};
        int[] aacgatagcggtaga = {15,14,0,1,12,6,4,2,8,13,3,7,9,10,11,5};
        long now = System.nanoTime();
        compareRawIntArrays(gac, SuffixTree.suffixArray("GAC$"));
        compareRawIntArrays(gagagaga, SuffixTree.suffixArray("GAGAGAGA$"));
        compareRawIntArrays(aacgatagcggtaga, SuffixTree.suffixArray("AACGATAGCGGTAGA$"));
        long timeTaken = System.nanoTime() - now;
        System.out.println("Raw microsecs: " + timeTaken /1000);
    }
    
    private void compareBoxedIntArrays(Integer[] a1, Integer[] a2) {
        assert a1.length == a2.length;
        for (int i = 0; i < a1.length; i++) {
            assert Objects.equals(a1[i], a2[i]);
        }
    }
    
    private void compareRawIntArrays(int[] a1, int[] a2) {
        assert a1.length == a2.length;
        for (int i = 0; i < a1.length; i++) {
            assert a1[i] == a2[i];
        }
    }
    
}
