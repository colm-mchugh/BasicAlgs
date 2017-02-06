package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import utils.RandGen;

/**
 * Undirected graph implementation of Graph API.
 * 
 * An undirected graph is one where all edges are bidirectional; for every edge
 * X -> Y it is possible to go from X to Y, and equally possible to go from Y to X.
 * 
 * @author colm_mchugh
 * @param <T> 
 */
public class UGraphMapImpl<T> implements Graph<T> {

    // rep is the graph representation; each vertex maps to its connected vertices
    final protected Map<T, Set<T>> rep;
    // constant for representing the empty set
    final private Set<T> emptySet = new HashSet<>();
    final private Map<T, Set<T>> fusions;

    public UGraphMapImpl() {
        rep = new HashMap<>();
        fusions = new HashMap<>();
    }

    @Override
    public void add(T from, T to) {
        // This is an undirected graph, so add two links, from->to and to->from
        this.link(from, to);
        this.link(to, from);
    }

    @Override
    public Iterable<T> connections(T from) {
        Set<T> rv = this.rep.get(from);
        if (rv == null) {
            rv = this.emptySet;
        }
        return rv;
    }

    @Override
    public int E() {
        int rv = 0;
        for (Map.Entry<T, Set<T>> edge : this.rep.entrySet()) {
            rv += edge.getValue().size();
        }
        // Undirected graph => edges are recorded in rep twice, but only counted once
        return rv / 2;
    }

    @Override
    public Iterable<T> V() {
        return this.rep.keySet();
    }

    @Override
    public int numVertices() {
        return this.rep.keySet().size();
    }

    protected void link(T from, T to) {
        if (!rep.containsKey(from)) { // Vertex from is not in the graph
            rep.put(from, new HashSet<>());
        }
        rep.get(from).add(to);
    }

    /**
     * Returns a cut of the graph.  
     * @return 
     */
    @Override
    public GraphCut<T> makeCut() {
        // Implement random cut computation using Karger's well known algorithhm:
        // While there are more than two vertices, pick two at random and fuse them
        // into a single vertex, removing self-loops and edge(s) between them
        Map<T, Set<T>> clone = this.cloneRep(); // Use a copy of the graph - don't mutate self
        while (clone.keySet().size() > 2) {
            T v1 = this.chooseRandom(clone.keySet());
            T v2 = this.chooseRandom(clone.get(v1));
            // note that if (v1, v2) happens to be a crossing edge of the graph's minimum cut,
            // then the cut created by this invocation cannot be a minimum cut.
            this.fuse(v1, v2, clone);
        }
        // Done fusing, now put the vertices on either side of the cut into sets
        Set<T> setA = new HashSet<>();
        Set<T> setB = new HashSet<>();
        Set<T> tmp = setA;
        // Clone has precisely two keys at this point - the randomly chosen vertices
        assert clone.keySet().size() == 2;
        for (T v : clone.keySet()) {
            if (!tmp.isEmpty()) {
                tmp = setB;
            }
            tmp.add(v);
            if (this.fusions.containsKey(v)) {
                tmp.addAll(fusions.get(v));
                fusions.remove(v);
            }
        }
        if (!fusions.isEmpty()) { 
            throw new RuntimeException("Fusions is not empty!!");
        }
        return new GraphCut<>(setA, setB, countCrossings(setA, setB));
    }

    /**
     * Return a random element of the set s
     * @param s the set
     * @return a random element in the set
     */
    private T chooseRandom(Set<T> s) {
        int i = RandGen.uniform(s.size());
        // This algorithm requires iterating the vertices, therefore is O(V())
        // There may be a more efficient implementation, depending on how stream()
        // is implemented: return s.stream().skip(i).findFirst().get();
        for (T v : s) {
            if (i == 0) {
                return v;
            }
            i--;
        }
        return null;
    }

    // Fuse vertices from and to into a single vertex in the graph representation clone.
    private void fuse(T from, T to, Map<T, Set<T>> clone) {
        Set<T> fromSet = clone.get(from);
        Set<T> toSet = clone.get(to);
        T mergeInto = from;
        T mergee = to;
        // merge the smaller set into the larger set
        if (fromSet.size() < toSet.size()) {
            mergeInto = to;
            mergee = from;
        }
        Set<T> mergeeSet = clone.get(mergee);
        for (T element : mergeeSet) {
            Set<T> elementSet = clone.get(element);
            elementSet.remove(mergee);
            elementSet.add(mergeInto);
        }
        Set<T> mergeintoSet = clone.get(mergeInto);
        mergeintoSet.addAll(mergeeSet);
        mergeintoSet.remove(mergeInto);
        clone.remove(mergee);
        if (fusions.get(mergeInto) == null) {
            fusions.put(mergeInto, new HashSet<>());
        };
        fusions.get(mergeInto).add(mergee);
        if (fusions.get(mergee) != null) {
            fusions.get(mergeInto).addAll(fusions.get(mergee));
            fusions.remove(mergee);
        }
    }

    private Map<T, Set<T>> cloneRep() {
        Map<T, Set<T>> rv = new HashMap<>();
        for (T v : this.rep.keySet()) {
            rv.put(v, new HashSet<>());
            rv.get(v).addAll(this.rep.get(v));
        }
        return rv;
    }

    private int countCrossings(Set<T> setA, Set<T> setB) {
        int rv = 0;
        for (T v : setA) {
            for (T x : setB) {
                if (this.rep.get(v).contains(x)) {
                    rv++;
                }
            }
        }
        return rv;
    }

    @Override
    public Graph<T> reverse() {
        return this;
    }
    
    public void print() {
        for (T vertex : this.V()) {
            System.out.print(vertex + ": ");
            for (T connection : this.connections(vertex)) {
                System.out.print(connection);
                System.out.print(' ');
            }
            System.out.println();
        }
    }
    
}
