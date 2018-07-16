package encoding;

import java.util.List;
import org.junit.Test;

public class LZWTest {
    
    @Test
    public void testEncode1() {
        String[] inputs = { "TATAGATCTTAATATA", "ABRACADABRABRABRA", "ABABABA" };
        char[][] alphabets = { 
                { 'A', 'C', 'G', 'T' }, 
                { 'A', 'B', 'C', 'D', 'R' },
                { 'A', 'B' }
        };
        int[][] expectedEncodings = { 
                { 4, 1, 5, 3, 6, 2, 4, 5, 6, 13 },
                { 1, 2, 5, 1, 3, 1, 4, 6, 8, 7, 13, 1 },
                { 1, 2, 3, 5 }
        };    
        
        for (int i = 0; i < inputs.length; i++) {
            LZWencoder encoder = new LZWencoder(alphabets[i]);
            List<Integer> result = encoder.encode(inputs[i]);
            assert result.size()== expectedEncodings[i].length;
            for (int j = 0; j < expectedEncodings[i].length; j++) {
                assert expectedEncodings[i][j] == result.get(j);
            }
        }
    }
    
}
