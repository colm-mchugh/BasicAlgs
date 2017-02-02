package graph.shortestpath;

import graph.WeightedGraph;
import graph.WeightedGraphDirected;
import graph.shortestpath.Dijkstra;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.junit.Test;

public class DijkstraTest {

    /**
     * Test of computing Dijkstra's shortest path for positive weighted directed graphs.
     */
    @Test
    public void testDijkstraShortestPath() {
        WeightedGraph<Integer> g = this.readGraph("resources/spaths.txt");
        Integer[] verticesOfInterest = {7,37,59,82,99,115,133,165,188,197};
        Integer[] expectedDistances = {2599, 2610, 2947, 2052, 2367, 2399, 2029, 2442, 2505, 3068};
        Map<Integer, Integer> distances = new HashMap<>();
        for (Integer v : verticesOfInterest) {
            distances.put(v, 1000000);
        }
        for (Integer t : g.V()) {
            if (distances.keySet().contains(t)) {
                Dijkstra<Integer> shortestPathCalcer = new Dijkstra<>(g);
                int d = shortestPathCalcer.sp(1, t); //g.sp(1, t);
                distances.replace(t, d);
            }
        }
        for (int i = 0; i < verticesOfInterest.length; i++) {
            assert Objects.equals(distances.get(verticesOfInterest[i]), expectedDistances[i]);
        }
    }

    /**
     * Read a weighted directed graph of Integers from the given file.
     * The data in the file is one or more lines of the form:
     * 
     * n1   v1,w1 v2,w2 .... vn,wn
     * 
     * where n1 is a vertex in the graph and each vi,wi is, respectively, a 
     * vertex that forms an edge with n1 and the weight of that edge.
     * For example, the line:
     * 
     * 11   76,244 89,453
     * 
     * means that vertex 11 has an edge to vertex 76 of weight 244 and an edge
     * to vertex 89 of weight 453.
     * 
     * @param path
     * @return 
     */
    WeightedGraph<Integer> readGraph(String path) {
        WeightedGraph<Integer> graph = new WeightedGraphDirected<>();
        FileReader fr;
        try {
            fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] split = line.trim().split( "(\\s)+" );
                int u = Integer.parseInt(split[0]);
                for (int j = 1; j < split.length; j++) {
                    String[] sp2 = split[j].split(",");
                    int v = Integer.parseInt(sp2[0]);
                    int d = Integer.parseInt(sp2[1]);
                    graph.link(u, v, d+0);
                }
            }
        } catch ( IOException | NumberFormatException e ) {
        }
        System.out.println("Completed creating WGraph");
        return graph;
    }
    
    @Test
    public void test1() {
        WeightedGraph<String> g = new WeightedGraphDirected();

        g.link("a", "b", 1);
        g.link("a", "c", 4);
        g.link("b", "c", 2);
        g.link("c", "d", 2);
        g.link("b", "d", 5);

        Dijkstra<String> sper = new Dijkstra<>(g);
        
        assert sper.sp("a", "d") == 5;

        String s = "a";
        for (String t : g.V()) {
            if (!t.equals(s)) {
                System.out.println("ShortestPath(" + s + ", " + t + ")=" + sper.sp(s, t));
            }
        }
    }

    @Test
    public void test2() {
        WeightedGraph<Integer> g = new WeightedGraphDirected();

        g.link(1, 2, 3);
        g.link(1, 3, 2);
        g.link(2, 4, 4);
        g.link(3, 2, 1);
        g.link(3, 4, 2);
        g.link(3, 5, 3);
        g.link(4, 5, 2);
        g.link(4, 6, 1);
        g.link(5, 6, 2);

        Dijkstra<Integer> sper = new Dijkstra<>(g);
        
        assert sper.sp(1, 6) == 5;
    }

    @Test
    public void test3() {
        WeightedGraph<Integer> g = new WeightedGraphDirected();

        g.link(1, 2, 1);
        g.link(1, 3, 1);
        g.link(2, 4, 2);
        g.link(2, 3, 3);
        g.link(3, 4, 2);
        g.link(4, 5, 1);
        g.link(4, 6, 2);
        g.link(5, 7, 1);
        g.link(6, 7, 1);
        g.link(7, 6, 1);
        
        printAllSps(g);
    }

    @Test
    public void Test4() {
        WeightedGraph<Integer> g = new WeightedGraphDirected();

        g.link(1, 2, 2);
        g.link(2, 3, 1);
        g.link(3, 1, 4);
        g.link(3, 4, 2);
        g.link(3, 5, 3);
        g.link(6, 4, 1);
        g.link(6, 5, 4);
        
        printAllSps(g);
        
    }
    
    private void printAllSps(WeightedGraph<Integer> g) {
        Dijkstra<Integer> sper = new Dijkstra<>(g);
        for (Integer u : g.V()) {
            for (Integer v : g.V()) {
                if (u.equals(v)) {
                    System.out.println("(" + u + " -> " + v + ") = 0");
                } else {
                    int duv = sper.sp(u, v);
                    if (duv == Integer.MAX_VALUE) {
                        System.out.println("No path: " + u + " -> " + v);
                    } else {
                        System.out.println("(" + u + " -> " + v + ") = " + duv);
                    }
                }
            }
        }
    }

    
}
