package graph.shortestpath;

import graph.WeightedGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import utils.RandGen;

/**
 * Compute the shortest paths between all points of a weighted graph.
 * 
 * Input: a weighted graph G(V, E) that may or may not have a negative cost cycle.
 * 
 * Uses Johnson's algorithm. The key idea is, if the graph does not have a -ve
 * cost cycle, then re-weight each edge to be positive (if not already so), such
 * that all paths between two vertices are increased by the same amount.
 * 
 * Given an edge (u, v) with weight Wuv, each vertex i is assigned a weighting Pi
 * such that Wuv' = Wuv + Pu - Pv.
 * Thus, for any path s-t of G of length L, L' = L + Ps - Pt
 * (because all intermediate weightings Pi cancel out).
 * 
 * The value Pv for each vertex V of G is calculated by introducing a new vertex S
 * and adding edges S,V of length 0 to G for all V, and then calculating the shortest path
 * from S to each V by running the Bellman-Ford algorithm. The key observation is
 * that a shortest S-V path can be at most 0, and may be negative if there is a vertex
 * U such that the edge U,V is negative (or more generally there is a negative length 
 * path from U to V).
 * 
 * If the graph does not contain a -ve cost cycle, then each edge U,V is re-weighted
 * with the value: Wuv + Pu - Pv. This has to be positive, because |Pv| >= |Pu|
 * Now the graph contains no negative edges, so Dijkstra's shortest path algorithm
 * can be run for each vertex pair.
 * 
 * Running time: O(N)       for adding vertex S
 *             + O(MN)      1 Bellman-Ford 
 *             + O(M)       edge adjustments
 *             + O(NMlogN)  N Dijkstra shortest path
 *             + O(N^2)     to re-calculate each (u,v) distance
 * ~ O(NMlogN)
 * 
 * @param <T> 
 */
public class Johnson<T> {

    public Set<Path<T>> apsp(WeightedGraph<T> G) {
        Set<Path<T>> rv = new HashSet<>();
        // Add edges from vertex s to all other vertexes of G with weight 0
        T s = null;
        List<T> vertices = new ArrayList<>(G.numVertices());
        vertices.addAll((Collection<? extends T>) G.V()); // necessary to prevent concurrent modification 
        assert vertices.size() == G.numVertices();
        for (T v : vertices) {
            G.link(s, v, 0);
        }
        // Run Bellman-Ford algorithm on G with s as the source vertex
        BellmanFord<T> negCycleDtctr = new BellmanFord<>();
        BellmanFord.ShortestPath<T> sp = negCycleDtctr.singleSourceShortestPaths(G, s);
        if (sp.hasNegativeCycles) {
            // Identified a negative cycle
            rv.add(Path.INFINITY);
            return rv;
        }
        // Apply the reweighting
        Map<T, Integer> reweightings = sp.weights;
        for (T u : G.V()) {
            Set<WeightedGraph.Edge<T>> edges = G.edgesOf(u);
            for (WeightedGraph.Edge<T> edge : edges) {
                int Pu = reweightings.get(u);
                int Pv = reweightings.get(edge.v);
                edge.d = (edge.d + Pu - Pv);
            }
        }
        // Finished re-weighting, now the graph has no negative edges.
        // Run Dijkstra shortest path on each u,v pair
        G.remove(s);
        Dijkstra<T> sper = new Dijkstra<>(G);
        Path<T> tmp = new Path<>(null, null, Integer.MAX_VALUE);
        for (T u : vertices) {
            for (int i = vertices.size() - 1; i >= 0; i--) {
                T v = vertices.get(i);
                if (u.equals(v)) {
                    continue;
                }
                if (rv.contains(tmp.set(u, v))) {
                    continue;
                }
                Map<T, Integer> shortestPaths = sper.sp(u, v);
                for (T w : shortestPaths.keySet()) {
                    Path<T> p = new Path<>(u, w, shortestPaths.get(w));
                    // Readjust to get the real distance
                    if (p.d != Integer.MAX_VALUE) {
                        p.d = p.d - reweightings.get(u) + reweightings.get(w);
                    }
                    rv.add(p);
                }
            }
        }
        return rv;
    }

}
