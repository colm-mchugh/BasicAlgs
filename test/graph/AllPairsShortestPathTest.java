package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

public class AllPairsShortestPathTest {
    

    @Test
    public void testAllPairsShortestPath() {
        String[] files = {"resources/g1.txt", "resources/g2.txt", "resources/g3.txt"};
        Object[] expected = {Integer.MAX_VALUE, Integer.MAX_VALUE, -19};
        
        for (int i = 0; i < files.length; i++) {
            WeightedGraph<Integer> g = this.readGraph(files[i]);
            assert g.apsp() == (int)expected[i];
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
                int u = Integer.parseInt(split[0]);
                int v = Integer.parseInt(split[1]);
                int d = Integer.parseInt(split[2]);
                graph.link(u, v, d);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return graph;
    }
    
}
