package graph;

import java.util.List;

/**
 * Weighted Graph API
 * 
 * A Weighted Graph is a graph where each edge has a weight. 
 */ 
public interface WeightedGraph<T> {
    
    /**
     * Create an edge between u and v with weight d
     * @param u
     * @param v
     * @param d
     */ 
    void link(T u, T v, int d);
    
    /**
     * The length of the shortest path between u and v
     * @param u
     * @param v
     * @return 
     */
    int sp(T u, T v);
    
    /**
     * An iterable of all vertices in the graph
     * @return 
     */
    Iterable<T> V();
    
    /**
     * The number of vertices in the graph
     */
    public int numVertices();

    WGraphImpl.SingleSourceResult<T> singleSourceShortestPaths(T s);
    
    List<WGraphImpl.ShortestPathResult<T>> allPairsShortestPaths();
    
    int apsp();
}
