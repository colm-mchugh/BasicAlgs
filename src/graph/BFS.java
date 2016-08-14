package graph;


import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;


public class BFS<T> extends GraphTraversalBase<T> {
    
    public BFS(Graph<T> graph, T source) {
        this.graph = graph;
        this.source = source;
        this.linksTo = new HashMap<>();
        visited = new HashSet<>();
        // doBFS(graph, source);
        Queue<T> queue = new ArrayDeque<>();
        queue.add(source);
        visited.add(source);
        while (!queue.isEmpty()) {
            T next = queue.remove();
            //visited.add(next);
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
