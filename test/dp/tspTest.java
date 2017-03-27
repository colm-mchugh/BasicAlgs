package dp;

import graph.GraphIO;
import graph.WeightedGraph;
import graph.WeightedGraphUndirected;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class tspTest {

    private final static boolean DOPRUNE = true;
    
    @Test
    public void test1() {
        TSPer t = new TSPer(DOPRUNE);
        // Test that subsets are correctly generated
        int N = 5;
        
        Map<BitSet, Map<Short, Float>> Abs = t.genBitSet(N);
        Set<BitSet> bitsets = Abs.keySet();
        for (BitSet b : bitsets) {
            System.out.println("Next set: " + b + " cardinality=" + b.cardinality());
        }
        assert bitsets.size() == 1 << (N - 1);
        
        Map<Integer, Map<Short, Float>> Aopt = t.genOptBitSet(N);
        Set<Integer> bss = Aopt.keySet();
        for (Integer bs : bss) {
            System.out.print("Next set: { ");
            for (Integer k = t.nextEl(bs, 0); k >= 0; k = t.nextEl(bs, k+1)) {
                System.out.print(k);
                System.out.print(' ');
            }
            System.out.print(" } cardinality=" + t.card(bs));
            System.out.println();
        }
        assert bss.size() == 1 << (N - 1);
    }

    @Test
    public void testTsp1() {
        String file = "resources/tsp_small.txt";
        TSPer t = new TSPer(this.init(file), !DOPRUNE);
        t.printDistances();
        float ans = t.computeOptTsp();
        System.out.println("tsp=" + ans);
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.CEILING);
        String foo = df.format(ans);
        assert foo.equals("7.89");
    }
    
    @Test
    public void testSetSz() {
        TSPer t = new TSPer(DOPRUNE);
        assert (t.setSize(0) == 0);
        assert (t.setSize(8) == 1);
        assert (t.setSize(15) == 4);
        assert (t.setSize(32) == 1);
        assert (t.setSize(31) == 5);
        int N = 8;
        int k = 2;
        Map<BitSet, Map<Short, Float>> rv = new HashMap<>();
        t.addSets(rv, N, k);
        
        Set<BitSet> bitsets = rv.keySet();
        for (BitSet b : bitsets) {
            System.out.println("Next set: " + b + " cardinality=" + b.cardinality());
        }
    }
    
    @Test
    public void testBig() {
        String file = "resources/tsp.txt";
        TSPer t = new TSPer(this.init(file), DOPRUNE);
        long now = System.currentTimeMillis();
        float ans = t.computeOptTsp();
        System.out.println(ans + " in " + (System.currentTimeMillis() - now)/1000 + " secs");
        assert Math.floor(ans) == 26442.0;
        
        ans = t.computeTsp();
        System.out.println(ans + " in " + (System.currentTimeMillis() - now)/1000 + " secs");
        assert Math.floor(ans) == 26442.0;
        
    }
    
    @Test
    public void testGraph1() {
        WeightedGraph<Integer> g = new WeightedGraphUndirected<>();
        int[] links = {1,2,20, 1,3,42, 1,4,35, 2,3,30, 2,4,34, 3,4,12};
        int[] p = {1,4,3,2};
        GraphIO.populateGraph(g, links);
        TSPer t = new TSPer(g, !DOPRUNE);       
        float ans1 = t.computeTsp();
        List<Integer> path = t.getShortestPath();
        for (int i = 0; i < p.length; i++) {
            assert p[i] == path.get(i);
        }
        float ans2 = t.computeOptTsp();       
        path = t.getShortestPath();
        for (int i = 0; i < p.length; i++) {
            assert p[i] == path.get(i);
        }
        assert ans1 == ans2 && ans1 == 97;
    }
    
    @Test
    public void testGraph2() {
        WeightedGraph<Integer> g = new WeightedGraphUndirected<>();
        int[] links = {1,2,1, 1,3,10, 2,3,10, 2,4,10, 2,5,10, 3,5,10, 4,5,1};
        int[] p = {1,3,5,4,2};
        GraphIO.populateGraph(g, links);
        TSPer t = new TSPer(g, !DOPRUNE);       
        float ans1 = t.computeTsp();
        List<Integer> path = t.getShortestPath();
        for (int i = 0; i < p.length; i++) {
            assert p[i] == path.get(i);
        }
        float ans2 = t.computeOptTsp();       
        path = t.getShortestPath();
        for (int i = 0; i < p.length; i++) {
            assert p[i] == path.get(i);
        }
        assert ans1 == ans2 && ans1 == 32;
    }
    
    public Float[] init(String file) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int N = Integer.parseInt(line);
            Float[] theData = new Float[N * 2];
            Short c = 0;
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                theData[c++] = Float.parseFloat(split[0]);
                theData[c++] = Float.parseFloat(split[1]);
            }
            return theData;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    
}
