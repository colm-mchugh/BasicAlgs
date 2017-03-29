package graph;

import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class VertexCoverTest {
    
    @Test
    public void testVertexCover() {
        int[] links = {1,8, 1,3, 2,8, 2,3, 3,6, 3,7, 3,4, 4,5, 5,6, 5,7, 5,8, 7,8};
        Graph<Integer> gU = new UGraphMapImpl<>();
        Graph<Integer> gD = new DGraphImpl<>();
        
        GraphIO.populateGraph(gU, links);
        
        Set<Integer> vcU = gU.vertexCover();
        Set<Integer> vcD = gD.vertexCover();
        
        // TODO: implement vertex cover in directed and undirected graphs
        
    }
}
