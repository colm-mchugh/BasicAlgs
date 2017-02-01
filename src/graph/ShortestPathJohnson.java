package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ShortestPathJohnson<T> {
    
    // For recording shortest path results. 
    private final Map<WeightedGraph<T>, SPResult<T>> memo = new HashMap<>();
    
    /**
     * The result of a shortest path computation 
     * @param <T> 
     */
    public static class SPResult<T> {
        public T u;
        public T v;
        public int d;

        public SPResult(T u, T v, int d) {
            this.u = u;
            this.v = v;
            this.d = d;
        }
       
    }
    
    public final static SPResult INFINITY = new SPResult(null, null, Integer.MAX_VALUE);
    
    /**
     * Return the shortest path of all the possible paths of the given graph. A 
     * path exists between two vertices u,v of the graph if it is possible
     * to get to v from u.
     * 
     * If the graph has a negative cycle, it is not possible to determine the 
     * shortest path. In this case Integer.MAX_VALUE is used to mean +Infinity.
     * 
     * Otherwise, the graph is re-weighted to contain only positive edges, and
     * Dijkstra's Shortest Path is used to compute the paths between each pair
     * of vertices, keeping track of the shortest path found, which is finally 
     * returned by sp().
     * 
     * The mechanics of Johnson's algorithm for determining the shortest path 
     * are as follows:
     * 
     * @param graph
     * @return 
     */
    public SPResult<T> sp(WeightedGraph<T> graph) {
        if (memo.containsKey(graph)) {
            return memo.get(graph);
        }
        T s = null;
        Collection<T> vertices = new HashSet<>(graph.numVertices());
        for (T v : graph.V()) {
            vertices.add(v);
        }
        for (T v : vertices) {
            graph.link(s, v, 0);
        }
        
        ShortestPathBellmanFord<T> negCycleDtctr = new ShortestPathBellmanFord<>(graph);
        ShortestPathBellmanFord.ShortestPath<T> sp = negCycleDtctr.singleSourceShortestPaths(s);
        if (sp.hasNegativeCycles) {
            return INFINITY;
        }
        Map<T, Integer> sps = sp.weights;
        for (T u : graph.V()) {
            Set<WeightedGraph.Edge<T>> edges = graph.edgesOf(u);
            for (WeightedGraph.Edge<T> edge : edges) {
                int Pu = sps.get(u);
                int Pv = sps.get(edge.v);
                edge.d = (edge.d + Pu - Pv);
            }
        }
        // Finished weighting
        graph.remove(s);
        SPResult<T> minPath = new SPResult<>(null, null, Integer.MAX_VALUE);
        ShortestPathDijkstra<T> sper = new ShortestPathDijkstra<>(graph);
        for (T u : graph.V()) {
            for (T v : graph.V()) {
                if (u.equals(v)) {
                    continue;
                }
                int duv = sper.sp(u, v);
                if (duv == Integer.MAX_VALUE) {
                    continue;
                }
                int Pu = sps.get(u);
                int Pv = sps.get(v);
                if (minPath.d > duv - Pu + Pv) {
                    minPath.d = duv - Pu + Pv;
                    minPath.u = u;
                    minPath.v = v;
                }
            }
        }
        memo.put(graph, minPath);
        return minPath;
    }

}
