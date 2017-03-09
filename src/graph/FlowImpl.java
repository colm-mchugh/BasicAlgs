package graph;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FlowImpl<T>  implements Flow<T> {

    private final Map<T, Set<Edge<T>>> graph;

    public FlowImpl() {
        graph = new HashMap<>();
    }
    
    @Override
    public void link(T u, T v, int c) {
        Edge<T> newEdge = new Edge<>(u, v, c);
        addToGraph(u, newEdge);
        addToGraph(v, newEdge);
    }

    @Override
    public Set<T> V() {
        return graph.keySet();
    }

    @Override
    public int numVertices() {
        return graph.keySet().size();
    }

    private void addToGraph(T v, Edge<T> e) {
        if (!graph.containsKey(v)) {
            graph.put(v, new HashSet<>());
        }
        Set<Edge<T>> vEdges = graph.get(v);
        vEdges.add(e);
    }

    @Override
    public Set<Edge<T>> edgesOf(T u) {
        return this.graph.get(u);
    }

    @Override
    public int excess(T w) {
        assert this.graph.keySet().contains(w);       
        int xs = 0;
        for (Edge<T> e : this.graph.get(w)) {
            if (w.equals(e.u)) {
                xs -= e.flow;
            } else {
                xs += e.flow;
            }
        }
        return xs;
    }

    /**
     * Determine the maximum flow possible from source vertex s to  
     * @param s
     * @param t 
     */
    @Override
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
