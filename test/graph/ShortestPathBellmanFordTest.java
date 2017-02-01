package graph;

import org.junit.Test;

public class ShortestPathBellmanFordTest {

    private final ShortestPathBellmanFord<Character> bfer;

    public ShortestPathBellmanFordTest() {
        this.bfer = new ShortestPathBellmanFord<>(null);
    }
    
    @Test
    public void bellmanFord1() {
        WeightedGraph<Character> g = new WeightedGraphDirected();
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
        
        bfer.setGraph(g);
        ShortestPathBellmanFord.ShortestPath<Character> sp = bfer.singleSourceShortestPaths('s');
        assert !sp.hasNegativeCycles;
        assert sp.weights.get('z') == -2;
        assert sp.weights.get('x') == 4;
    }

    @Test
    public void bellmanFord2() {
        WeightedGraph<Character> g = new WeightedGraphDirected();
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

        bfer.setGraph(g);
        ShortestPathBellmanFord.ShortestPath<Character> sp = bfer.singleSourceShortestPaths('s');
        assert !sp.hasNegativeCycles;
        assert sp.weights.get('z') == 11;
        assert sp.weights.get('x') == 9;
    }

    @Test
    public void bellmanFordNegCycle1() {
        WeightedGraph<Character> g = new WeightedGraphDirected();
        g.link('h', 'i', 2);
        g.link('i', 'j', 3);
        g.link('j', 'h', -8);

        bfer.setGraph(g);
        ShortestPathBellmanFord.ShortestPath<Character> sp = bfer.singleSourceShortestPaths('h');
        assert sp.hasNegativeCycles;
    }

    @Test
    public void bellmanFordNegCycle2() {
        WeightedGraph<Character> g = new WeightedGraphDirected();
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

        bfer.setGraph(g);
        ShortestPathBellmanFord.ShortestPath<Character> sp = bfer.singleSourceShortestPaths('s');
        assert sp.hasNegativeCycles;
    }
    
}
