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

    final protected Map<T, Set<T>> rep;
    final private Set<T> emptySet = new HashSet<>();
    final private Map<T, Set<T>> fusions;

    public UGraphMapImpl() {
        rep = new HashMap<>();
        fusions = new HashMap<>();
    }

    @Override
    public void add(T from, T to) {
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
        if (!rep.containsKey(from)) {
            rep.put(from, new HashSet<>());
        }
        rep.get(from).add(to);
    }

    @Override
    public int minCut() {
        Map<T, Set<T>> clone = this.cloneRep();
        while (clone.keySet().size() > 2) {
            T v1 = this.chooseRandom(clone.keySet());
            T v2 = this.chooseRandom(clone.get(v1));
            this.fuse(v1, v2, clone);
        }
        Set<T> setA = new HashSet<>();
        Set<T> setB = new HashSet<>();
        Set<T> tmp = setA;
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
        return countCrossings(setA, setB);
    }

    private T chooseRandom(Set<T> s) {
        int i = RandGen.uniform(s.size());
        for (T v : s) {
            if (i == 0) {
                return v;
            }
            i--;
        }
        return null;
    }

    private void fuse(T from, T to, Map<T, Set<T>> clone) {
        Set<T> fromSet = clone.get(from);
        Set<T> toSet = clone.get(to);
        T mergeInto = from;
        T mergee = to;
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
