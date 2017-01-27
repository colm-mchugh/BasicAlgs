package graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShortestPathJohnson<T> {
    
    private WeightedGraph<T> graph;

    public ShortestPathJohnson(WeightedGraph<T> graph) {
        this.graph = graph;
    }

    public WeightedGraph<T> getGraph() {
        return graph;
    }

    public void setGraph(WeightedGraph<T> graph) {
        this.graph = graph;
    }
    
    /**
     * Return the shortest path between all the pairs of the graph.
     * 
     * If the graph has a negative cycle, it is not possible to return the 
     * shortest path so Integer.MAX_VALUE really means +Inf.
     * 
     * Otherwise, the graph is re-weighted to contain no negative edge, and
     * Dijkstra's Shortest Path is used to compute the paths between each pair
     * of vertices, keeping track of the shortest path found, which is then 
     * returned as the result.
     * 
     * @return 
     */
    public int sp() {
        T s = null;
        Collection<T> vertices = new HashSet<T>(graph.numVertices());
        for (T v : graph.V()) {
            vertices.add(v);
        }
        for (T v : vertices) {
            graph.link(s, v, 0);
        }
        WeightedGraphDirected.SingleSourceResult<T> singleSourceSP = graph.singleSourceShortestPaths(s);
        if (singleSourceSP.hasNegativeCycles) {
            return Integer.MAX_VALUE;
        }
        Map<T, Integer> sps = singleSourceSP.weights;
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
        int minPath = Integer.MAX_VALUE;
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
                if (minPath > duv - Pu + Pv) {
                    minPath = duv - Pu + Pv;
                }
            }
        }
        return minPath;
    }

}
