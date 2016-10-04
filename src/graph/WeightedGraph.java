package graph;

import java.util.List;


public interface WeightedGraph<T> {
    void link(T u, T v, int d);
    
    int sp(T u, T v);
    
    Iterable<T> V();
    
    public int numVertices();

    WGraphImpl.SingleSourceResult<T> singleSourceShortestPaths(T s);
    
    List<WGraphImpl.ShortestPathResult<T>> allPairsShortestPaths();
    
    int apsp();
}
