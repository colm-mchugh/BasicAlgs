package graph;

import org.junit.Test;

public class KargerMinCutTest {

    /**
     * Test of minCut method, of class KargerMinCut.
     */
    @Test
    public void testMinCut() {
        String file = "resources/kargerMinCut.txt";
        KargerMinCut kmc = new KargerMinCut(file);
        kmc.print();
        
        int minCut = kmc.minCut();
        System.out.println("The minCut is " +  minCut);
        assert minCut == 17;
    }
    
}
