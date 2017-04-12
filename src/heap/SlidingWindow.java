package heap;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a list of numbers l and a window size w, return a list of the maximum
 * numbers in each window of l. A window in l is a slice i..i+w-1 of l.
 * Given [1 3 -1 -3 5 3 6 7] and window size 3, return [3 3 5 5 6 7]
 * 
 * Uses a heap instance to keep track of maximum elements in each window.
 * Space overhead: 3*w (heap object)
 * Time overhead: N*log(w) (heap delete)
 * 
 */
public class SlidingWindow {
    
    public static  List<Integer> maxWindowVals(List<Integer> l, int w) {
        int N = l.size();
        List<Integer> windowMax = new ArrayList<>(N/w);
        if (w > N) {
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < N; i++) {
                if (l.get(i) > max) {
                    max = l.get(i);
                }
            }
            windowMax.add(max);
        } else {
            Heap<Integer> windowEls = new MaxHeap<>();
            for (int i = 0; i < w; i++) {
                windowEls.Insert(l.get(i));
            }
            windowMax.add(windowEls.Peek());
            for (int i = 1; i <= N - w; i++) {
                windowEls.DeleteSpecificKey(l.get(i - 1));
                windowEls.Insert(l.get(i + w - 1));
                windowMax.add(windowEls.Peek());
            }
        }
        return windowMax;
    }

}
