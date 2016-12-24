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
     * Test of readAlphabet method, of class HuffmanEncoder.
     */
    @Test
    public void testReadAlphabet() throws Exception {
        System.out.println("readAlphabet");
        String file = "";
        List<HuffmanEncoder.Node> expResult = null;
        List<HuffmanEncoder.Node> result = HuffmanEncoder.readAlphabet(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

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
    
}
