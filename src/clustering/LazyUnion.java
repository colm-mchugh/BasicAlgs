package clustering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * LazyUnion or Union By Rank.
 * 
 * Implementation of UnionFind that .
 * 
 * @param <T> 
 */
public class LazyUnion<T> implements UnionFind<T> {

    // parent map. Maps items (keys) to their parents (values).
    private final Map<T, T> parent = new HashMap<>();
    
    // keeps track of number of leaders (clusters or groups). 
    int leaderNumber = 0;
    
    // keeps track of each item's rank
    private final Map<T, Integer> rank = new HashMap<>();
    
    private final boolean doPathCompression;

    public LazyUnion(boolean doPathCompression) {
        this.doPathCompression = doPathCompression;
    }
    
    @Override
    public void union(T p, T q) {
        T s1 = getLeader(p);
        T s2 = getLeader(q);
        int r1 = this.rank.get(s1);
        int r2 = this.rank.get(s2);
        if (r1 > r2) {
            parent.put(s2, s1);
        } else {
            parent.put(s1, s2);
            if (Objects.equals(s1, s2)) {
                rank.put(s2, rank.get(s2) + 1);
            }
        }
        leaderNumber--;
    }

    @Override
    public boolean find(T p, T q) {
        return Objects.equals(this.getLeader(q), this.getLeader(p));
    }

    @Override
    public void addCluster(T p) {
        if (parent.containsKey(p)) {
            return;
        }
        leaderNumber++;
        parent.put(p, p);
        rank.put(p, 0);
    }

    @Override
    public int numClusters() {
        return leaderNumber;
    }

    @Override
    public Iterator<T> iterator() {
        return parent.keySet().iterator();
    }
    
    private T getLeader(T x) {
        T child = x;
        T leader = this.parent.get(x);
        while (!Objects.equals(child, leader)) {
            child= leader;
            leader = this.parent.get(child);
        }
        if (doPathCompression && !Objects.equals(leader, this.parent.get(x))) {
            this.parent.put(x, leader);
        }
        return leader;
    }
}
