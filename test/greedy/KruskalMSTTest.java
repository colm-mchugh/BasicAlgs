package greedy;

import clustering.LazyUnion;
import clustering.QuickFind;
import clustering.UnionFind;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class KruskalMSTTest {
    
    @Test
    public void testMst() {
        System.out.println("mst");
        String file = "resources/edges.txt";
        boolean withPathCompression = true;
        boolean trackLeaderSets = true;
        
        this.verify(new QuickFind<>(trackLeaderSets), file, -3612829);
        this.verify(new QuickFind<>(!trackLeaderSets), file, -3612829);           
        this.verify(new LazyUnion<>(withPathCompression), file, -3612829);
        this.verify(new LazyUnion<>(!withPathCompression), file, -3612829);
    }

    private void verify(UnionFind<Integer> uf, String file, long expected) {
        long now = System.currentTimeMillis();
        KruskalMST<Integer> instance = new KruskalMST<>(uf);
        readGraph(instance, file);
        long result = instance.mst().cost();
        assertEquals(expected, result);
        long timeTaken = System.currentTimeMillis() - now;
        System.out.println("Time taken: " + timeTaken);
    }
    
    private void readGraph(KruskalMST<Integer> instance, String path) {      
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
    }

    
    
}
