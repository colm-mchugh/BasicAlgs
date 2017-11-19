package dp;

import java.util.BitSet;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class SetGenTest {
    
    /**
     * Test of dfsSetGen method, of class SetGen.
     */
    @Test
    public void testDfsSetGen() {
        System.out.println("dfsSetGen");
        int N = 3;
        List<BitSet> sets = SetGen.dfsSetGen(N);
        for (BitSet set : sets) {
            System.out.println(set.toString());
        }
    }

    /**
     * Test of bfsSetGen method, of class SetGen.
     */
    @Test
    public void testBfsSetGen() {
        System.out.println("bfsSetGen");
        int N = 3;
        List<BitSet> sets = SetGen.bfsSetGen(N);
        for (BitSet set : sets) {
            System.out.println(set.toString());
        }
    }
    
    @Test
    public void testLdsSetGen() {
        System.out.println("bfsSetGen");
        int N = 8;
        List<BitSet> sets = SetGen.ldsSetGen(N);
        for (BitSet set : sets) {
            System.out.println(set.toString());
        }
    }
    
}
