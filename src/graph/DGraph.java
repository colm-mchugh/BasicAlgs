package graph;


public interface DGraph<T> {
    
    /**
     * 
     * @return true if the graph contains no cycles, false otherwise
     */
    boolean isAcyclic();
    
}
