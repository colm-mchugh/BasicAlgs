package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FlowImpl<T>  implements Flow<T> {

    private final Map<T, Set<Edge<T>>> graph;

    public FlowImpl() {
        graph = new HashMap<>();
    }
    
    @Override
    public void link(T u, T v, int c, int f) {
        Edge<T> newEdge = new Edge<>(u, v, c);
        newEdge.flow = f;
        addToGraph(u, newEdge);
        addToGraph(v, newEdge);
    }

    @Override
    public Iterable<T> V() {
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
    
}
