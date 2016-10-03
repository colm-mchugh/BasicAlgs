package graph;

import java.util.List;
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
    
    @Test
    public void bellmanFord1() {
        WeightedGraph<Character> g = new WGraphImpl();
        g.link('s', 't', 6);
        g.link('s', 'y', 7);
        g.link('t', 'y', 8);
        g.link('z', 's', 2);
        g.link('t', 'x', 5);
        g.link('y', 'z', 9);
        g.link('x', 't', -2);
        g.link('t', 'z', -4);
        g.link('y', 'x', -3);
        g.link('z', 'x', 7);
        
        WGraphImpl.SingleSourceResult<Character> res = g.singleSourceShortestPaths('s');  
        assert !res.hasNegativeCycles;      
        assert res.weights.get('z') == -2;
        assert res.weights.get('x') == 4;      
    }
    
    @Test
    public void bellmanFord2() {
        WeightedGraph<Character> g = new WGraphImpl();
        g.link('s', 't', 3);
        g.link('s', 'y', 5);
        g.link('t', 'y', 2);
        g.link('y', 't', 1);
        g.link('t', 'x', 6);
        g.link('y', 'x', 4);
        g.link('y', 'z', 6);
        g.link('x', 'z', 2);
        g.link('z', 's', 3);
        g.link('z', 'x', 7);
        
        WGraphImpl.SingleSourceResult<Character> res = g.singleSourceShortestPaths('s');  
        assert !res.hasNegativeCycles;      
        assert res.weights.get('z') == 11;
        assert res.weights.get('x') == 9;      
    }
    
    @Test
    public void bellmanFordNegCycle1() {
        WeightedGraph<Character> g = new WGraphImpl();
        g.link('h', 'i', 2);
        g.link('i', 'j', 3);
        g.link('j', 'h', -8);
        
        WGraphImpl.SingleSourceResult<Character> res = g.singleSourceShortestPaths('h');
        assert res.hasNegativeCycles;
    }
    
    @Test
    public void bellmanFordNegCycle2() {
        WeightedGraph<Character> g = new WGraphImpl();
        g.link('s', 'a', 3);
        g.link('s', 'c', 5);
        g.link('s', 'e', 2);
        g.link('a', 'b', -4);
        g.link('c', 'd', 6);
        g.link('d', 'c', -3);
        g.link('e', 'f', 3);
        g.link('f', 'e', -6);
        g.link('b', 'g', 4);
        g.link('d', 'g', 8);
        g.link('f', 'g', 7);
        
        WGraphImpl.SingleSourceResult<Character> res = g.singleSourceShortestPaths('s');
        assert res.hasNegativeCycles;
    }
    
    @Test
    public void floydWarshall() {
        WeightedGraph<Integer> g = new WGraphImpl<>();
        g.link(1, 2, 3);
        g.link(1, 3, 8);
        g.link(1, 5, -4);
        g.link(2, 5, 7);
        g.link(2, 4, 1);
        g.link(3, 2, 4);
        g.link(4, 1, 2);
        g.link(4, 3, -5);
        g.link(5, 4, 6);
        
        List<WGraphImpl.ShortestPathResult<Integer>> resList = g.allPairsShortestPaths();
        assert resList.size() == 25;
        for (WGraphImpl.ShortestPathResult<Integer> res: resList) {
            if (res.u == 1 && res.v == 5) {
                assert res.d == -4;
            }
            if (res.u == 1 && res.v == 3) {
                assert res.d == -3;
            }
        }
    }
    
    @Test
    public void floydWarshallExt1() {
        WeightedGraph<Integer> g = new WGraphExps<>();
        g.link(1, 2, 1);
        g.link(1, 3, 1);
        g.link(1, 5, 1);
        g.link(2, 5, 1);
        g.link(2, 4, 1);
        g.link(3, 2, 1);
        g.link(4, 1, 1);
        g.link(4, 3, 1);
        g.link(5, 4, 1);
        List<WGraphImpl.ShortestPathResult<Integer>> resList = g.allPairsShortestPaths();
        assert resList.size() == 25;
        for (WGraphImpl.ShortestPathResult<Integer> res : resList) {
            System.out.println(res);
        }
    }
    
    @Test
    public void floydWarshallNegCycle() {
        WeightedGraph<Character> g = new WGraphImpl();
        g.link('s', 'a', 1);
        g.link('s', 'c', 1);
        g.link('s', 'e', 1);
        g.link('a', 'b', 1);
        g.link('c', 'd', 1);
        g.link('d', 'c', 1);
        g.link('e', 'f', 1);
        g.link('f', 'e', 1);
        g.link('b', 'g', 1);
        g.link('d', 'g', 1);
        g.link('f', 'g', 1);
        List<WGraphImpl.ShortestPathResult<Character>> resList = g.allPairsShortestPaths();
        for (WGraphImpl.ShortestPathResult<Character> res : resList) {
            System.out.println(res);
        }
    }
    
    @Test
    public void reweighting() {
        WeightedGraph<Integer> g = new WGraphExps<>();
        g.link(1, 5, -1);
        g.link(2, 1, 1);
        g.link(2, 4, 2);
        g.link(3, 2, 2);
        g.link(3, 6, -8);
        g.link(4, 1, -4);
        g.link(4, 5, 3);
        g.link(5, 2, 7);
        g.link(6, 2, 5);
        g.link(6, 3, 10);
        g.apsp();
        
        g = new WGraphExps<>();
        g.link(1, 2, 10);
        g.link(1, 3, -1);
        g.link(1, 4, 1);
        g.link(3, 5, -1);
        g.link(3, 6, -1);
        g.link(4, 7, 100);
        g.link(4, 6, 1);
        g.apsp();
    }
    @Test
    public void Johnson() {
        WeightedGraph<Character> g = new WGraphImpl<>();
        g.link('a', 'b', -2);
        g.link('b', 'c', -1);
        g.link('c', 'a', 4);
        g.link('c', 'x', 2);
        g.link('c', 'y', -3);
        g.link('z', 'x', 1);
        g.link('z', 'y', -4);
        
        g.apsp();
    }
}
