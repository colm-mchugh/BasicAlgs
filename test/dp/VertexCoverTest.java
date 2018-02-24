package dp;

import graph.GraphIO;
import graph.WeightedGraph;
import graph.WeightedGraphUndirected;
import java.util.Set;
import junit.framework.TestCase;
import org.junit.Test;

public class VertexCoverTest extends TestCase {
    
    @Test
    public void testStarGraph() {
        int[] links = {0,3,3, 1,3,2, 2,3,2, 4,3,1, 5,3,1, 6,3,4, 7,3,3, 8,3,2, 9,3,2 };
        WeightedGraph<Integer> G = new WeightedGraphUndirected<>();
        GraphIO.populateWeightedGraph(G, links);
        VertexCover<Integer> vcer = new VertexCover<>();
        Set<Integer> vc = vcer.cover(G, 1);
        assert vc.size() == 1;
        assert vc.contains(3);
    }
    
    @Test
    public void testSimpleCLRS() {
        int[] links = {0,1,1, 1,2,1, 2,3,1, 3,4,1, 4,5,1, 6,3,1, 2,4,1, 3,5,1 };
        WeightedGraph<Integer> G = new WeightedGraphUndirected<>();
        GraphIO.populateWeightedGraph(G, links);
        VertexCover<Integer> vcer = new VertexCover<>();
        Set<Integer> vc = vcer.cover(G, 3);
        assert vc.contains(1);
        assert vc.contains(3);
    }
    
}
