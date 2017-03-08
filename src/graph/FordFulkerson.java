package graph;

import graph.Flow.Edge;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FordFulkerson<T> {

    private final Set<T> mincut;
    private int value;
    
    public FordFulkerson(Flow<T> g, T s, T t) {
        assert g.V().contains(s) && g.V().contains(t);
        assert !s.equals(t);
        mincut = new HashSet<>(g.numVertices());
        value = g.excess(t);
        Map<T, Edge<T>> path = new HashMap<>(g.numVertices());
        while (getAugmentingPath(g, s, t, path)) {
            int minDelta = Integer.MAX_VALUE;
            for (T v = t; !v.equals(s); v = path.get(v).other(v)) {
                minDelta = Integer.max(minDelta, path.get(v).residualCapacity(v));
            }
            for (T v = t; v != s; v = path.get(v).other(v)) {
                path.get(v).addResidualFlow(v, minDelta);
            }
            value += minDelta;
        }
        assert mincut.contains(s) && !mincut.contains(t);
                
    }
    
    private boolean getAugmentingPath(Flow<T> g, T s, T t, Map<T, Edge<T>> pathTo) {
        mincut.clear();       
        Queue<T> q = new ArrayDeque<>();
        q.add(s);
        mincut.add(s);
        while (!q.isEmpty() && !mincut.contains(t)) {
            T u = q.remove();
            for (Edge<T> e : g.edgesOf(u)) {
                T v = e.other(u);
                if (e.residualCapacity(v) > 0 && !mincut.contains(v)) {
                    pathTo.put(v, e);
                    mincut.add(v);
                    q.add(v);
                }
            }
        }
        return mincut.contains(t);
    }

    public Set<T> getMincut() {
        return mincut;
    }

    public int getValue() {
        return value;
    }
    
}
