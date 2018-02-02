package clustering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
/**
 * QuickFind.
 * 
 * Implementation of UnionFind with constant time find and O(N) union operations.
 * 
 * @param <T> 
 */
public class QuickFind<T> implements UnionFind<T> {

    // follower to leader mappings
    private final Map<T, T> followerLeader = new HashMap<>();
    
    // keeps track of number of leaders (clusters or groups). 
    int leaderNumber = 0;
    
    @Override
    public void addCluster(T p) {
        if (followerLeader.containsKey(p)) {
            return;
        }
        leaderNumber++;
        followerLeader.put(p, p);
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
        T pCluster = followerLeader.get(p);
        T qCluster = followerLeader.get(q);
        
        // Noop if p and q are in the same cluster
        if (Objects.equals(pCluster, qCluster)) {
            return;
        }
        // Put the elements of q's cluster into p's cluster
        for (T el : followerLeader.keySet()) {
            if (followerLeader.get(el) == qCluster) {
                followerLeader.put(el, pCluster);
            }
        }
        
        leaderNumber--; // one less cluster
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
        return Objects.equals(followerLeader.get(p), followerLeader.get(q));
    }
    
    @Override
    public Iterator<T> iterator() {
        return followerLeader.keySet().iterator();
    }
    
    @Override
    public int numClusters() {
        return leaderNumber;
    }
    
}
