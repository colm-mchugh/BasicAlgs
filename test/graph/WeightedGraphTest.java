/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author colm_mchugh
 */
public class WeightedGraphTest {

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
                int d = g.sp(1, t);
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
        WeightedGraph<Integer> graph = new WGraphImpl<>();
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
}
