package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 *
 * CCer - Connected Components computer - outline of a class for calculating the
 * connected components of a directed graph. Subclasses implement an algorithm
 * to generate the connected components.
 *
 * The strongly connected components C1,..CN of a directed graph g are disjoint
 * sets of the vertices of g such that, for every pair of vertices (v1, v2) in
 * each Ci, v1 <=> v2, i.e. in g, there is a path from v1 to v2 and from v2 to v1.
 * A single vertex v is a strongly connected component, i.e. v <=> v is true. 
 *
 */
public abstract class CCer<T> {

    protected Graph<T> g;
    protected final Map<T, List<T>> components;
    protected final Map<T, T> index;

    public CCer() {
        this.g = null;
        this.components = new HashMap<>();  // The SCCs of g
        this.index = new Hashtable<>(); // index for O(1) resolution of vertices being in same CC
    }

    /**
     * Return the strongly connected components of g as computed by the
     * constructor.
     *
     * @return a map of leader -> vertex set, where leader is the leader vertex
     * of the set and vertex set is a set of vertices such that v1 <=> v2 for
     * all pairs v1 and v2 in the set (including the leader).
     */
    public abstract Map<T, List<T>> getComponents(Graph<T> g);

    /**
     *
     * @return the graph this CCCer operates on
     */
    public Graph<T> getGraph() {
        return g;
    }

    /**
     * Return true if vertices t1 and t2 are in the same connected component,
     * false otherwise.
     *
     * Uses the component index for O(1) running time.
     *
     * @param t1
     * @param t2
     * @return
     */
    public boolean sameCC(T t1, T t2) {
        T l1 = this.index.get(t1);
        T l2 = this.index.get(t2);
        return l1 != null && l2 != null && l1 == l2;
    }

    /**
     * Return a list of the size of the connected components. The list is in
     * descending order, i.e. the first number is the size of the largest
     * component, second number is the size of the second largest component,
     * etc.
     *
     * @return
     */
    public List<Integer> ccSizes() {
        List<Integer> rv = new ArrayList<>(this.components.keySet().size());
        for (T v : this.components.keySet()) {
            rv.add(this.components.get(v).size());
        }
        rv.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println("#components=" + this.components.keySet().size());
        return rv;
    }

    public void print() {
        for (T foo : this.components.keySet()) {
            List<T> comps = this.components.get(foo);
            for (T c : comps) {
                System.out.print(c);
                System.out.print(' ');
            }
            System.out.println();
        }
    }
}
