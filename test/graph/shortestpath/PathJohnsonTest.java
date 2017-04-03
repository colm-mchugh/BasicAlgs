package graph.shortestpath;

import graph.GraphIO;
import graph.WeightedGraph;
import graph.WeightedGraphDirected;
import org.junit.Test;

public class PathJohnsonTest {
    
    @Test
    public void Johnson1() {
        WeightedGraph<Character> g = new WeightedGraphDirected<>();
        g.link('a', 'b', -2);
        g.link('b', 'c', -1);
        g.link('c', 'a', 4);
        g.link('c', 'x', 2);
        g.link('c', 'y', -3);
        g.link('z', 'x', 1);
        g.link('z', 'y', -4);

        Johnson<Character> sper = new Johnson<>();
        assert sper.sp(g).d == -6 ;
    }
    
    @Test
    public void Johnson2() {
        WeightedGraph<Integer> g = new WeightedGraphDirected();
        int[] links = {1,2,1, 1,3,1, 2,4,2, 2,3,3, 3,4,-2, 4,5,-1, 4,6,2, 5,7,-1, 6,7,-1, 7,6,1};
        GraphIO.populateWeightedGraph(g, links);
        Johnson<Integer> sper = new Johnson<>();
        assert sper.sp(g).d == -4;
    }
    
    @Test
    public void Johnson3() {
        WeightedGraph<Integer> g = new WeightedGraphDirected();
        int[] links = {1,2,-2, 2,3,-1, 3,1,4, 3,4,2, 3,5,-3, 6,4,1, 6,5,-4};
        GraphIO.populateWeightedGraph(g, links);
        Johnson<Integer> sper = new Johnson<>();
        assert sper.sp(g).d == -6;
    }
    
    @Test
    public void Johnson4() {
        WeightedGraph<Integer> g = new WeightedGraphDirected();
        int[] links = {1,6,-10, 1,2,-5, 2,3,1, 3,4,1, 4,5,-10000};
        GraphIO.populateWeightedGraph(g, links);
        Johnson<Integer> sper = new Johnson<>();
        assert sper.sp(g).d == -10003;
    }

    @Test
    public void testReallyBiginquotesGraph() {
        String[] files = {"resources/g3.txt", "resources/g1.txt", "resources/g2.txt", };
        int[] expected = {-19, Integer.MAX_VALUE, Integer.MAX_VALUE};   
        Johnson<Integer> sper = new Johnson<>();
        for (int i = 0; i < files.length; i++) {
            assert sper.sp(GraphIO.readWeightedGraphDirected(files[i])).d == expected[i];
        }
    }
    
}
