package clustering;

import java.util.Iterator;


public interface UnionFind<T> {
    
    void union(T p, T q);
    
    boolean find(T p, T q);
    
    void addCluster(T p);
    
    int numClusters();
    
    Iterator<T> iterator();
}
