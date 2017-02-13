package string;

import java.util.List;
import org.junit.Test;

public class SuffixTreeTest {
    
    @Test
    public void suffixProperties() {
        String text = "ATAAATG$";
        Suffix s1 = new Suffix(2, 1, text);
        Suffix s2 = new Suffix(4, 3, text);
        
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

    private void validate(String text, String[] expected) {
        SuffixTree st = new SuffixTree();
        List<String> suffixes = st.computeSuffixTreeEdges(text);
        for (String s : expected) {
            assert suffixes.contains(s);
        }
    }
    
}
