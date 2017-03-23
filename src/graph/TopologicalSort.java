package graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;


/**
 * Created an ordering of the vertices of a Directed acyclic graph such that,
 * for each edge in the graph: u -> v, the vertex u succeeds v in the ordering.
 * i.e. index(u) > index(v), where index(x) gives the position of a vertex in the
 * ordering. Sink nodes will precede source nodes in the ordering.
 * 
 * TODO: extend with Tarjan's algorithm (https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm)
 * 
 * @param <T> 
 */
public class TopologicalSort<T> {
   
    public List<T> sort(DGraphImpl<T> graph) {
        if (!graph.isAcyclic()) {
            // graph has no sink vertices => cannot determine a final vertex
            // in any ordering
            throw new IllegalArgumentException("Graph must be acyclic");
        }
        Set<T> visited = new HashSet<>();
        Stack<T> ordering = new Stack<>();
        for (T v : graph.V()) {
            if (!visited.contains(v)) {
                this.doDFS(graph, v, visited, ordering);
            }
        }
        return ordering;
    }
    
    private void doDFS(DGraphImpl<T> graph, T source, Set<T> visited, Stack<T> ordering) {
        visited.add(source);
        for (T v : graph.connections(source)) {
            if (!visited.contains(v)) {
                doDFS(graph, v, visited, ordering);
            }
        }
        ordering.push(source);
    }
    
}
