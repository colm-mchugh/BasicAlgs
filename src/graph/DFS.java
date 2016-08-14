package graph;


import java.util.HashMap;
import java.util.HashSet;

public class DFS<T> extends GraphTraversalBase<T> {
    
    public DFS(Graph<T> graph, T source) {
        this.graph = graph;
        this.source = source;
        this.linksTo = new HashMap<>();
        visited = new HashSet<>();
        doDFS(graph, source);
    }  

    private void doDFS(Graph<T> graph, T source) {
        visited.add(source);
        for (T v : graph.connections(source)) {
            if (!visited.contains(v)) {
                doDFS(graph, v);
                this.linksTo.put(v, source);
            }
        }
    }
}
