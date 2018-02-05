package clustering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import utils.Stack;

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
    private final Stack<T> compressed;

    public LazyUnion(boolean doPathCompression) {
        this.doPathCompression = doPathCompression;
        if (doPathCompression) {
            compressed = new Stack<>();
        } else {
            compressed = null;
        }
    }
    
    @Override
    public void union(T p, T q) {
        T pLeader = getLeader(p);
        T qLeader = getLeader(q);
        
        // Same leader => already in same group/cluster
        if (Objects.equals(pLeader, qLeader)) {
            return;
        }
        
        int pRank = this.rank.get(pLeader);
        int qRank = this.rank.get(qLeader);
        if (pRank > qRank) {
            parent.put(qLeader, pLeader);
        } else {
            parent.put(pLeader, qLeader);
            if (Objects.equals(pRank, qRank)) {
                rank.put(qLeader, rank.get(qLeader) + 1);
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
        while (!Objects.equals(parent.get(x), parent.get(parent.get(x)))) {
            if (doPathCompression) {
                compressed.Push(x);
            }
            x = parent.get(x);
        }
        if (doPathCompression) {
            while (!compressed.isEmpty()) {
                parent.put(compressed.Pop(), parent.get(x));
            }
        }
        return parent.get(x);
    }
}
