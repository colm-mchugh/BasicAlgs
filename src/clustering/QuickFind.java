package clustering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * QuickFind.
 *
 * Implementation of UnionFind with constant time find and O(N) union
 * operations.
 *
 * @param <T>
 */
public class QuickFind<T> implements UnionFind<T> {

    // follower to leader mappings
    private final Map<T, T> leader = new HashMap<>();

    // keeps track of number of leaders (clusters or groups). 
    int leaderNumber = 0;

    // For managing follower sets. This is an optimization that reduces the 
    // time spent in union operations from N (all elements) to the size of
    // the smaller follower set.
    private Map<T, Set<T>> followers;

    private final boolean trackFollowers;

    public QuickFind(boolean trackLeaderSets) {
        this.trackFollowers = trackLeaderSets;
        if (trackLeaderSets) {
            this.followers = new HashMap<>();
        }
    }

    @Override
    public void addCluster(T p) {
        if (leader.containsKey(p)) {
            return;
        }
        leaderNumber++;
        leader.put(p, p);
        if (trackFollowers) {
            Set<T> pSet = new HashSet<>();
            pSet.add(p);
            followers.put(p, pSet);
        }
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
        T pCluster = leader.get(p);
        T qCluster = leader.get(q);

        // Noop if p and q are in the same cluster
        if (Objects.equals(pCluster, qCluster)) {
            return;
        }

        leaderNumber--; // one less cluster

        if (this.trackFollowers) {
            // Determine the bigger follower set and union the 
            // smaller follower set to the bigger.
            Set<T> pSet = this.followers.get(pCluster);
            Set<T> qSet = this.followers.get(qCluster);
            if (pSet.size() > qSet.size()) {
                this.combine(qCluster, pCluster);
            } else {
                this.combine(pCluster, qCluster);
            }
        } else {
            // Put the elements of q's cluster into p's cluster
            // Slow - iterate through all N elements and change 
            // q elements to have p as leader
            for (T el : leader.keySet()) {
                if (leader.get(el) == qCluster) {
                    leader.put(el, pCluster);
                }
            }
        }
    }

    /**
     * Given two leaders from and to, make to be the leader of
     * from and all of from's followers.
     * 
     * @param from
     * @param to 
     */
    private void combine(T from, T to) {
        Set<T> fromSet = this.followers.get(from);
        Set<T> toSet = this.followers.get(to);
        assert fromSet.size() <= toSet.size();
        for (T follower : fromSet) {
            leader.put(follower, to);
        }
        toSet.addAll(fromSet);
        followers.put(to, toSet);
        followers.remove(from);
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
        return Objects.equals(leader.get(p), leader.get(q));
    }

    @Override
    public Iterator<T> iterator() {
        return leader.keySet().iterator();
    }

    @Override
    public int numClusters() {
        return leaderNumber;
    }

}
