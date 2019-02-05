package graph;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class FlowEdmndKarp<T>  extends Flow<T> {    

    /**
     * Determine the maximum flow possible from source vertex s to sink t.
     * Uses Ford-Fulkerson algorithm (*) as follows: while there is an 
     * augmenting path from source to sink - a path along which flow can be
     * increased - increase the flow along that path by the maximal allowed.
     * Stop when no more paths can be found
     * 
     * (*) CLRS ch.26, KT ch. 7, DPV 7.1-7.3, among others.
     * @param s
     * @param t 
     */
    public Max<T> getMax(T s, T t) {
        Max<T> mf = new Max<>(s, t, this);
        Map<T, Edge<T>> path = getAugmentingPath(mf);
        while (!path.isEmpty()) {              
            int minDelta = Integer.MAX_VALUE;
            for (T v = t; !v.equals(s); v = path.get(v).other(v)) {
                minDelta = Integer.min(minDelta, path.get(v).residualCapacity(v));
            }
            for (T v = t; v != s; v = path.get(v).other(v)) {
                path.get(v).addResidualFlow(v, minDelta);
            }
            mf.value += minDelta;
            path = getAugmentingPath(mf);
        }
        assert mf.mincut.contains(s) && !mf.mincut.contains(t);
        return mf;
    }
    
    /**
     * Find an augmenting path in the graph that fits the given maximum flow;
     * the given maximum flow specifies the source and sink vertices. BFS is
     * used per Edmonds-Karp to obtain the shortest possible path (if it exists)
     * and avoid pathological cases like the 'flip-flop' behaviour on p.728
     * of CLRS. 
     * 
     * @param g
     * @param mf
     * @return 
     */
    private Map<T, Edge<T>> getAugmentingPath(Max<T> mf) {
        Map<T, Edge<T>> pathTo = new HashMap<>(this.numVertices());
        mf.mincut.clear();
        Queue<T> q = new ArrayDeque<>();
        q.add(mf.source);
        mf.mincut.add(mf.source);
        while (!q.isEmpty() && !mf.mincut.contains(mf.sink)) {
            T u = q.remove();
            for (Edge<T> e : this.edgesOf(u)) {
                T v = e.other(u);
                if (!mf.mincut.contains(v) && e.residualCapacity(v) > 0) {
                    pathTo.put(v, e);
                    mf.mincut.add(v);
                    q.add(v);
                }
            }
        }
        if (!mf.mincut.contains(mf.sink)) {
            pathTo.clear();
        }
        mf.augmentations++;
        return pathTo;
    }

    
}
