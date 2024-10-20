package graph.shortestpath;

import graph.GraphIO;
import graph.WGraphExps;
import graph.WeightedGraph;
import graph.WeightedGraphDirected;
import java.util.List;
import org.junit.Test;

public class FloydWarshallTest {

    @Test
    public void floydWarshall() {
        WeightedGraph<Integer> g = new WeightedGraphDirected<>();
        int[] links = {1, 2, 3, 1, 3, 8, 1, 5, -4, 2, 5, 7, 2, 4, 1, 3, 2, 4, 4, 1, 2, 4, 3, -5, 5, 4, 6};
        GraphIO.populateWeightedGraph(g, links);
        FloydWarshall<Integer> sper = new FloydWarshall<>(g);
        List<Path<Integer>> resList = sper.apsp();
        assert resList.size() == 25;
        for (Path<Integer> res : resList) {
            if (res.u == 1 && res.v == 5) {
                assert res.d == -4;
            }
            if (res.u == 1 && res.v == 3) {
                assert res.d == -3;
            }
        }
    }

    @Test
    public void floydWarshallExt1() {
        WeightedGraph<Integer> g = new WGraphExps<>();
        int[] links = {1, 2, 1, 1, 3, 1, 1, 5, 1, 2, 5, 1, 2, 4, 1, 3, 2, 1, 4, 1, 1, 4, 3, 1, 5, 4, 1};
        GraphIO.populateWeightedGraph(g, links);
        FloydWarshall<Integer> sper = new FloydWarshall<>(g);
        List<Path<Integer>> resList = sper.apsp();
        assert resList.size() == 25;
        for (Path<Integer> res : resList) {
            System.out.println(res);
        }
    }

    @Test
    public void floydWarshallNegCycle() {
        WeightedGraph<Character> g = new WeightedGraphDirected();
        g.link('s', 'a', 1);
        g.link('s', 'c', 1);
        g.link('s', 'e', 1);
        g.link('a', 'b', 1);
        g.link('c', 'd', 1);
        g.link('d', 'c', 1);
        g.link('e', 'f', 1);
        g.link('f', 'e', 1);
        g.link('b', 'g', 1);
        g.link('d', 'g', 1);
        g.link('f', 'g', 1);
        FloydWarshall<Character> sper = new FloydWarshall<>(g);
        List<Path<Character>> resList = sper.apsp();
        for (Path<Character> res : resList) {
            System.out.println(res);
        }
    }

    @Test
    public void bigTest() {
        String file = "resources/g3.txt";
        WeightedGraph<Integer> g = GraphIO.readWeightedGraphDirected(file);
        FloydWarshall<Integer> sper = new FloydWarshall<>(g);
        List<Path<Integer>> paths = sper.apsp();
        int minPath = Integer.MAX_VALUE;
        for (Path<Integer> p : paths) {
            minPath = Integer.min(minPath, p.d);
        }
        assert minPath == -19;
    }

    @Test
    public void bigTest2() {
        String file = "resources/g1.txt";
        WeightedGraph<Integer> g = GraphIO.readWeightedGraphDirected(file);
        FloydWarshall<Integer> sper = new FloydWarshall<>(g);
        assert sper.hasNegativeCostCycle;
    }

    @Test
    public void bigTest3() {
        String file = "resources/g_44_2048.txt";
        WeightedGraph<Integer> g = GraphIO.readWeightedGraphDirected(file);
        FloydWarshall<Integer> sper = new FloydWarshall<>(g);
        List<Path<Integer>> paths = sper.apsp();
        int minPath = Integer.MAX_VALUE;
        for (Path<Integer> p : paths) {
            minPath = Integer.min(minPath, p.d);
        }
        assert minPath == -3127;
    }

    @Test
    public void multiTest() {
        String[] files = {"input_random_11_8.txt", "input_random_18_32.txt",
            "input_random_25_128.txt", "input_random_30_256.txt",
            "input_random_35_512.txt", "input_random_39_1024.txt"
        };
        int[] expected = {-89, -355, Integer.MAX_VALUE, -961, -1722, -2123};
        for (int i = 0; i < files.length; i++) {
            WeightedGraph<Integer> g = GraphIO.readWeightedGraphDirected("resources/" + files[i]);
            FloydWarshall<Integer> sper = new FloydWarshall<>(g);
            List<Path<Integer>> paths = sper.apsp();
            if (expected[i] == Integer.MAX_VALUE) {
                assert sper.hasNegativeCostCycle;
                continue;
            }
            int min = Integer.MAX_VALUE;
            for (Path<Integer> p : paths) {
                min = Integer.min(min, p.d);
            }
            System.out.println("min=" + min + ", expected=" + expected[i]);
            assert min == expected[i];
        }
    }
}
