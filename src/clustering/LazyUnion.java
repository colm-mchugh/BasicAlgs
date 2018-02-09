package clustering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import utils.Stack;

/**
 * LazyUnion or Union By Rank.
 * 
 * Implementation of UnionFind that implements union(X, Y) by setting X to be
 * Y's leader, if Tree(X) > Tree(Y), otherwise Y is set to be X's leader.
 * 
 * This means a path must be traversed in order to get the leader for the group 
 * or cluster of an item X. The length of this path is O(log(N)) where N is the
 * overall number of items, when union is implemented as above. If path compression
 * is applied when getting a cluster leader, then the path length is O(log*(N)), 
 * where log*(N) is the number of times log() must be applied to N before it is 
 * 1. E.g. log*(2^65536) = 5.
 * 
 * Properties:
 * 
 * parent(X) == X => isLeader(X)
 * Leaf(X) => no item has X as parent
 * Rank(X): longest path from X to a leaf
 * Root(X): Parent(X) == X
 * Rank(Parent(X)) = Rank(X) + 1 (with path compression, Rank(Parent(X)) > Rank(X))
 * Tree(X): the ancestors of X
 *
 * Once an item gets a new parent, it never changes again.
 * If Rank(X) == Rank(Y) then tree(X) and Tree(Y) are disjoint. 
 * (An item Z cannot be on the same path as X and Y because ranks on a path are increasing)
 * 
 * If Rank(X) == r then Tree(X) has size >= 2^r
 * Max(Rank(X)) <= log(N), where N is the number of items.
 * Path compression: change parent, leave rank unchanged.
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
