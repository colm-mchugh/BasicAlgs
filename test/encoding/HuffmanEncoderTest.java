package encoding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;
import java.util.Map;
import org.junit.Test;

public class HuffmanEncoderTest {

    /**
     * Test of makeEncoding method, of class HuffmanEncoder.
     */
    @Test
    public void testMakeEncoding() {
        System.out.println("makeEncoding");
        Huffman<Character> encoder = new Huffman<>();
        encoder.addSymbol('A', 15);
        encoder.addSymbol('E', 5);
        encoder.addSymbol('B', 7);
        encoder.addSymbol('D', 6);
        encoder.addSymbol('C', 6);
        Map<Character, BitSet> expResult = null;
        Map<Character, BitSet> resultbS = encoder.getBitSetEncoding();
        
        for (BitSet b : resultbS.values()) {
            for (BitSet a : resultbS.values()) {
                assert !isPrefix(b, a);
            }
        }
    }

    @Test
    public void testEncoding() {
        char[] letters = {' ', 'a', 'e', 'f', 'h', 'i', 'm', 'n', 's', 't', 'l', 'o', 'p', 'r', 'u', 'x'};
        int[] frequencies = {7, 4, 4, 3, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1};
        Huffman<Character> encoder = new Huffman<>();
        for (int i = 0; i < letters.length; i++) {
            encoder.addSymbol(letters[i], frequencies[i]);
        }
        Map<Character, BitSet> result = encoder.getBitSetEncoding();

        for (BitSet b : result.values()) {
            for (BitSet a : result.values()) {
                assert !isPrefix(b, a);
            }
        }
    }

    @Test
    public void bigTest() {
        Huffman<String> foo = readVertices("resources/huffman.txt");

        Map<String, String> result = foo.getStringEncoding();

        Map.Entry<String, String> min = null;
        Map.Entry<String, String> max = null;

        for (Map.Entry<String, String> pish : result.entrySet()) {
            if (min == null || pish.getValue().length()< min.getValue().length()) {
                min = pish;
            }
            if (max == null || pish.getValue().length() > max.getValue().length()) {
                max = pish;
            }
        }

        assert min.getValue().length() == 9;
        assert max.getValue().length() == 19;
        
        Map<String, BitSet> resultbS = foo.getBitSetEncoding();
        for (BitSet b : resultbS.values()) {
            for (BitSet a : resultbS.values()) {
                assert !isPrefix(b, a);
            }
        }
    }

    private boolean isPrefix(BitSet b1, BitSet b2) {
        if (b2.size() > b1.size()) {
            for (int i = 0; i < b1.size(); i++) {
                if (b1.get(i) != b2.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private Huffman<String> readVertices(String file) {
        FileReader fr;
        Huffman<String> encoder = new Huffman<>();
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            for (int j = 0; (line = br.readLine()) != null; j++) {
                int weight = Integer.parseInt(line);
                encoder.addSymbol("sym" + j, weight);
            }
        } catch (IOException | NumberFormatException e) {
        }
        return encoder;
    }
}
