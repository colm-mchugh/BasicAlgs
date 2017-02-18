package string;

import java.util.List;
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
    public void testSuffixArray() {
        int[] gac = {3, 1, 2, 0};
        int[] gagagaga = {8,7,5,3,1,6,4,2,0};
        int[] aacgatagcggtaga = {15,14,0,1,12,6,4,2,8,13,3,7,9,10,11,5};
        
        compareArrays(gac, SuffixTree.suffixArray("GAC$"));
        compareArrays(gagagaga, SuffixTree.suffixArray("GAGAGAGA$"));
        compareArrays(aacgatagcggtaga, SuffixTree.suffixArray("AACGATAGCGGTAGA$"));
    }
    
    private void compareArrays(int[] a1, int[] a2) {
        assert a1.length == a2.length;
        for (int i = 0; i < a1.length; i++) {
            assert a1[i] == a2[i];
        }
    }
    
}
