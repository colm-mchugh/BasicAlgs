
import dp.tsp;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.BitSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class tspTest {

    @Test
    public void test1() {
        tsp t = new tsp();
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
        tsp t = new tsp();
        t.init(file);
        t.printDistances();
        float ans = t.computeOptTsp();
        System.out.println("tsp=" + ans);
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.CEILING);
        String foo = df.format(ans);
        assert foo.equals("7.89");
    }
}
