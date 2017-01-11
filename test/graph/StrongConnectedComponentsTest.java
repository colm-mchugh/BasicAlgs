package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.junit.Test;

public class StrongConnectedComponentsTest {

    /**
     * Test of main method, of class StrongConnectedComponents.
     */
    @Test
    public void testMain() {
        String file = "resources/SCC.txt";
        Graph directedGraph = readGraph(file);
        System.out.println("Completed creating DiGraph");
        StrongCC sccer = new StrongCC(directedGraph);
        Iterator<Integer> ccSizes = sccer.ccSizes().iterator();
        int[] expectedCCSizes = {434821, 968, 459, 313, 211};
        for (int sz : expectedCCSizes) {
            int nextSize = ccSizes.next();
            assert sz == nextSize;
        }
    }

    private Graph readGraph(String path) {
        DGraphImpl graph = new DGraphImpl<>();
        FileReader fr;
        try {
            fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                int u = Integer.parseInt(split[0]);
                int v = Integer.parseInt(split[1]);
                graph.add(u, v);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return graph;
    }

}
