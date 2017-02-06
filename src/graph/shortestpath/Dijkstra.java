package graph.shortestpath;

import graph.WeightedGraph;
import heap.MinHeap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Dijkstra<T> {
    
    private WeightedGraph<T> graph;

    public Dijkstra(WeightedGraph<T> graph) {
        this.graph = graph;
    }

    public WeightedGraph<T> getGraph() {
        return graph;
    }

    public void setGraph(WeightedGraph<T> graph) {
        this.graph = graph;
    }
    
    /**
     * The length of the shortest path between u and v
     * @param u
     * @param v
     * @return 
     */
    public Path<T> sp(T u, T v) {
        T s = u;
        Set<T> X = new HashSet<>();
        Map<T, Integer> d = new HashMap<>();
        Path<T> rv = new Path<>(u, v, Integer.MAX_VALUE);
        rv.path = new ArrayList<>();
        d.put(s, 0);
        MinHeap<WeightedGraph.Edge<T>> heap = new MinHeap<>();
        for (T t : graph.V()) {
            if (!t.equals(u)) {
                heap.Insert(new WeightedGraph.Edge<>(t, Integer.MAX_VALUE));
            }
        }
        X.add(u);
        rv.path.add(u);
        computeKeys(heap, X, u, d);
        while (!s.equals(v)) {
            WeightedGraph.Edge<T> w = heap.Delete();
            if (w.d == Integer.MAX_VALUE) { // No path from u to v ?
                return rv; // path length is Integer.MAX_VALUE, meaning +Infinity
            }
            d.put(w.v, w.d);
            s = w.v;
            rv.path.add(s);
            X.add(w.v);
            computeKeys(heap, X, w.v, d);
        }
        rv.path.add(v);
        rv.d = d.get(s);
        return rv;
    }
    
    private void computeKeys(MinHeap<WeightedGraph.Edge<T>> heap, Set<T> X, T w, Map<T, Integer> d) {
        for (WeightedGraph.Edge<T> e : graph.edgesOf(w)) {
            if (!X.contains(e.v)) {
                WeightedGraph.Edge<T> heapV = heap.DeleteSpecificKey(e);
                heapV.d = Integer.min(heapV.d, d.get(w) + e.d);
                heap.Insert(heapV);
            }
        }
    }
}
