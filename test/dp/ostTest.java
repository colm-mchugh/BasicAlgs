package dp;

import dp.OptmlSrchTree;
import org.junit.Test;


public class ostTest {
    
    @Test
    public void testOST() {
        float[] freq = {0.2f, 0.05f, 0.17f, 0.1f, 0.2f, 0.03f, 0.25f};
        int[] keys = {1, 2, 3, 4, 5, 6, 7};
        float min = OptmlSrchTree.optimalSearchTree(keys, freq, keys.length);
        assert min == 2.23f;
    }
}
