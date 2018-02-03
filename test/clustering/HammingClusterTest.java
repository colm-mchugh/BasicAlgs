package clustering;

import java.util.BitSet;
import org.junit.Test;
import static org.junit.Assert.*;


public class HammingClusterTest {
    
    @Test
    public void testDoit() {
        String file = "resources/clustering_big.txt";
        int expected = 6118;
        boolean doPathCompression = true;
        
        // test for different union find implementations
        this.verify(new LazyUnion<>(!doPathCompression), file, expected);
        this.verify(new LazyUnion<>(doPathCompression), file, expected);
        this.verify(new QuickFind<>(), file, expected);
    }

    /**
     * Test HammingCluster with specific union find instance
     */
    private void verify(UnionFind<BitSet> uf, String file, int expected) {
        HammingCluster instance = new HammingCluster(uf);
        int result = instance.doit(file);
        System.out.println("Result is " + result);
        assertEquals(expected, result);
    }
}
