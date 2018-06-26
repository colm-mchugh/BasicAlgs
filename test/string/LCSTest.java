package string;

import org.junit.Test;

public class LCSTest {
    
    static String[][] cases = {
        {"CATCGA", "GTACCGTCA"},
        {"ABAZDC", "BACBAD"},
        {"ABCDGH", "AEDFHR"},
        {"AGGTAB", "GXTXAYB"},
        {"XMJYAUZ", "MZJAWXU"},
        {"ZXNCBZNBC", "IWUYRUIWEYRWIUERYFKJDF"}
    };
    
    static String[] results = {
        "CTCA", "ABAD", "ADH", "GTAB", "MJAU", ""
    };
    
    @Test
    public void testCases() {
        for (int i = 0; i < cases.length; i++) {
            String X = cases[i][0];
            String Y = cases[i][1];
            String exp = results[i];
            String lcs = LCS.Of(X, Y);      
            assert (lcs == null ? false : lcs.equals(exp));
        }     
    }
    
}
