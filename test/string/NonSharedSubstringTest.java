package string;

import org.junit.Test;

public class NonSharedSubstringTest {
    
    @Test
    public void testSolve() {
        String[] input = {
            "CCAAGCTGCTAGAGG", "CATGCTGGGCTGGCT",
            "ATGCGATGACCTGACTGA", "CTCAACGTATTGGCCAGA",
            "AAAAAAAAAAAAAAAAAAAA", "TTTTTTTTTTTTTTTTTTTT",
            "A", "T",
            };
        String[] expected = { "AA", "ATG", "A", "A", };
        for (int i = 0; i < expected.length; i++) {
            validate(input[i*2], input[i*2+1], expected[i]);
        }
    }
    
    @Test
    public void testShortSolve() {
        String s1 = "AXXC", s2 ="AXSC";
        String[] expected = { "XX", "XC"};
        String rv = (new NonSharedSubstring()).solve(s1, s2);
        assert rv.equals(expected[0]) || rv.equals(expected[1]);
    }
    
    @Test
    public void testShortSolve2() {
        String s1 = "ABBC", s2 ="ABC";
        String[] expected = { "BB", };
        String rv = (new NonSharedSubstring()).solve(s1, s2);
        assert rv.equals(expected[0]) ;
    }
    
    private void validate(String p, String q, String v) {
        NonSharedSubstring nss = new NonSharedSubstring();
        String rv = nss.solve(p, q);
        assert v.equals(rv);
    }
}
