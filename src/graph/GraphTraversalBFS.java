package graph;


import java.util.ArrayDeque;
import java.util.Queue;


public class GraphTraversalBFS<T> extends GraphTraversal<T> {
    
    public GraphTraversalBFS(Graph<T> graph, T source) {
        super(graph, source);
        Queue<T> queue = new ArrayDeque<>();
        queue.add(source);
        visited.add(source);
        while (!queue.isEmpty()) {
            T next = queue.remove();
            for (T v : this.graph.connections(next)) {
                if (!visited.contains(v)) {
                    this.linksTo.put(v, next);
                    queue.add(v);
                    visited.add(v);
                }
            }
        }
        
    }
}
