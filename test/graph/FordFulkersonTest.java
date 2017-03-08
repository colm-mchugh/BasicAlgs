package graph;

import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class FordFulkersonTest {

    @Test
    public void testGetValue() {
        Flow<Integer> G = new FlowImpl<>();
        G.link(0, 1, 16);
        G.link(0, 2, 13);
        G.link(1, 2, 10);
        G.link(2, 1, 4);
        G.link(1, 3, 12);
        G.link(2, 4, 14);
        G.link(3, 2, 9);
        G.link(4, 3, 7);
        G.link(3, 5, 20);
        G.link(4, 5, 4);
        
        FordFulkerson<Integer> ff = new FordFulkerson<>(G, 0, 5);
        assert ff.getValue() == 23; 
    }
    
}
