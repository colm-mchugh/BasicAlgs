package greedy;

import graph.GraphIO;
import graph.WeightedGraph;
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
        WeightedGraph<Integer> MST = p.slowMst();
        assert MST.cost() == -3612829;
    }

    /**
     * Test of mstCost method, of class PrimMST.
     */
    @Test
    public void testHeapMstCost() {
        WeightedGraphUndirected<Integer> pish = this.readWeightedUndirectedGraph("resources/edges.txt");
        PrimMST p = new PrimMST(pish);
        long minCost = p.heapMST();
        assert minCost == -3612829;
    }

    @Test
    public void testSmall() {
        int[][] links = {{0, 1, 4, 1, 2, 8, 2, 3, 7, 3, 4, 9, 4, 5, 10, 5, 6, 2, 6, 7, 1, 7, 0, 8,
            1, 7, 11, 7, 8, 7, 6, 8, 6, 2, 8, 2, 2, 5, 4, 3, 5, 14},
        {1, 2, 2, 2, 3, 6, 3, 1, 3, 2, 4, 5, 2, 5, 3, 3, 5, 2, 4, 5, 1, 4, 6, 2, 5, 6, 4},
        {1, 2, 6, 1, 3, 1, 1, 4, 5, 2, 3, 5, 3, 4, 5, 2, 5, 3, 3, 5, 6, 3, 6, 4, 4, 6, 2, 5, 6, 6},
        {1, 2, 4, 2, 3, 8, 3, 4, 7, 4, 5, 9, 5, 6, 10, 6, 7, 2, 7, 8, 1, 8, 1, 8, 2, 8, 11, 8, 9, 7, 9, 3, 2, 9, 7, 6, 3, 6, 4, 4, 6, 14}};
        int[] expected = {37, 10, 15, 37};
        int i = 0;
        for (int[] link : links) {
            WeightedGraphUndirected<Integer> pish = new WeightedGraphUndirected<>();
            GraphIO.populateWeightedGraph(pish, link);
            PrimMST p = new PrimMST(pish);
            long minCostheap = p.heapMST();
            long minCostSlow = p.slowMst().cost();
            assert minCostSlow == minCostheap;
            assert minCostheap == expected[i++];
        }
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
