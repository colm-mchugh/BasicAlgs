package graph.shortestpath;

import graph.WeightedGraph;
import heap.MinHeap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Dijkstra<T> {
    
    private WeightedGraph<T> G;

    public Dijkstra(WeightedGraph<T> graph) {
        this.G = graph;
    }

    public WeightedGraph<T> getGraph() {
        return G;
    }

    public void setGraph(WeightedGraph<T> graph) {
        this.G = graph;
    }
    
    /**
     * The length of the shortest path between s and t using Dijkstra's algorithm.
     * 
     * Goal: find the shortest path from s to t in a directed graph G.
     * Algorithm: X = { s }, D[s] = 0, D[w] = +Inf all w E G, w != s
     *      X is the set of vertices that have been processed 
     *      D gives the shortest distance from s to v when v E X, +Inf when v !E X
     * While s != t
     *      Find (v, w) with Min(D[v] + l(v, w)) for all (v, w) E G
     *          where v E X AND w E G - X.
     *      X = X + { w }
     *      D[w] = D[v] + l(v, w)
     * 
     * For a graph with N vertices and M edges, a naive implementation takes O(N*M)
     * because for each vertex potentially all of the edges need to be explored 
     * to determine the minimum distance edge between X and G - X.
     * 
     * This implementation uses a minimum priority queue and takes O(M + N*log(N)),
     * because it is a graph traversal that performs a constant number of priority
     * queue operations on each vertex. 
     * 
     * @param s start vertex
     * @param t end vertex
     * @return 
     */
    public Path<T> sp(T s, T t) {
        // 'X' is the set of vertices that have been processed; initialy empty
        Set<T> X = new HashSet<>();
        // shortest[v] gives the shortest distance from s to a vertex v. 
        Map<T, Integer> shortest = new HashMap<>();
        // 'path' is the chain of vertices from s to t that makes up the shortest path. 
        Path<T> path = new Path<>(s, t, Integer.MAX_VALUE);
        path.path = new ArrayList<>();
        // 'gMinusX' contains an entry for all vertices in G but not in X.
        MinHeap<WeightedGraph.Edge<T>> gMinusX = new MinHeap<>();
        for (T v : G.V()) {
            int weight = (!v.equals(s) ? Integer.MAX_VALUE : 0);
            gMinusX.Insert(new WeightedGraph.Edge<>(v, weight));
        }
        // At this point gMinusX[s] == 0, gMinusX[v] = +Inf fora all v E G and v != s
        
        while (!s.equals(t)) {
            // Get the vertex s with the lowest value between X and gMinusX
            WeightedGraph.Edge<T> w = gMinusX.Delete();
            if (w.d == Integer.MAX_VALUE) { // No path from u to v ?
                return path; // path length is Integer.MAX_VALUE, meaning +Infinity
            }
            // The weight value gives the distance from source to the vertex
            shortest.put(w.v, w.d);    
            // Put the vertex on the shortest path
            path.path.add(s);
            // Move the vertex into X
            moveFromHeapToX(gMinusX, X, w.v, shortest);
            // Continue from this vertex
            s = w.v;
        }
        // We arrived at a vertex that equals t, the target. 
        // Add t as the final link in the path and set the 
        // shortest path value
        path.path.add(t);
        path.d = shortest.get(s);
        return path;
    }
    
    /**
     * moveFromHeapToX(): Put the vertex w in X and re-calculate  
     * heap values for vertices that are not in X but share an 
     * edge with w.
     * 
     * @param heap
     * @param X
     * @param w
     * @param d 
     */
    private void moveFromHeapToX(MinHeap<WeightedGraph.Edge<T>> heap, Set<T> X, T w, Map<T, Integer> d) {
        // X gets a new element, the vetex w
        X.add(w);
        // The frontier between X and G - X has changed. Because w is in X, all
        // vertices v of edges (w,v), where v !E X, need to have their weighting
        // recomputed. This is done by removing them from the heap, and re-entering 
        // with a weigthing set to the minimum of: current weighting OR shortest 
        // distance from s to w + l(w,v)
        for (WeightedGraph.Edge<T> e : G.edgesOf(w)) {
            if (!X.contains(e.v)) {
                WeightedGraph.Edge<T> heapV = heap.DeleteSpecificKey(e);
                heapV.d = Integer.min(heapV.d, d.get(w) + e.d);
                heap.Insert(heapV);
            }
        }
    }
}
