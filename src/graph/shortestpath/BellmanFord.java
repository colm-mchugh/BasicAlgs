package graph.shortestpath;

import graph.WeightedGraph;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BellmanFord<T> {
    
    private final Map<WeightedGraph<T>, Map<T, ShortestPath<T>>> memo = new HashMap<>();
    
    public static class ShortestPath<T> {

        T source;
        boolean hasNegativeCycles;
        Map<T, Integer> weights;
        Map<T, T> preds;

        public ShortestPath(T source, int size) {
            this.source = source;
            this.hasNegativeCycles = false;
            this.weights = new HashMap<>(size);
            this.preds = new HashMap<>(size);
        }
    }
    
    public ShortestPath<T> singleSourceShortestPaths(WeightedGraph<T> graph, T s) {
        Map<T, ShortestPath<T>> rcrd = this.memo.get(graph);
        if (rcrd != null && rcrd.containsKey(s)) {
            return rcrd.get(s);
        }
        if (graph.edgesOf(s) == null) {
            throw new IllegalArgumentException("No such vertex: " + s);
        }
        ShortestPath<T> rv = new ShortestPath<>(s, graph.numVertices());
        for (T v : graph.V()) {
            rv.weights.put(v, Integer.MAX_VALUE);
            rv.preds.put(v, null);
        }
        rv.weights.put(s, 0);
        boolean shortCircuit = true;
        for (int i = 0; i < graph.numVertices(); i++) {
            shortCircuit = true;
            for (T u : graph.V()) {
                Set<WeightedGraph.Edge<T>> edges = graph.edgesOf(u);
                for (WeightedGraph.Edge<T> edge : edges) {
                    int newW = rv.weights.get(u);
                    if (newW == Integer.MAX_VALUE) {
                        continue;
                    }
                    newW += edge.d;
                    if (rv.weights.get(edge.v) > newW) {
                        rv.weights.put(edge.v, newW);
                        rv.preds.put(edge.v, u);
                        shortCircuit = false;
                    }
                }
            }
        }
        if (!shortCircuit) {
            for (T u : graph.V()) {
                Set<WeightedGraph.Edge<T>> edges = graph.edgesOf(u);
                for (WeightedGraph.Edge<T> edge : edges) {
                    int newW = rv.weights.get(u) + edge.d;
                    if (rv.weights.get(edge.v) > newW) {
                        rv.hasNegativeCycles = true;
                    }
                }
            }
        }
        if (rcrd == null) {
            rcrd = new HashMap<>();
            memo.put(graph, rcrd);
        }
        rcrd.put(s, rv);
        return rv;
    }
}
