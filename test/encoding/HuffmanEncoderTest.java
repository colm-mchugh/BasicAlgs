package encoding;

import encoding.Huffman;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class HuffmanEncoderTest {
    
    /**
     * Test of makeEncoding method, of class HuffmanEncoder.
     */
    @Test
    public void testMakeEncoding() {
        System.out.println("makeEncoding");
        List<Huffman.Node> alphabet = new ArrayList<>(5);
        alphabet.add(new Huffman.Leaf('A', 15));
        alphabet.add(new Huffman.Leaf('E', 5));
        alphabet.add(new Huffman.Leaf('B', 7));
        alphabet.add(new Huffman.Leaf('D', 6));
        alphabet.add(new Huffman.Leaf('C', 6));
        Map<Character, BitSet> expResult = null;
        Map<Character, BitSet> result = Huffman.makeEncoding(alphabet);
    }
    
    @Test
    public void testEncoding() {
        char[] letters = { ' ', 'a', 'e', 'f', 'h', 'i', 'm', 'n', 's', 't', 'l', 'o', 'p', 'r', 'u', 'x'};
        int[] frequencies = {7, 4, 4, 3, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1};
        List<Huffman.Node> alphabet = new ArrayList<>(letters.length);
        for (int i = 0; i < letters.length; i++) {
            alphabet.add(new Huffman.Leaf(letters[i], frequencies[i]));
        }
        Map<Character, BitSet> result = Huffman.makeEncoding(alphabet);
        
        for (BitSet b : result.values()) {
            for (BitSet a : result.values()) {
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
}