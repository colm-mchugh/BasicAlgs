package clustering;

import java.util.Iterator;
import java.util.Set;


public interface UnionFind<T> {
    
    void union(T p, T q);
    
    boolean find(T p, T q);
    
    Set<T> Cluster(T p);
    
    void addCluster(T p);
    
    int numClusters();
    
    Iterator<T> iterator();
}
