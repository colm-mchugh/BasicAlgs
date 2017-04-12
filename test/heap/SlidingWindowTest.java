package heap;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class SlidingWindowTest {
    
    @Test
    public void testMaxWindowVals() {
        int[] data = {1, 3, -1, -3, 5, 3, 6, 7};
        int[] win = {3,3,5,5,6,7};
        List<Integer> m = SlidingWindow.maxWindowVals(makeList(data), 3);
        assert win.length == m.size();
        for (int i = 0; i < win.length; i++) {
            assert win[i] == m.get(i);
        }
    }
    
    
    private List<Integer> makeList(int[] data) {
        List<Integer> rv = new ArrayList<>(data.length);
        for (int d : data) {
            rv.add(d);
        }
        return rv;
    }
    
}
