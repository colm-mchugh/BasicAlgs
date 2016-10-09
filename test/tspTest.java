
import dp.tsp;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class tspTest {

    @Test
    public void test1() {
        tsp t = new tsp();
        // Test that subsets are correctly generated
        int N = 4;
        Map<Set<Short>, Map<Short, Float>> A = t.genSet(N);
        Set<Set<Short>> sets = A.keySet();
        for (Set<Short> set : sets) {
            System.out.println("Next set: " + set);
        }
        assert sets.size() == 1 << (N - 1);
    }
    
    @Test
    public void testTsp1() {
        String file = "resources/tsp_small.txt";
        tsp t = new tsp();
        t.init(file);
        t.printDistances();
        float ans = t.computeTsp();
        System.out.println("tsp=" + ans);
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.CEILING);
        String foo = df.format(ans);
        assert foo.equals("7.89");
    }
}
