package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CCKosaraju (Connecting Components Computer) computes the strongly connected
 * components of a directed graph and stores them for retrieval.
 * 
 * @param <T> the type of the graph
 */
public class CCKosaraju<T> extends CCer<T> {

    /**
     * CCCer determines the strongly connected components of a directed graph.
     *
     *
     * CCCer computes the strongly connected components of g using Kosaraju's 
     * algorithm; this performs two Depth First (DFS) passes over the graph, as 
     * described in part 1 and part 2 below.
     * 
     * @param g the graph g
     * @return 
     */
    @Override
    public Map<T, List<T>> getComponents(Graph<T> g) {
        if (this.g == g && !this.components.isEmpty()) {
            return this.components;
        }
        this.g = g;
        Set<T> visited = new HashSet<>();  // To keep track of visited vertices during a graph search
        
        // part 1 - reverse the graph and construct a topological ordering of all
        // the vertices of the reversed graph
        Graph<T> rev = g.reverse();
        List<T> ordering = new ArrayList<>(rev.numVertices());
        for (T v : rev.V()) {
            if (!visited.contains(v)) {
                // This will be called for each SCC in g.rev
                this.makeTopologicalOrdering(rev, v, ordering, visited);
            }
        }
        // postcondition: ordering is a topological ordering of the vertices of
        // g.reverse()
        
        visited.clear(); // reuse visited for determining the connected components
        
        // part 2 - visit the vertices of the topological ordering from most  
        // recent first, and for each vertex v that has not been visited, it
        // becomes the leader of a new component; populateConnectedComponents 
        // captures all the vertices in v's component, i.e. vertices reachable 
        // from v via DFS
        for (int i = ordering.size() - 1; i >= 0; i--) {
            T v = ordering.get(i);
            if (!visited.contains(v)) {  
                this.components.put(v, new ArrayList<>());
                // v will be leader of a new component, which will also include v
                this.populateConnectedComponents(g, v, v, visited);
            }
        }
        return this.components;
    }
    
    /**
     * Create a topological ordering of the vertices in graph, starting from vertex
     * source.
     * 
     * The graph is traversed Depth-First, and source added to the ordering after 
     * all the unvisited vertices in its connected component have been processed.
     * This means ordering will list the vertices from the sink to the first.
     * 
     * @param graph
     * @param source
     * @param ordering
     * @param visited 
     */
    public void makeTopologicalOrdering(Graph<T> graph, T source, List<T> ordering, Set<T> visited) {
        visited.add(source);
        for (T v : graph.connections(source)) {
            if (!visited.contains(v)) {
                makeTopologicalOrdering(graph, v, ordering, visited);
            }
        }
        ordering.add(source);
    }
    
    /**
     * For the current leader, add all the vertices that are reachable from that
     * leader via DFS (Depth-First search). 
     * 
     * @param graph
     * @param source
     * @param ordering
     * @param visited 
     */
    private void populateConnectedComponents(Graph<T> graph, T leader, T source, Set<T> visited) {
        this.components.get(leader).add(source);
        this.index.put(source, leader);
        visited.add(source);
        for (T v : graph.connections(source)) {
            if (!visited.contains(v)) {
                populateConnectedComponents(graph, leader, v, visited);
            }
        }
    }
    
}
