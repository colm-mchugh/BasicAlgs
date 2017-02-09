package graph;

public class GraphTraversalDFS<T> extends GraphTraversal<T> {
    
    public GraphTraversalDFS(Graph<T> graph, T source) {
        super(graph, source);
        this.doDFS(source);
    }  

    /**
     * Construct a graph traversal using depth-first search.
     * 
     * This algorithm uses recursion and an initially empty visited set of vertices.
     * 
     * visited += source
     * for each edge (source, w):
     *      if w has not been visited:
     *          DFS(w)
     * 
     * @param source 
     */
    private void doDFS(T source) {
        visited.add(source);
        for (T v : graph.connections(source)) {
            if (!visited.contains(v)) {
                doDFS(v);
                this.linksTo.put(v, source);
            }
        }
    }
}
