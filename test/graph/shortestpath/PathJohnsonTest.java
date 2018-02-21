package graph.shortestpath;

import graph.GraphIO;
import graph.WeightedGraph;
import graph.WeightedGraphDirected;
import java.util.List;
import java.util.Set;
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
        Set<Path<Character>> pish = sper.apsp(g);
        int min = Integer.MAX_VALUE;
        for (Path<Character> p : pish) {
            min = Integer.min(min, p.d);
        }
        assert min == -6 ;
    }
    
    @Test
    public void Johnson2() {
        int[] links = {1,2,1, 1,3,1, 2,4,2, 2,3,3, 3,4,-2, 4,5,-1, 4,6,2, 5,7,-1, 6,7,-1, 7,6,1};
        testArray(links, -4);
    }
    
    @Test
    public void Johnson3() {
        int[] links = {1,2,-2, 2,3,-1, 3,1,4, 3,4,2, 3,5,-3, 6,4,1, 6,5,-4};
        testArray(links, -6);
    }
    
    @Test
    public void Johnson4() {
        int[] links = {1,6,-10, 1,2,-5, 2,3,1, 3,4,1, 4,5,-10000};
        testArray(links, -10003);
    }

    @Test 
    public void testBigGraph1() {
        testFile("resources/g1.txt", Integer.MAX_VALUE);
    }
    
    @Test 
    public void testBigGraph2() {
        testFile("resources/g2.txt", Integer.MAX_VALUE);
    }
    
    @Test 
    public void testBigGraph3() {
        testFile("resources/g3.txt", -19);
    }
    
    private void testFile(String file, int expected) {
        Johnson<Integer> sper = new Johnson<>();
        Set<Path<Integer>> pish = sper.apsp(GraphIO.readWeightedGraphDirected(file));
        int min = Integer.MAX_VALUE;
        for (Path<Integer> p : pish) {
            min = Integer.min(min, p.d);
        }
        assert min == expected;
    }
    
    private void testArray(int[] links, int expected) {
        Johnson<Integer> sper = new Johnson<>();
        WeightedGraph<Integer> g = new WeightedGraphDirected();
        GraphIO.populateWeightedGraph(g, links);
        Set<Path<Integer>> pish = sper.apsp(g);
        int min = Integer.MAX_VALUE;
        for (Path<Integer> p : pish) {
            min = Integer.min(min, p.d);
        }
        assert min == expected;
    }
}
