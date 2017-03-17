package string;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class PermsMatchTest {
    
    
    @Test
    public void testPermutationsOf() {
        String p = "AABA";
        String t = "AABAACAADAABAABA";
        PermsMatch instance = new PermsMatch();
        List<Integer> e = new ArrayList<>(3);
        e.add(0);
        e.add(1);
        e.add(9);
        e.add(10);
        e.add(12);
        List<Integer> result = instance.permutationsOf(p, t);
        assertEquals(e, result);
    }
}
