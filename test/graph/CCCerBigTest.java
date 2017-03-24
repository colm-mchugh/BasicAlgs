package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class CCCerBigTest {

    Graph<Integer> makeDiGraph(int[] vertices) {
        DGraphImpl<Integer> g = new DGraphImpl<>();
        for (int i = 0; i < vertices.length; i += 2) {
            g.add(vertices[i], vertices[i+1]);
        }
        return g;
    }
    
    @Test
    public void test1() {       
        test1(new CCKosaraju<>());
        test1(new CCTarjan<>());
        test2(new CCKosaraju<>());
        test2(new CCTarjan<>());
    }
    
    private void test1(CCer<Integer> scc) {
        int[] graphData = { 9,7, 8,9, 7,8, 6,9, 6,1, 5,6, 4,5, 4,2, 3,4, 2,3, 1,5, 3,6, };
        Graph<Integer> g = this.makeDiGraph(graphData);
        Map<Integer, List<Integer>> components = scc.getComponents(g);
        List<Integer> szs = scc.ccSizes();
        assert szs.size() == 3; 
        for (int sz : szs) {
            assert sz == 3;
        };
        assert scc.sameCC(7, 8) && scc.sameCC(8, 9) && scc.sameCC(9, 7) && !scc.sameCC(6, 9);
        assert scc.sameCC(1, 6) && scc.sameCC(1, 5) && scc.sameCC(5, 6);
    }
    
    private void test2(CCer<Integer> scc) {
        int[] graphData = { 9,7, 8,9, 7,8, 6,9, 6,1, 5,6, 4,5, 4,2, 3,4, 2,3, 1,5, 3,6, 7, 3};
        Graph<Integer> g = this.makeDiGraph(graphData);
        Map<Integer, List<Integer>> components = scc.getComponents(g);
        List<Integer> szs = scc.ccSizes();
        assert szs.size() == 1 && szs.get(0) == 9;
    }
    
    @Test
    public void testCCTarjan() {
        Graph<Character> g = new DGraphImpl<>();
        char[] links = { 'A','B', 'B','C', 'C','A', 'D','B', 'D','C', 'D','F', 'F','D', 'F','E', 
                            'E','C', 'E','G', 'G','E', 'H','F', 'H','G', 'H','H', };
        for (int i = 0; i < links.length; i += 2) {
            g.add(links[i], links[i+1]);
        }
        CCTarjan<Character> Tarjaner = new CCTarjan<>();
        Map<Character, List<Character>> sccs = Tarjaner.getComponents(g);
        assert sccs.size() == 4;
        
    }
    
    @Test
    public void test2() {
        int[] graphData = { 1,2, 2,3, 3,1, 2,4, 3,5, 3,6, 5,6, 6,7, 6,8, 8,5, 7,8, 
            4,9, 4,10, 10,11, 11,9, 9,10, 6,9, 7,11
        };
        Graph<Integer> g = this.makeDiGraph(graphData);
        g.print();
    }

    @Test
    public void test3() {
        int[] graphData = { 2,3, 3,4, 4,5, 5,6, 6,1, 1,5, 4,2, 7,8, 8,9, 9,7, 6,9, 6,8 };
        Graph<Integer> g = this.makeDiGraph(graphData);
        CCKosaraju scc = new CCKosaraju();
        scc.getComponents(g);
        scc.ccSizes(); 
    }
    
    private final static String FILE = "resources/SCC.txt";
    
    @Test
    public void testBigGraphTarjan() {
        testBigGraph(new CCTarjan<>());
    }
    
    @Test
    public void testBigGraphKosaraju() {
        testBigGraph(new CCKosaraju<>());
    }
    
    // This test may require setting JVM -Xss parameter.
    private void testBigGraph(CCer<Integer> ccer) {
        Graph<Integer> g = readGraph(FILE);
        Map<Integer, List<Integer>> components = ccer.getComponents(g);
        int[] expectedCCSizes = {434821, 968, 459, 313, 211};
        Iterator<Integer> ccSizes = ccer.ccSizes().iterator();
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
    private Graph<Integer> readGraph(String path) {
        DGraphImpl<Integer> graph = new DGraphImpl<>();
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
