package greedy;

import clustering.KCluster;
import clustering.LazyUnion;
import clustering.QuickFind;
import clustering.UnionFind;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

public class KClustererTest {
    
    @Test
    public void testGetKDistance() {
        String file = "resources/clustering.txt";
        int spacing = 4;
        int result = 106;
        boolean doPathCompression = true;
        
        verify(new LazyUnion<>(doPathCompression), file, spacing, result);
        verify(new LazyUnion<>(!doPathCompression), file, spacing, result);
        verify(new QuickFind<>(), file, spacing, result);
    }
    
    private void verify(UnionFind<Integer> uf, String file, int spacing, int expected) {
        KClusterer<Integer> instance = new KClusterer(uf);
        this.init(file, instance);
        assert instance.getKDistance(spacing) == expected;
        
        KCluster kc = new KCluster(file, uf);
        assert kc.doKCluster(spacing) == expected;
    }
    
    public void init(String file, KClusterer<Integer> instance) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] firstLine = line.trim().split("(\\s)+");
            int numNodes = Integer.parseInt(firstLine[0]);
            for (Integer i = 1; i <= numNodes; i++) {
                instance.addCluster(i);
            }
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                int u = Integer.parseInt(split[0]);
                int v = Integer.parseInt(split[1]);
                int d = Integer.parseInt(split[2]);
                instance.addEdge(u, v, d);
            }
        } catch (IOException | NumberFormatException e) {
        }
    }

}
