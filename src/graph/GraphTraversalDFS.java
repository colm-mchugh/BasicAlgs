package graph;

public class GraphTraversalDFS<T> extends GraphTraversal<T> {
    
    public GraphTraversalDFS(Graph<T> graph, T source) {
        super(graph, source);
        this.doDFS(source);
    }  

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
