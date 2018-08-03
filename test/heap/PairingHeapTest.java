package heap;

import org.junit.Test;
import static org.junit.Assert.*;
import utils.RandGen;

public class PairingHeapTest {
    
    @Test
    public void testPairingHeap() {
        int M = 1000;
        int N = 1000000;
        PairingHeap<Integer> ph = new PairingHeap<>(M+1);
        for (int i : RandGen.uniformSample(N, M)) {
            ph = ph.Insert(i);
        }
        int prev = -1;
        for (int i = 1; i <= M+1; i++) {
            int next = ph.Peek();
            ph = ph.Delete();
            assert prev < next;
            prev = next;
        }
    }
    
}
