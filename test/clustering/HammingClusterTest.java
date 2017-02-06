package clustering;

import org.junit.Test;
import static org.junit.Assert.*;


public class HammingClusterTest {
    
    @Test
    public void testDoit() {
        HammingCluster instance = new HammingCluster();
        int result = instance.doit("resources/clustering_big.txt");
        System.out.println("Answer is " + result);
        int expResult = 6118;
        assertEquals(expResult, result);
    }

}
