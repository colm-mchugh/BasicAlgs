package graph;


/**
 * A graph API.
 * 
 * @author colm_mchugh
 * @param <T> The type of object in the graph
 */
public interface Graph<T> {
    
    /**
     * Add an edge between vertices from and to.
     * Post conditions:
     * 1) from and to are both in the graph (if they weren't already)
     * 2) the graph contains the edge: from -> to
     * 
     * @param from
     * @param to 
     */
    void add(T from, T to);
    
    /**
     * Return all the objects that a given vertex 'from' has edges with, 
     * in other words all the vertices it is connected to.
     * 
     * @param from The vertex of interest.
     * @return an iterable over all the vertices that from has edges with.
     * 
     * If the graph does not contain a vertex 'from', or if it does and 'from
     * has zero edges, then connections returns an empty iterable.
     */
    Iterable<T> connections(T from);
    
    /**
     * 
     * @return the number of edges in the graph
     */
    int E();
   
    /**
     * 
     * @return all the vertices in the graph
     */
    Iterable<T> V();
    
    /**
     * 
     * @return the number of vertices in the graph
     */
    public int numVertices();
    
    /**
     * 
     * @return the size of the minimum cut of the graph
     * 
     * A cut of a graph is a partition of a graph into two subsets S1 and S2
     * s.t. S1 intersection S2 == {}, S1 U S2 == V() (i.e. all the vertices of the graph)
     * and N is the number of edges between S1 and S2.
     * The Minimum Cut is the partition with smallest possible N.
     */
    public int minCut();
    
    /**
     * Return a graph that is the reverse of this graph. 
     * A directed graph will return a copy of itself with all edges reversed.
     * An undirected graph will return itself.
     * @return 
     */
    Graph<T> reverse();
    
    /**
     * Print a representation of the graph
     */
    void print();
}
