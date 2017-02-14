package string;

import org.junit.Test;

public class NonSharedSubstringTest {
            
    @Test
    public void testShortSolve1() {
        this.validate("A", "T", "A");
    }
    
    @Test
    public void testLongSolve1() {
        this.validate("AAAAAAAAAAAAAAAAAAAA", "TTTTTTTTTTTTTTTTTTTT", "A");
    }
    
    @Test
    public void testLongSolve2() {
        this.validate("CCAAGCTGCTAGAGG", "CATGCTGGGCTGGCT", "AA");
    }
    
    @Test
    public void testLongSolve3() {
        this.validate("ATGCGATGACCTGACTGA", "CTCAACGTATTGGCCAGA", "ATG");
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
        this.validate("ABBC", "ABC", "BB");
    }
    
    private void validate(String p, String q, String v) {
        NonSharedSubstring nss = new NonSharedSubstring();
        String rv = nss.solve(p, q);
        assert v.equals(rv);
    }
}
