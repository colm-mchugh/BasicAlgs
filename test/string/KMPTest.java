package string;

import org.junit.Test;

public class KMPTest {
    
    @Test
    public void testBordersOf() {
        int[] expected = {0,0,1,2,3,4,0,1,1,2};
        validateBordersOf("abababcaab", expected);
    }
    
    @Test
    public void testBordersOf2() {
        int[] expected = {0,0,1,0,1,2,3,4,5,6,7,2,3};
        validateBordersOf("ACATACATACACA", expected);
    }
    
    private void validateBordersOf(String p, int[] expected) {
        int[] borders = KMP.bordersOf(p);
        assert borders.length == expected.length;
        for (int i = 0; i < expected.length; i++) {
            assert borders[i] == expected[i];
        }
    }
    @Test
    public void testPatternLongerThanText() {
        int[] expected = {};
        validate("GT", "TACG", expected);
    }
    
    @Test
    public void testPatternOverlapsInText() {
        int[] expected = {0, 2};
        validate("ATATA", "ATA", expected);
    }
    
    @Test
    public void testPatternOverlapsInText2() {
        int[] expected = {1, 3, 9};
        validate("GATATATGCATATACTT", "ATAT", expected);
    }
    
    private void validate(String s, String p, int[] expected) {
        int[] result = KMP.occurences(s, p);
        assert expected.length == result.length;
        for (int i = 0; i < expected.length; i++) {
            assert expected[i] == result[i];
        }
    }
    
}
