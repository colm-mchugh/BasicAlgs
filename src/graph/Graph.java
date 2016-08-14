package graph;


public interface Graph<T> {
    
    void add(T from, T to);
    
    Iterable<T> connections(T from);
    
    int E();
    
    Iterable<T> V();
    
    public int numVertices();
    
    public int minCut();
    
    Graph<T> reverse();
    
    void print();
}
