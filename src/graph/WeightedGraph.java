package graph;


public interface WeightedGraph<T> {
    void link(T u, T v, int d);
    
    int sp(T u, T v);
    
    Iterable<T> V();
    
    public int numVertices();

}
