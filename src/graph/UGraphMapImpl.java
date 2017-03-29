package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
    public Collection<T> connections(T from) {
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
        // Implement random cut using Karger's algorithhm:
        // While there are more than two vertices, pick two at random and fuse them
        // into a single vertex, removing self-loops and edge(s) between them.
        // The edges of the fused vertex are the union of the randomly two vertices edges.
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
        return new GraphCut<>(setA, setB, this);
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

    /**
     * Fuse vertices u and v together in the cloned graph.
     * 
     * This involves:
     * - Decide which of u and v have the most edges; the edges of the other
     *   vertex will be merged into these edges.
     * - 
     *
     * Postcondition: clone contains a vertex uv AND edges(uv) = edges(u) U edges(v)
     * 
     * @param u
     * @param v
     * @param clone 
     */
    private void fuse(T u, T v, Map<T, Set<T>> clone) {
        Set<T> uSet = clone.get(u);
        Set<T> vSet = clone.get(v);
        T merger = u; 
        T mergee = v;
        // merge the smaller set into the larger set
        if (uSet.size() < vSet.size()) {
            merger = v;
            mergee = u;
        }
        // At this point, mergee is the vertex with smallest #edges of (u,v)
        //                merger is the vertex with largest #edges of (u,v)
        // In the mergee's edges, replace edges to the mergee with edges to merger       
        Set<T> mergeeSet = clone.get(mergee);
        for (T element : mergeeSet) {
            Set<T> elementSet = clone.get(element);
            elementSet.remove(mergee);
            elementSet.add(merger);
        }
        // Add the mergee's edges to the merger's edges
        Set<T> mergerSet = clone.get(merger);
        mergerSet.addAll(mergeeSet);
        mergerSet.remove(merger); // removes self loops, if any
        clone.remove(mergee); // remove mergee from cloned graph; the edge (u,v) is now a single vertex uv
        // fusions should contain an entry for the merger vertex, with all the mergee's edges
        // fusions should no longer contain an entry for the mergee vertex
        if (fusions.get(merger) == null) {
            fusions.put(merger, new HashSet<>());
        };
        fusions.get(merger).add(mergee);
        if (fusions.get(mergee) != null) {
            fusions.get(merger).addAll(fusions.get(mergee));
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

    @Override
    public Set<T> vertexCover() {
        throw new UnsupportedOperationException("Not supported yet."); 
        // TODO: Add support for accessing the edges of the graph randomly.
        //       Also: given a vertex, access its inbound and outbound edges.
        // Given this, the following algorithm can be used to implement 
        // approximate vertex cover in O(E), where E is the number of edges.
        //
        // Edges = all edges E(u,v) of the graph
        // Vertex Cover = {}
        // while (Edges are not empty)
        //      Select an edge E at random from Edges
        //      Add E.u, E.v to the Vertex Cover.
        //      From Edges, remove all edges touched by E
        //      i.e. All edges Er where Er.u = E.u or Er.v = E.u or Er.u = E.v or Er.v = E.v
        // Return Vertex Cover
    }
    
        
}
