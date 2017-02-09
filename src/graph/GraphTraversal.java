package graph;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * GraphTraversal encapsulates the traversal of a graph from a given source vertex.
 * 
 * It includes the necessary state to 
 * 
 * @param <T> 
 */
public abstract class GraphTraversal<T> {
    protected  Graph<T> graph; // the graph under traversal
    protected  T source;    // the starting vertex in graph for the traversal
    protected  Map<T, T> linksTo;   // chains of all the vertices visted in the traversal
    protected  Set<T> visited;  // all vertices visited in the traversal. (note: linksTo.keySet() != visited)

    /**
     * Construct a graph traversal of the given graph starting at the given source 
     * vertex.
     * 
     * Preconditions: source is in graph.V()
     * 
     * @param graph
     * @param source 
     */
    public GraphTraversal(Graph<T> graph, T source) {
        this.graph = graph;
        this.source = source;
        this.linksTo = new HashMap<>();
        visited = new HashSet<>();
    }
    
    /**
     * Return true if there is a path from the source vertex to x in the graph,
     * false otherwise.
     * 
     * @param x
     * @return 
     */
    public boolean hasPathTo(T x) {
        return this.visited.contains(x);
    }
    
    public Iterable<T> pathTo(T x) {
        Deque<T> path = new ArrayDeque<>();
        if (!this.hasPathTo(x)) {
            return path;
        }
        for (T v = x; v != this.source; v = this.linksTo.get(v)) {
            path.push(v);
        }
        path.push(this.source);
        return path;
    }

    public Graph<T> getGraph() {
        return graph;
    }

    public T getSource() {
        return source;
    }
    
}
