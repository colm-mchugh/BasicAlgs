package clustering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
/**
 * QuickFind.
 * 
 * Implementation of UnionFind with O(1) find and O(N) union.
 * 
 * @param <T> 
 */
public class QuickFind<T> implements UnionFind<T> {

    // groupings maps an object to its cluster number
    private final Map<T, Integer> clusters = new HashMap<>();
    
    // keeps track of number of clusters (1 based). 
    int clusterNumber = 0;
    
    @Override
    public void addCluster(T p) {
        if (clusters.containsKey(p)) {
            return;
        }
        clusterNumber++;
        clusters.put(p, clusterNumber);
        //groupSizes[groupCounter++] = 1;
    }
    
    /**
     * Combine p and q in the same cluster.
     * 
     * Runs in O(N) of the number of items in all clusters. 
     * 
     * @param p
     * @param q 
     */
    @Override
    public void union(T p, T q) {
        int pCluster = clusters.get(p);
        int qCluster = clusters.get(q);
        
        // Noop if p and q are in the same cluster
        if (pCluster == qCluster) {
            return;
        }
        // Put the elements of q's cluster into p's cluster
        for (T el : clusters.keySet()) {
            if (clusters.get(el) == qCluster) {
                clusters.put(el, pCluster);
            }
        }
        
        clusterNumber--; // one less cluster
    }

    /**
     * true if p and q are in the same cluster, false otherwise.
     * 
     * Runs in constant time, using hash lookup on clusters.
     * 
     * @param p
     * @param q
     * @return 
     */
    @Override
    public boolean find(T p, T q) {
        return Objects.equals(clusters.get(p), clusters.get(q));
    }
    
    @Override
    public Iterator<T> iterator() {
        return clusters.keySet().iterator();
    }
    
    @Override
    public int numClusters() {
        return clusterNumber;
    }
    
}
