package graph.flow;

import java.util.Collection;
import org.junit.Test;

public class GraphTest {
    // gN = { u,v,cap, .... }
    private static final int[] g1 = { 1,2,1, 1,3,100, 2,3,100, 3,2,1, 2,4,100, 3,4,1};
    private static final int[] g2 = { 1,2,16, 1,3,13, 3,2,4, 2,4,12, 3,5,14, 4,3,9, 5,4,7, 4,6,20, 5,6,4 };
    private static final int[] g3 = { 1,2,1000, 1,3,1000, 2,3,1, 2,4,1000, 3,4,1000};
    
    @Test
    public void testG1() {
        Graph<Integer> G1 = makeGraph(g1);
        
        assert(G1.numVertices() == 4);
        Collection<Graph.Edge<Integer>> edges = G1.edgesOf(3);
        int edgeCount = 0;
        for (Graph.Edge<Integer> e : edges) {
            edgeCount += (e.cap > 0 ? 1 : 0);
        }
        assert(edgeCount == 2);
        
        Graph.Edge<Integer> revEdge = G1.reverseEdge(2, 3);
        assert(revEdge.cap == 1);
        
        revEdge = G1.reverseEdge(3, 2);
        assert(revEdge.cap == 100);
    }

    @Test
    public void testG2() {
        Graph<Integer> G2 = makeGraph(g2);
        
        assert(G2.numVertices() == 6);
        Collection<Graph.Edge<Integer>> edges = G2.edgesOf(3);
        assert(edges.size() == 2);
    }
    
    
    private Graph<Integer> makeGraph(int data[]) {
        Graph<Integer> graph = new Graph<>();
        for (int i = 0; i < data.length; i += 3) {
            int u = data[i];
            int v = data[i + 1];
            int cap = data[i + 2];
            
            graph.link(u, v, cap);
            graph.link(v, u, 0);
        }
        return graph;
    }
    
}
