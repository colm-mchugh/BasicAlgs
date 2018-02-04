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
    private final Map<T, T> followerLeader = new HashMap<>();

    // keeps track of number of leaders (clusters or groups). 
    int leaderNumber = 0;

    private Map<T, Set<T>> leaderSets;

    private final boolean trackLeaderSets;

    public QuickFind(boolean trackLeaderSets) {
        this.trackLeaderSets = trackLeaderSets;
        if (trackLeaderSets) {
            this.leaderSets = new HashMap<>();
        }
    }

    @Override
    public void addCluster(T p) {
        if (followerLeader.containsKey(p)) {
            return;
        }
        leaderNumber++;
        followerLeader.put(p, p);
        if (trackLeaderSets) {
            Set<T> pSet = new HashSet<>();
            pSet.add(p);
            leaderSets.put(p, pSet);
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
        T pCluster = followerLeader.get(p);
        T qCluster = followerLeader.get(q);

        // Noop if p and q are in the same cluster
        if (Objects.equals(pCluster, qCluster)) {
            return;
        }

        leaderNumber--; // one less cluster

        if (this.trackLeaderSets) {
            Set<T> pSet = this.leaderSets.get(pCluster);
            Set<T> qSet = this.leaderSets.get(qCluster);
            if (pSet.size() > qSet.size()) {
                this.combine(qCluster, pCluster);
            } else {
                this.combine(pCluster, qCluster);
            }
        } else {

            // Put the elements of q's cluster into p's cluster
            for (T el : followerLeader.keySet()) {
                if (followerLeader.get(el) == qCluster) {
                    followerLeader.put(el, pCluster);
                }
            }
        }
    }

    private void combine(T from, T to) {
        Set<T> fromSet = this.leaderSets.get(from);
        Set<T> toSet = this.leaderSets.get(to);
        assert fromSet.size() <= toSet.size();
        for (T follower : fromSet) {
            followerLeader.put(follower, to);
        }
        toSet.addAll(fromSet);
        leaderSets.put(to, toSet);
        leaderSets.remove(from);
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
