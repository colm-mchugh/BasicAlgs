package graph.shortestpath;

import graph.WeightedGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FloydWarshall<T> {
    
    // For recording shortest path results. 
    private final Map<WeightedGraph<T>, List<Path<T>>> memo = new HashMap<>();
    
    public List<Path<T>> sp(WeightedGraph<T> graph) {
        if (this.memo.containsKey(graph)) {
            return this.memo.get(graph);
        }
        List<T> vertices = new ArrayList<>(graph.numVertices());
        for (T v : graph.V()) {
            vertices.add(v);
        }
        int n = graph.numVertices();

        Map<Integer, Map<Integer, Map<Integer, Integer>>> record = new HashMap<>(n);
        for (int i = 1; i <= n; i++) {
            if (record.get(i) == null) {
                record.put(i, new HashMap<>());
            }
            Map<Integer, Map<Integer, Integer>> iDim = record.get(i);
            for (int j = 1; j <= n; j++) {
                if (iDim.get(j) == null) {
                    iDim.put(j, new HashMap<>());
                }
                Map<Integer, Integer> jDim = iDim.get(j);
                if (i == j) {
                    jDim.put(0, 0);
                } else {
                    T u = vertices.get(i - 1);
                    T v = vertices.get(j - 1);
                    WeightedGraph.Edge<T> edge = this.edgeTo(graph, u, v);
                    if (edge != null) {
                        jDim.put(0, edge.d);
                    } else {
                        jDim.put(0, Integer.MAX_VALUE);
                    }
                }
            }
        }
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    int dPrev = record.get(i).get(j).get(k - 1);
                    int dik = record.get(i).get(k).get(k - 1);
                    int dkj = record.get(k).get(j).get(k - 1);
                    int dPlus = Integer.MAX_VALUE;
                    if (dik != Integer.MAX_VALUE && dkj != Integer.MAX_VALUE) {
                        dPlus = dik + dkj;
                    }
                    record.get(i).get(j).put(k, Integer.min(dPrev, dPlus));
                }
            }
        }
        List<Path<T>> rv = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                int dMin = record.get(i).get(j).get(n);
                rv.add(new Path<>(vertices.get(i - 1), vertices.get(j - 1), dMin));
            }
        }
        this.memo.put(graph, rv);
        return rv;
    }

    protected WeightedGraph.Edge<T> edgeTo(WeightedGraph<T> graph, T u, T v) {
        WeightedGraph.Edge<T> rv = null;
        Set<WeightedGraph.Edge<T>> edges = graph.edgesOf(u);
        for (WeightedGraph.Edge<T> edge : edges) {
            if (edge.v.equals(v)) {
                rv = edge;
                break;
            }
        }
        return rv;
    }
   

        

}
