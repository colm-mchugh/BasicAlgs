package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class CCCerBigTest {

    Graph<Integer> makeDiGraph(int[] vertices) {
        DGraphImpl<Integer> g = new DGraphImpl<>();
        for (int i = 0; i < vertices.length; i += 2) {
            g.add(vertices[i], vertices[i + 1]);
        }
        return g;
    }

    @Test
    public void test1() {
        int[] graphData = {9, 7, 8, 9, 7, 8, 6, 9, 6, 1, 5, 6, 4, 5, 4, 2, 3, 4, 2, 3, 1, 5, 3, 6,};
        int[] sccSzs = {3, 3, 3};
        int[] sameCCs = {7, 8, 8, 9, 9, 7, 1, 6, 1, 5, 5, 6};
        int[] notsameCCs = {6, 9};
        this.runScctest(new CCTarjan<>(), graphData, sccSzs.length, sccSzs, sameCCs, notsameCCs);
        this.runScctest(new CCKosaraju<>(), graphData, sccSzs.length, sccSzs, sameCCs, notsameCCs);
        this.runScctest(graphData, sccSzs.length, sccSzs, sameCCs, notsameCCs);
    }

    @Test
    public void test2() {
        int[] graphData = {9, 7, 8, 9, 7, 8, 6, 9, 6, 1, 5, 6, 4, 5, 4, 2, 3, 4, 2, 3, 1, 5, 3, 6, 7, 3};
        int[] sccSzs = {9};
        int[] sameCCs = {7, 8, 8, 9, 9, 7, 1, 6, 1, 5, 5, 6};
        int[] notsameCCs = {};
        this.runScctest(new CCTarjan<>(), graphData, sccSzs.length, sccSzs, sameCCs, notsameCCs);
        this.runScctest(new CCKosaraju<>(), graphData, sccSzs.length, sccSzs, sameCCs, notsameCCs);
        this.runScctest(graphData, sccSzs.length, sccSzs, sameCCs, notsameCCs);
    }

    @Test
    public void testCCTarjan() {
        Graph<Character> g = new DGraphImpl<>();
        char[] links = {'A', 'B', 'B', 'C', 'C', 'A', 'D', 'B', 'D', 'C', 'D', 'F', 'F', 'D', 'F', 'E',
            'E', 'C', 'E', 'G', 'G', 'E', 'H', 'F', 'H', 'G', 'H', 'H',};
        for (int i = 0; i < links.length; i += 2) {
            g.add(links[i], links[i + 1]);
        }
        CCTarjan<Character> Tarjaner = new CCTarjan<>();
        Map<Character, List<Character>> sccs = Tarjaner.getComponents(g);
        assert sccs.size() == 4;
        for (List<Character> scc : sccs.values()) {
            System.out.println("scc sz:" + scc.size());
            System.out.print("[ ");
            for (Character v : scc) {
                System.out.print(v);
                System.out.print(' ');
            }
            System.out.println("]");
        }
    }

    @Test
    public void test5() {
        int[] graphData = {1, 2, 2, 3, 3, 1, 2, 4, 3, 5, 3, 6, 5, 6, 6, 7, 6, 8, 8, 5, 7, 8,
            4, 9, 4, 10, 10, 11, 11, 9, 9, 10, 6, 9, 7, 11
        };
        Graph<Integer> g = this.makeDiGraph(graphData);
        g.print();

        CCer<Integer> scc = new CCKosaraju();
        scc.getComponents(g);
        scc.ccSizes();

        scc = new CCTarjan<>();
        scc.getComponents(g);
        scc.ccSizes();

    }

    @Test
    public void test3() {
        int[] graphData = {2, 3, 3, 4, 4, 5, 5, 6, 6, 1, 1, 5, 4, 2, 7, 8, 8, 9, 9, 7, 6, 9, 6, 8};
        int[] sccSzs = {3, 3, 3};
        int[] samePairs = {2, 4, 5, 1, 8, 9, 7, 9};
        int[] notSamePairs = {2, 5, 6, 8, 9, 3};

        runScctest(new CCKosaraju<>(), graphData, 3, sccSzs, samePairs, notSamePairs);
        runScctest(new CCTarjan<>(), graphData, 3, sccSzs, samePairs, notSamePairs);
        this.runScctest(graphData, sccSzs.length, sccSzs, samePairs, notSamePairs);
    }

    @Test
    public void test4() {
        int[] graphData = {1, 2, 2, 3, 3, 4, 4, 5, 5, 2, 4, 6, 6, 7, 7, 8, 8, 9, 9, 6};
        int[] sccSzs = {4, 4, 1};
        int[] sameCC = {5, 2, 6, 8};
        int[] notSame = {1, 5, 3, 9};

        runScctest(new CCKosaraju<>(), graphData, 3, sccSzs, sameCC, notSame);
        runScctest(new CCTarjan<>(), graphData, 3, sccSzs, sameCC, notSame);
        runScctest(graphData, 3, sccSzs, sameCC, notSame);
    }

    private void runScctest(CCer<Integer> sccer, int[] graphData, int sz, int[] sccSzs, int[] samePairs, int[] notSamePairs) {
        Map<Integer, List<Integer>> sccs = sccer.getComponents(makeDiGraph(graphData));
        List<Integer> szs = sccer.ccSizes();
        assert sccs.size() == sz;
        for (List<Integer> scc : sccs.values()) {
            System.out.println("scc sz:" + scc.size());
            System.out.print("[ ");
            for (Integer v : scc) {
                System.out.print(v);
                System.out.print(' ');
            }
            System.out.println("]");
        }
        for (int i = 0; i < sccSzs.length; i++) {
            assert sccSzs[i] == szs.get(i);
        }
        for (int i = 0; i < samePairs.length; i += 2) {
            assert sccer.sameCC(samePairs[i], samePairs[i + 1]);
        }
        for (int i = 0; i < notSamePairs.length; i += 2) {
            assert !sccer.sameCC(notSamePairs[i], notSamePairs[i + 1]);
        }
    }

    private void runScctest(int[] graphData, int sz, int[] sccSzs, int[] samePairs, int[] notSamePairs) {
        CCKosarajuIterative<Integer> sccer = new CCKosarajuIterative<Integer>();
        List<Integer> graphLinks = new ArrayList<>(graphData.length);
        for (int i = 0; i < graphData.length; i++) {
            graphLinks.add(graphData[i]);
        }
        Map<Integer, Set<Integer>> sccs = sccer.getComponents(graphLinks);
        List<Integer> szs = sccer.ccSizes();
        assert sccs.size() == sz;
        for (Set<Integer> scc : sccs.values()) {
            System.out.println("scc sz:" + scc.size());
            System.out.print("[ ");
            for (Integer v : scc) {
                System.out.print(v);
                System.out.print(' ');
            }
            System.out.println("]");
        }
        for (int i = 0; i < sccSzs.length; i++) {
            assert sccSzs[i] == szs.get(i);
        }
        for (int i = 0; i < samePairs.length; i += 2) {
            assert sccer.sameCC(samePairs[i], samePairs[i + 1]);
        }
        for (int i = 0; i < notSamePairs.length; i += 2) {
            assert !sccer.sameCC(notSamePairs[i], notSamePairs[i + 1]);
        }
    }

    private final static String[] files = { "resources/SCC.txt" };
    private final static int[][] expected = { {434821, 968, 459, 313, 211} };
    
    @Test
    public void testBigGraphTarjan() {
        for (String file : files) 
            testBigGraph(new CCTarjan<>(), file);
    }

    @Test
    public void testBigGraphKosaraju() {
        for (String file : files)
            testBigGraph(new CCKosaraju<>(), file);
    }

    // This test may require setting JVM -Xss parameter.
    private void testBigGraph(CCer<Integer> ccer, String file) {
        long pish = System.currentTimeMillis();
        Graph<Integer> g = readGraph(file);
        Map<Integer, List<Integer>> components = ccer.getComponents(g);
        int[] expectedCCSizes = {434821, 968, 459, 313, 211};
        Iterator<Integer> ccSizes = ccer.ccSizes().iterator();
        long timeTaken = System.currentTimeMillis() - pish;
        System.out.println("Time taken:" + timeTaken);
        for (int sz : expectedCCSizes) {
            int nextSize = ccSizes.next();
            assert sz == nextSize;
        }
    }

    @Test
    public void testSmallGraph() {
        int[] graphData = {1, 4, 4, 7, 7, 1, 9, 7, 9, 3, 3, 6, 6, 9, 8, 6, 8, 5, 5, 2, 2, 8};
        CCKosarajuIterative<Integer> gt = new CCKosarajuIterative<>();
        List<Integer> graphLinks = new ArrayList<>(graphData.length);
        for (int i = 0; i < graphData.length; i++) {
            graphLinks.add(graphData[i]);
        }
        CCKosarajuIterative.Graph<Integer> g = gt.makeGraph(graphLinks);
        CCKosarajuIterative.Graph<Integer> gRev = g.reverse();
        CCKosarajuIterative.Vertex<Integer> startV = gRev.vertexMap.get(9);
        List<CCKosarajuIterative.Vertex<Integer>> ordering = new ArrayList<>(graphData.length);

        gt.topOrder(gRev, startV, ordering);
        for (CCKosarajuIterative.Vertex<Integer> v : gRev.rep.keySet()) {
            if (!v.explored) {
                gt.topOrder(gRev, v, ordering);
            }
        }
        assert (ordering.get(0).data == 3);
        assert (ordering.get(6).data == 1);

        for (int i = ordering.size() - 1; i >= 0; i--) {
            CCKosarajuIterative.Vertex<Integer> v = ordering.get(i);
            v = g.vertexMap.get(v.data);
            if (!v.explored) {
                gt.components.put(v.data, new HashSet<>());
                gt.populateConnectedComponents(g, v, v);
            }
        }
        assert (gt.components.size() == 3);
        for (Integer c : gt.components.keySet()) {
            assert (gt.components.get(c).size() == 3);
        }
    }

    @Test
    public void testIterativeKosaraju() {
        long pish = System.currentTimeMillis();

        List<Integer> data = readList(files[0]);
        CCKosarajuIterative<Integer> gt = new CCKosarajuIterative<>();

        Map<Integer, Set<Integer>> components = gt.getComponents(data);
        int[] expectedCCSizes = {434821, 968, 459, 313, 211};
        List<Integer> rv = gt.ccSizes();
        long timeTaken = System.currentTimeMillis() - pish;
        System.out.println("Time taken:" + timeTaken);

        Iterator<Integer> ccSizes = rv.iterator();
        for (int sz : expectedCCSizes) {
            int nextSize = ccSizes.next();
            assert sz == nextSize;
        }
        
    }

    /**
     * Initialize a directed graph of integers from data based in the given
     * file. The format of the data in the file is one or more lines of:
     *
     * number number
     *
     * Each line represents an edge in a directed graph. For example, the
     * following line means two vertices, 6 and 7, with an edge connecting 6 to
     * 7:
     *
     * 6 7
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

    private List<Integer> readList(String path) {
        List<Integer> list = new ArrayList<>();
        FileReader fr;
        try {
            fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                int u = Integer.parseInt(split[0]);
                int v = Integer.parseInt(split[1]);
                list.add(u);
                list.add(v);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return list;
    }
}
