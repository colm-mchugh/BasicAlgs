package graph;


public interface GraphTraveral<T> {
    
    boolean hasPathTo(T x);
    
    Iterable<T> pathTo(T x);
}
