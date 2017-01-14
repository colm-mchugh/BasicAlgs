package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StrongCC<T> {

    private final Graph<T> g;
    private final Map<T, Set<T>> components;
    
    /**
     * StrongCC creates the strongly connected components of a directed graph.
     *
     * The strongly connected components C1,..CN of a directed graph g are disjoint 
     * sets of the vertices of g such that, for every pair of vertices (v1, v2)
     * in each Ci, v1 <=> v2, i.e. there is a path from v1 to v2 and there is a
     * path from v2 to v1. A single vertex v can be a strongly connected
     * component; v <=> v is true.
     *
     * StrongCC computes the strongly connected components of g using Kosaraju's 
     * algorithm; this performs two Depth First (DFS) passes over the graph, as 
     * described in part 1 and part 2 below.
     * 
     * @param g the graph g
     */
    public StrongCC(Graph<T> g) {
        this.g = g; // the directed graph 
        this.components = new HashMap<>();  // Will hold the SCCs of g
        Set<T> visited = new HashSet<>();  // for keeping track of visited vertices during a graph search
        
        // part 1 - reverse the graph and construct a topological ordering of all
        // the vertices of the reversed graph
        Graph<T> rev = g.reverse();
        List<T> ordering = new ArrayList<>(rev.numVertices());
        for (T v : rev.V()) {
            if (!visited.contains(v)) {
                // following will be called for each SCC in g.rev
                this.makeTopologicalOrdering(rev, v, ordering, visited);
            }
        }
        // postcondition: ordering contains a topological ordering of the 
        // vertices of g.reverse()
        
        visited.clear();
        // part 2 - visit the vertices of the topological ordering from most  
        // recent first, and for each vertex v that has not been visited, it
        // becomes the leader of a new component; populateConnectedComponents 
        // captures all the vertices in v's component, i.e. vertices reachable 
        // from v via DFS
        for (int i = ordering.size() - 1; i >= 0; i--) {
            T v = ordering.get(i);
            if (!visited.contains(v)) {  
                this.components.put(v, new HashSet<>());
                // v will be leader of a new component, which will also include v
                this.populateConnectedComponents(g, v, v, ordering, visited);
            }
        }
    }
    
    /**
     * Create a topological ordering of the vertices in graph, starting from vertex
     * source.
     * 
     * The graph is traversed Depth-First, and source added to the ordering after 
     * all the unvisited vertices in its connected component have been processed.
     * 
     * @param graph
     * @param source
     * @param ordering
     * @param visited 
     */
    private void makeTopologicalOrdering(Graph<T> graph, T source, List<T> ordering, Set<T> visited) {
        visited.add(source);
        for (T v : graph.connections(source)) {
            if (!visited.contains(v)) {
                makeTopologicalOrdering(graph, v, ordering, visited);
            }
        }
        ordering.add(source);
    }
    
    /**
     * For the current leader, add all the vertices that are reachable from that
     * leader via DFS (Depth-First search). 
     * 
     * @param graph
     * @param source
     * @param ordering
     * @param visited 
     */
    private void populateConnectedComponents(Graph<T> graph, T leader, T source, List<T> ordering, Set<T> visited) {
        this.components.get(leader).add(source);
        visited.add(source);
        for (T v : graph.connections(source)) {
            if (!visited.contains(v)) {
                populateConnectedComponents(graph, leader, v, ordering, visited);
            }
        }
    }
    
    /**
     * Return the strongly connected components of g as computed in the
     * constructor.
     *
     * @return a map of leader -> vertex set, where leader is the leader vertex
     * of the set and vertex set is a set of vertices such that v1 <=> v2 for
     * all pairs v1 and v2 in the set (including leader).
     */
    public Map<T, Set<T>> ccs() {
        return this.components;
    }

    /**
     * Return true if vertices t1 and t2 are in the same connected component,
     * false otherwise.
     *
     * @param t1
     * @param t2
     * @return
     */
    public boolean areCC(T t1, T t2) {
        for (Set<T> s : this.components.values()) {
            if (s.contains(t1) && s.contains(t2)) {
                return true;
            }
        }
        return false;
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
            Set<T> comps = this.components.get(foo);
            for (T c : comps) {
                System.out.print(c);
                System.out.print(' ');
            }
            System.out.println();
        }
    }
}
