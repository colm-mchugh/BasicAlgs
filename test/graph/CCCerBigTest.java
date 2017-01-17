package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

public class CCCerBigTest {

    @Test
    public void test1() {
        DGraphImpl<Integer> g = new DGraphImpl<>();
        g.add(9, 7);
        g.add(8, 9);
        g.add(7, 8);
        g.add(6, 9);
        g.add(6, 1);
        g.add(5, 6);
        g.add(4, 5);
        g.add(4, 2);
        g.add(3, 4);
        g.add(2, 3);
        g.add(1, 5);
        //g.add(7, 3); // Counter example for "ccs never change", "ccs never decrease by more than 1"
        g.add(3, 6);
        CCCer<Integer> scc = new CCCer(g);
        List<Integer> szs = scc.ccSizes();
        assert szs.size() == 3; 
        for (int sz : szs) {
            assert sz == 3;
        };
        assert scc.sameCC(7, 8) && scc.sameCC(8, 9) && scc.sameCC(9, 7) && !scc.sameCC(6, 9);
        assert scc.sameCC(1, 6) && scc.sameCC(1, 5) && scc.sameCC(5, 6);
        
        // Adding an edge from 7 to 3 means there is one connected component
        g.add(7, 3);
        
        scc = new CCCer(g);
        szs = scc.ccSizes();
        assert szs.size() == 1 && szs.get(0) == 9;
    }

    @Test
    public void test2() {
        DGraphImpl<Integer> g = new DGraphImpl<>();
        g.add(1, 2);
        g.add(2, 3);
        g.add(3, 1);
        g.add(2, 4);
        g.add(3, 5);
        g.add(3, 6);
        g.add(5, 6);
        g.add(6, 7);
        g.add(6, 8);
        g.add(8, 5);
        g.add(7, 8);
        g.add(4, 9);
        g.add(4, 10);
        g.add(10, 11);
        g.add(11, 9);
        g.add(9, 10);
        g.add(6, 9);
        g.add(7, 11);
        g.print();
    }

    @Test
    public void test3() {
        DGraphImpl<Integer> g = new DGraphImpl<>();
        g.add(2, 3);
        g.add(3, 4);
        g.add(4, 5);
        g.add(5, 6);
        g.add(6, 1);
        g.add(1, 5);
        g.add(4, 2);
        g.add(7, 8);
        g.add(8, 9);
        g.add(9, 7);
        g.add(6, 9);
        g.add(6, 8);
        CCCer scc = new CCCer(g);
        scc.ccSizes(); 
    }
    /**
     * Test of a "large" graph.
     */
    @Test
    public void testBigGraph() {
        String file = "resources/SCC.txt";
        Graph directedGraph = readGraph(file);
        System.out.println("Completed creating DiGraph");
        CCCer sccer = new CCCer(directedGraph);
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
