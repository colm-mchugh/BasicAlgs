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
        boolean trackLeaderSets = true;
        // test for different union find implementations
        this.verify(new QuickFind<>(!trackLeaderSets), file, expected);
        this.verify(new QuickFind<>(trackLeaderSets), file, expected);
        this.verify(new LazyUnion<>(!doPathCompression), file, expected);
        this.verify(new LazyUnion<>(doPathCompression), file, expected);
    }

    /**
     * Test HammingCluster with specific union find instance
     */
    private void verify(UnionFind<BitSet> uf, String file, int expected) {
        long now = System.currentTimeMillis();        
        HammingCluster instance = new HammingCluster(uf);
        int result = instance.doit(file);
        assertEquals(expected, result);
        long timeTaken = System.currentTimeMillis() - now;
        System.out.println("Time taken: " + timeTaken);
    }
}
