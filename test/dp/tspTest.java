package dp;


import dp.TSPer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class tspTest {

    @Test
    public void test1() {
        TSPer t = new TSPer();
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
        TSPer t = new TSPer(this.init(file));
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
        TSPer t = new TSPer();
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
        //assert bitsets.size() == Math.pow(N, k);
    }
    
    @Test
    public void testBig() {
        String file = "resources/tsp.txt";
        TSPer t = new TSPer(this.init(file));
        float ans = t.computeTsp();
        System.out.println(ans);
        assert ans == 26442.0;
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
