package graph.shortestpath;

import graph.WeightedGraph;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import utils.math;

public class Johnson<T> {
    
    // For recording shortest path results. 
    private final Map<WeightedGraph<T>, Path<T>> memo = new HashMap<>();
       
    
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
     * returned.
     * 
     * The mechanics of Johnson's algorithm for determining the shortest path 
     * are as follows:
     * 
     * @param graph
     * @return 
     */
    public Path<T> sp(WeightedGraph<T> graph) {
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
        
        BellmanFord<T> negCycleDtctr = new BellmanFord<>();
        BellmanFord.ShortestPath<T> sp = negCycleDtctr.singleSourceShortestPaths(graph, s);
        if (sp.hasNegativeCycles) {
            return Path.INFINITY;
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
        Path<T> minPath = new Path<>(null, null, Integer.MAX_VALUE);
        Dijkstra<T> sper = new Dijkstra<>(graph);
        int N = graph.numVertices();
        for (T u : graph.V()) {
            int n = 0;
            for (T v : graph.V()) {
                n++;
                if (math.asPercentage(n, N)%10 == 0) {
                    System.out.print(math.asPercentage(n, N)%10 +"%..");
                }
                if (u.equals(v)) {
                    continue;
                }
                int duv = sper.sp(u, v).d;
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
        System.out.println();
        memo.put(graph, minPath);
        return minPath;
    }

}
