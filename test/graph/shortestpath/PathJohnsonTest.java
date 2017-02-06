package graph.shortestpath;

import graph.WeightedGraph;
import graph.WeightedGraphDirected;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

        g.link(1, 2, 1);
        g.link(1, 3, 1);
        g.link(2, 4, 2);
        g.link(2, 3, 3);
        g.link(3, 4, -2);
        g.link(4, 5, -1);
        g.link(4, 6, 2);
        g.link(5, 7, -1);
        g.link(6, 7, -1);
        g.link(7, 6, 1);

        Johnson<Integer> sper = new Johnson<>();
        assert sper.sp(g).d == -4;
    }
    
    @Test
    public void Johnson3() {
        WeightedGraph<Integer> g = new WeightedGraphDirected();

        g.link(1, 2, -2);
        g.link(2, 3, -1);
        g.link(3, 1, 4);
        g.link(3, 4, 2);
        g.link(3, 5, -3);
        g.link(6, 4, 1);
        g.link(6, 5, -4);

        Johnson<Integer> sper = new Johnson<>();
        assert sper.sp(g).d == -6;
    }
    
    @Test
    public void Johnson4() {
        WeightedGraph<Integer> g = new WeightedGraphDirected();

        g.link(1, 6, -10);
        g.link(1, 2, -5);
        g.link(2, 3, 1);
        g.link(3, 4, 1);
        g.link(4, 5, -10000);
        
        Johnson<Integer> sper = new Johnson<>();
        assert sper.sp(g).d == -10003;
    }

    @Test
    public void testReallyBiginquotesGraph() {
        String[] files = {"resources/g1.txt", "resources/g2.txt", "resources/g3.txt"};
        Object[] expected = {Integer.MAX_VALUE, Integer.MAX_VALUE, -19};
        
        Johnson<Integer> sper = new Johnson<>();
        for (int i = 0; i < files.length; i++) {
            assert sper.sp(this.readGraph(files[i])).d == (int)expected[i];
        }
    }

    private WeightedGraph<Integer> readGraph(String file) {
        WeightedGraph<Integer> graph = new WeightedGraphDirected<>();
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                int uVertex = Integer.parseInt(split[0]);
                int vVertex = Integer.parseInt(split[1]);
                int edgeWeight = Integer.parseInt(split[2]);
                graph.link(uVertex, vVertex, edgeWeight);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return graph;
    }
    
}
