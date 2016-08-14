package graph;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;


public class TopologicalSort<T> {
    private final DGraphImpl<T> graph;
    private Set<T> visited;
    private Stack<T> ordering;
    
    public TopologicalSort(DGraphImpl<T> graph) {
        this.graph = graph;
        if (false && !graph.isAcyclic()) {
            // graph has no sink vertices => cannot determine a final vertex
            // in an ordering
            throw new IllegalArgumentException("Graph must be acyclic");
        }
        this.orderIt();
    }

    private void orderIt() {
        visited = new HashSet<T>();
        ordering = new Stack<>();
        for (T v : this.graph.V()) {
            if (!visited.contains(v)) {
                this.doDFS(graph, v);
            }
        }
    }
    
    public Iterable<T> getOrdering() {
        return this.ordering;
    }
    
    private void doDFS(DGraphImpl<T> graph, T source) {
        visited.add(source);
        for (T v : graph.connections(source)) {
            if (!visited.contains(v)) {
                doDFS(graph, v);
            }
        }
        ordering.push(source);
    }
    
    public T next() {
        if (ordering.empty()) {
            return null;
        }
        return ordering.pop();
    }
    
}
