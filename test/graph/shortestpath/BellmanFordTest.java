package graph.shortestpath;

import graph.GraphIO;
import graph.WeightedGraph;
import graph.WeightedGraphDirected;
import org.junit.Test;

public class BellmanFordTest {

    private final BellmanFord<Character> bfer;

    public BellmanFordTest() {
        this.bfer = new BellmanFord<>();
    }

    @Test
    public void bellmanFord0() {
        WeightedGraph<Character> g = new WeightedGraphDirected();
        g.link('s', 'v', 2);
        g.link('s', 'x', 4);
        g.link('v', 'x', 1);
        g.link('v', 'w', 2);
        g.link('x', 't', 4);
        g.link('w', 't', 2);

        BellmanFord.ShortestPath<Character> sp = bfer.singleSourceShortestPaths(g, 's');
        assert !sp.hasNegativeCycles;
        assert sp.weights.get('v') == 2;
        assert sp.weights.get('x') == 3;
        assert sp.weights.get('w') == 4;
        assert sp.weights.get('t') == 6;
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

        BellmanFord.ShortestPath<Character> sp = bfer.singleSourceShortestPaths(g, 's');
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

        BellmanFord.ShortestPath<Character> sp = bfer.singleSourceShortestPaths(g, 's');
        assert !sp.hasNegativeCycles;
        assert sp.weights.get('z') == 11;
        assert sp.weights.get('x') == 9;
        assert sp.weights.get('t') == 3;
    }

    @Test
    public void bellmanFordNegCycle1() {
        WeightedGraph<Character> g = new WeightedGraphDirected();
        g.link('h', 'i', 2);
        g.link('i', 'j', 3);
        g.link('j', 'h', -8);

        BellmanFord.ShortestPath<Character> sp = bfer.singleSourceShortestPaths(g, 'h');
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

        BellmanFord.ShortestPath<Character> sp = bfer.singleSourceShortestPaths(g, 's');
        assert sp.hasNegativeCycles;
    }
    
    @Test
    public void bigTest() {
        String file = "resources/g4.txt";
        WeightedGraph<Integer> g = GraphIO.readWeightedGraphDirected(file);
        BellmanFord<Integer> bf = new BellmanFord<>();
        BellmanFord.ShortestPath<Integer> sp = bf.singleSourceShortestPaths(g, 1);
        assert !sp.hasNegativeCycles;
    }

}
