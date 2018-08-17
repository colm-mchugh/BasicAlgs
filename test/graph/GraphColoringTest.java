package graph;

import org.junit.Test;

public class GraphColoringTest {
    
    /**
     * Test of color method, of class GraphColoring.
     */
    @Test
    public void testColor() {
        Graph<Integer> g = new UGraphMapImpl<>();
        int[] links = { 1,2, 1,3, 1,4, 2,5, 4,6,
                    3,7, 3,8, 5,6, 5,8, 6,7,
                    9,7, 10,8, 
                    2,9, 9,10, 10,4,
        };
        
        for (int i = 0; i < links.length; i += 2) {
            g.add(links[i], links[i+1]);
        }
        GraphColoring<Integer> gc = new GraphColoring<>();
        assert gc.color(g, 3);
        assert !gc.color(g, 2);
    }
    
}
