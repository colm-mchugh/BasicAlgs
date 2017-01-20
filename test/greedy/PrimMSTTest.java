package greedy;

import graph.WeightedGraphUndirected;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

public class PrimMSTTest {
    
    /**
     * Test of mstCost method, of class PrimMST.
     */
    @Test
    public void testMstCost() {
        WeightedGraphUndirected<Integer> pish = this.readWeightedUndirectedGraph("resources/edges.txt");
        PrimMST p = new PrimMST(pish);
        assert p.mstCost() == -3612829;
    }
    
    WeightedGraphUndirected<Integer> readWeightedUndirectedGraph(String path) {
        WeightedGraphUndirected<Integer> graph = new WeightedGraphUndirected<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while (br.ready()) {
                String[] split = br.readLine().trim().split("(\\s)+");
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
