package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.junit.Test;

public class StrongConnectedComponentsTest {

    /**
     * Test of StrongConnectedComponents.
     */
    @Test
    public void testSCC() {
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

    /**
     * Initialize a directed graph of integers from data based in the given file.
     * The format of the data in the file is one or more lines of:
     * 
     * number number
     * 
     * Each line represents an edge in a directed graph. For example, the line:
     * 
     * 6 7
     * 
     * means there will be two vertices, 6 and 7, and an edge connecting 6 to 7. .
     * 
     * @param path path to the file containing the data
     * @return 
     */
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
