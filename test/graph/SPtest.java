package graph;

import org.junit.Test;

public class SPtest {
    
    @Test
    public void test1() {
        WeightedGraph<String> g = new WGraphImpl();
        
        g.link("a", "b", 1);
        g.link("a", "c", 4);
        g.link("b", "c", 2);
        g.link("c", "d", 2);
        g.link("b", "d", 5);
        
        assert g.sp("a", "d") == 5;
        
        String s = "a";
        for (String t : g.V()) {
            if (!t.equals(s)) {
                System.out.println( "ShortestPath(" + s + ", " + t + ")=" + g.sp(s, t));
            }
        }
    }
    
    @Test
    public void test2() {
        WeightedGraph<Integer> g = new WGraphImpl();
        
        g.link(1, 2, 3);
        g.link(1, 3, 2);
        g.link(2, 4, 4);
        g.link(3, 2, 1);
        g.link(3, 4, 2);
        g.link(3, 5, 3);
        g.link(4, 5, 2);
        g.link(4, 6, 1);        
        g.link(5, 6, 2);
        
        assert g.sp(1, 6) == 5;
    }
    
}
