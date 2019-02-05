package graph;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class DFSTest {
    
    private final GraphTraversal<Integer> myGAPI;
    
    public DFSTest() {
        Graph<Integer> g = new UGraphMapImpl<>();
        
        g.add(0, 1);
        g.add(0, 2);
        g.add(0, 6);
        g.add(6, 4);
        g.add(4, 3);
        g.add(3, 5);
        g.add(5, 0);
        
        g.add(7, 8);
        
        g.add(9, 10);
        g.add(9, 11);
        g.add(9, 12);
        g.add(11, 12);
        
        this.myGAPI = new GraphTraversalDFS(g, 0);

    }
    
    /**
     * Test of hasPathTo method, of class DFS.
     */
    @Test
    public void testHasPathTo() {
        System.out.println("hasPathTo(3)=" + this.myGAPI.hasPathTo(3));
        System.out.println("hasPathTo(11)=" + this.myGAPI.hasPathTo(11));
    }

    /**
     * Test of pathTo method, of class DFS.
     */
    @Test
    public void testPathTo() {
        for (int i = 1; i < 7; i++) {
            System.out.println("pathTo(" + i + ")=" + this.myGAPI.pathTo(i));
        }
    }
    
    @Test
    public void testReverse() {
        Graph<Integer> g = new DGraphImpl<>();
        
        g.add(0, 1);
        g.add(0, 2);
        g.add(0, 6);
        g.add(6, 4);
        g.add(4, 3);
        g.add(3, 5);
        g.add(5, 0);
        
        g.add(7, 8);
        
        g.add(9, 10);
        g.add(9, 11);
        g.add(9, 12);
        g.add(11, 12);
        g.print();
        g.reverse().print();
    }
    
    @Test
    public void testBFS()
    {
        Graph<Integer> g = new DGraphImpl<>();
        g.add(0, 1);
        g.add(0, 2);
        g.add(1, 2);
        g.add(1, 3);
        g.add(2, 3);
        
        GraphTraversalBFS<Integer> doBFS = new GraphTraversalBFS<>(g, 0);
        
        Iterable<Integer> path = doBFS.pathTo(3);
        for (int i : path) {
            System.out.println(i);
        }
    }
}
