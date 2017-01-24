package greedy;

import graph.WeightedGraph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class KruskalMSTTest {
    
    @Test
    public void testMst() {
        System.out.println("mst");
        KruskalMST<Integer> instance = this.readGraph("resources/edges.txt");
        WeightedGraph<Integer> result = instance.mst();
        assertEquals(-3612829, result.cost());
    }

    private KruskalMST<Integer> readGraph(String path) {
        KruskalMST<Integer> instance = new KruskalMST<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while (br.ready()) {
                String[] split = br.readLine().trim().split("(\\s)+");
                int u = Integer.parseInt(split[0]);
                int v = Integer.parseInt(split[1]);
                int d = Integer.parseInt(split[2]);
                instance.addEdge(u, v, d);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return instance;
    }

    
    
}
