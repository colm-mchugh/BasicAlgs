package graph;

import java.util.Set;
import org.junit.Test;

public class VertexCoverTest {
    
    @Test
    public void testVertexCover() {
        int[] links = {1,8, 1,3, 2,8, 2,3, 3,6, 3,7, 3,4, 4,5, 5,6, 5,7, 5,8, 7,8};
        Graph<Integer> gU = new UGraphMapImpl<>();
        Graph<Integer> gD = new DGraphImpl<>();
        
        GraphIO.populateGraph(gU, links);
        
        Set<Integer> vcU = gU.vertexCover();
        Set<Integer> vcD = gD.vertexCover();     
    }
    
    private void testGraph(int[] links, int expectedSz, int[] expectedCntnts) {
        Graph<Integer> gU = new UGraphMapImpl<>();
        Graph<Integer> gD = new DGraphImpl<>();
        
        GraphIO.populateGraph(gU, links);
        GraphIO.populateGraph(gD, links);
        
        Set<Integer> vcU = gU.vertexCover();
        Set<Integer> vcD = gD.vertexCover();
        
        assert vcU.size() <= expectedSz * 2;
        assert vcD.size() <= expectedSz * 2;
         
        for (int v : expectedCntnts) {
            assert vcU.contains(v);
            assert vcD.contains(v); 
        }
    }
    
    @Test
    public void testVertexCoverSimpleStar() {
        int[] links = {0,3, 1,3, 2,3, 4,3, 5,3, 6,3 };
        int[] expcted = { 3 };
        testGraph(links, 1, expcted);
    }
    
    @Test
    public void testVertexCoverSimpleCLRS() {
        int[] links = {0,1, 1,2, 2,3, 3,4, 4,5, 6,3, 2,4, 3,5 };
        int[] expcted = { 1, 3 };
        testGraph(links, 3, expcted);
    }
}
