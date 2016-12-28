/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author colm_mchugh
 */
public class HuffmanEncoderTest {
    
    /**
     * Test of makeEncoding method, of class HuffmanEncoder.
     */
    @Test
    public void testMakeEncoding() {
        System.out.println("makeEncoding");
        List<HuffmanEncoder.Node> alphabet = new ArrayList<>(5);
        alphabet.add(new HuffmanEncoder.Leaf('A', 15));
        alphabet.add(new HuffmanEncoder.Leaf('E', 5));
        alphabet.add(new HuffmanEncoder.Leaf('B', 7));
        alphabet.add(new HuffmanEncoder.Leaf('D', 6));
        alphabet.add(new HuffmanEncoder.Leaf('C', 6));
        Map<Character, BitSet> expResult = null;
        Map<Character, BitSet> result = HuffmanEncoder.makeEncoding(alphabet);
    }
    
    @Test
    public void testEncoding() {
        char[] letters = { ' ', 'a', 'e', 'f', 'h', 'i', 'm', 'n', 's', 't', 'l', 'o', 'p', 'r', 'u', 'x'};
        int[] frequencies = {7, 4, 4, 3, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1};
        List<HuffmanEncoder.Node> alphabet = new ArrayList<>(letters.length);
        for (int i = 0; i < letters.length; i++) {
            alphabet.add(new HuffmanEncoder.Leaf(letters[i], frequencies[i]));
        }
        Map<Character, BitSet> result = HuffmanEncoder.makeEncoding(alphabet);
    }
}
