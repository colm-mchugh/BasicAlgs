
import dp.tsp;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
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
    
}
