package graph;

import org.junit.Test;

public class FordFulkersonTest {

    @Test
    public void testGetValue() {
        /* Calculate the maximum flow for the following graph:
             0 -> 1, cap=16
             0 -> 2, cap=13
             1 -> 2, cap=10
             2 -> 1, cap=4
             1 -> 3, cap=12
             2 -> 4, cap=14
             2 -> 3, cap=9
             3 -> 4, cap=7
             3 -> 5, cap=20
             4 -> 5, cap=4
        Source: 0
        Sink: 5
        */
        Flow<Integer> G = new FlowImpl<>();
        G.link(1, 0, 16);
        G.link(2, 0, 13);
        G.link(2, 1, 10);
        G.link(1, 2, 4);
        G.link(3, 1, 12);
        G.link(4, 2, 14);
        G.link(2, 3, 9);
        G.link(3, 4, 7);
        G.link(5, 3, 20);
        G.link(5, 4, 4);
        // G is the residual network for the graph.
        Flow.Max<Integer> maxflow = G.getMax(0, 5);
        assert maxflow.value == 23; 
    }
    
    @Test
    public void testAugmentations() {
        /* Given this "pathological" flow:
            0 -> 1, cap=1000000
            0 -> 2, cap=1000000
            1 -> 2, cap=1
            1 -> 3, cap=1000000
            2 -> 3, cap=1000000
           The maximum function on a flow should have no more than |V| augmentations
           where |V| is the number of vertices in the flow.
        */
        Flow<Integer> G = new FlowImpl<>();
        // Note: G is the residual network of the "pathological" flow given above.
        G.link(1, 0, 1000000);
        G.link(2, 0, 1000000);
        G.link(2, 1, 1);
        G.link(3, 1, 1000000);
        G.link(3, 2, 1000000);
        
        Flow.Max<Integer> maxFlow = G.getMax(0, 3);
        assert maxFlow.value == 1000000 * 2;
        assert maxFlow.augmentations <= G.numVertices();
    }
}
