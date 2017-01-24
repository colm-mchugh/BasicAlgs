package greedy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

public class KClustererTest {
    
    @Test
    public void testGetKDistance() {
        KClusterer<Integer> instance = new KClusterer();
        this.init("resources/clustering.txt", instance);
        assert instance.getKDistance(4) == 106;
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
