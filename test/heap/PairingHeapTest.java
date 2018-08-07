package heap;

import org.junit.Test;
import utils.RandGen;

public class PairingHeapTest {

    @Test
    public void testPairingHeap() {
        int M = 10000;
        int N = 10000000;
        PairingHeap<Integer> ph = new PairingHeap<>();
        for (int i : RandGen.uniformSample(N, M)) {
            ph = ph.Insert(i);
        }
        int prev = Integer.MIN_VALUE;
        for (int i = 1; i <= M; i++) {
            int next = ph.Min();
            ph = ph.Delete();
            assert prev < next;
            prev = next;
        }
        System.out.println("#heaps = " + PairingHeap.nheaps);
        System.out.println("#merges = " + PairingHeap.nmerges);
        System.out.println("#pairings = " + PairingHeap.npairings);
        System.out.println("#max_children = " + PairingHeap.max_children);
    }

    @Test
    public void testPairingHeapSmall() {
        int[] data = { 10, 7, 11, 4, 2, 13, 8, 3, 5, 17, 15, 1, 6, 19, 12, 9, 14 };
        
        PairingHeap<Integer> ph = new PairingHeap<>();
        for (int i : data) {
            ph = ph.Insert(i);
        }
        
        ph.Print();
        
        int prev = -1;
        for (int i = 1; i <= data.length; i++) {
            int next = ph.Min();
            ph = ph.Delete();
            assert prev < next;
            prev = next;
        }
        System.out.println("#heaps = " + PairingHeap.nheaps);
        System.out.println("#merges = " + PairingHeap.nmerges);
        System.out.println("#pairings = " + PairingHeap.npairings);
        System.out.println("#max_children = " + PairingHeap.max_children);
    }
}
