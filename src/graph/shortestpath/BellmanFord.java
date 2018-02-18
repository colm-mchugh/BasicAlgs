package graph.shortestpath;

import graph.WeightedGraph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Bellman Ford Algorithm.
 *
 * Given a directed graph G(V, E) that may have -ve edges and source vertex s,
 * give one of: 
 *      - Shortest path from s to all other vertices (each path has at most n-1 edges (no cycles)) 
 *      - Assert that G has a -ve cost cycle
 *
 * The algorithm uses a dynamic programming paradigm with the key idea to
 * restrict the number of edges in a path in order to build up sub-problems.
 *
 * For i in 1..(N-1), v E V, let P = shortest s-v path with at most i edges.
 * Either: 
 *  1. P has at most (i - 1) edges 
 *  2. P has i edges with last edge = (w, v). Then P’ is a shortest s-w path with at most (i - 1) edges
 *
 * A[ i , v ] = length of shortest s-v path with at most i edges. 
 *  - cycles allowed 
 *  - +Infinity if there is no s-v path with i edges
 *
 * Recurrence: A[ i, V ] = MIN ( A [ i - 1, V ], MIN ( A[ i - 1, W ] + Cwv ) for all (w,v) in E) )
 *
 * A[ 0, s ] = 0, A [ 0, v ] = +Infinity for all v E V, v != s
 * For i in 1 .. N - 1 // Solve in order of increasing problem size 
 *  For each vertex V A[ i, V ] = MIN ( A [ i - 1, V ], MIN ( A[ i - 1, W ] + Cwv ) for  all (w,v) E G) )
 *
 * Running time: O(MN). In a dense graph, tends to O(N^3), because M ~ N^2
 *
 * Early out: If A[ j - 1, V ] = A[ j, V ] for some j in 1..(N-1) and all V,
 * then can stop. Cycle detection: If A [ N, V] = A [ N - 1, V] for all V, then
 * there are no cycles. Because No path decreased on this iteration.
 *
 * @param <T>
 */
public class BellmanFord<T> {

    private final Map<WeightedGraph<T>, Map<T, ShortestPath<T>>> savedShortestPaths = new HashMap<>();

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
        Map<T, ShortestPath<T>> rcrd = this.savedShortestPaths.get(graph);
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
        boolean earlyOut = false;
        for (int i = 0; i < graph.numVertices() && !earlyOut; i++) {
            earlyOut = true;
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
                        earlyOut = false;
                    }
                }
            }
        }
        if (!earlyOut) {
            for (T u : graph.V()) {
                Set<WeightedGraph.Edge<T>> edges = graph.edgesOf(u);
                for (WeightedGraph.Edge<T> edge : edges) {
                    int newW = rv.weights.get(u) + edge.d;
                    if (rv.weights.get(edge.v) > newW) {
                        rv.hasNegativeCycles = true;
                    }
                }
            }
        } else {
            rv.hasNegativeCycles = false;
        }
        if (rcrd == null) {
            rcrd = new HashMap<>();
            savedShortestPaths.put(graph, rcrd);
        }
        rcrd.put(s, rv);
        return rv;
    }
}
