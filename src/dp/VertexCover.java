package dp;

import graph.WeightedGraph;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Two approximation algorithms for vertex cover.
 * 
 * @author Colm
 * @param <T> 
 */
public class VertexCover<T> {
    
    public final Set<T> EMPTY_SET = new HashSet<>(); // immutable
    
  /**
    * Compute the vertex cover of a weighted graph G(V, E).
    * 
    * Vertex Cover of G: the minimum sized set of vertices that includes at least 
    * one endpoint of each edge in E. 
    * 
    * Given G(V, E) and k, return either:
    *  - V', a subset of V of size less than or equal to k
    *  - The empty set, if there is no vertex cover of at most size k 
    * 
    * Exhaustively trying all possibilities (N choose k) is O(N^k).
    * This algorithm uses a search strategy based on the logical bicondition:
    *      E vertex-cover(G, k) <=> E vertex-cover(G - u, k - 1) || E vertex-cover(G - v, k - 1)
    * where (u,v) is any edge of G.
    * 
    * Running time: O(2^k), because 2 calls per invocation, with recursion depth at most k.
    *               O(E) per invocation => O(E2^k) overall. Feasible when k ~ O(log(N))
    * @param <T> 
     * @param G 
    */
    public Set<T> cover(WeightedGraph<T> G, int k) {
        // Base cases:
        if (G.numVertices() == 0 || G.numEdges() == 0) {
            return new HashSet<>();
        }
        if (k == 0 && G.numVertices() > 0) {
            return EMPTY_SET;
        }
        // Pick an edge (u,v) of G
        // TODO: pick (u, v) with vertex u having the most edges in G
        T u = null; 
        T v = null;
        Iterator<T> gV = G.V().iterator();
        do {
            u = gV.next();
            Set<WeightedGraph.Edge<T>> uE = G.edgesOf(u);
            WeightedGraph.Edge<T> ev = (uE.isEmpty() ? null : uE.iterator().next());
            v = (ev != null ? ev.v : null);
        } while (v == null) ;
        
        // Remove u and its edges from G, recurse with cover size (k - 1)
        Set<WeightedGraph.Edge<T>> uEdges = G.remove(u);
        Set<T> Su = cover(G, k - 1);
        if (Su != EMPTY_SET && Su.size() == k - 1) {
            Su.add(u);
            return Su;
        }
        
        // Remove v and its edges from G, recurse with cover size (k - 1)
        Set<WeightedGraph.Edge<T>> vEdges = G.restore(u, uEdges).remove(v);
        Set<T> Sv = cover(G, k - 1);
        if (Sv != EMPTY_SET && Sv.size() == k - 1) {
            Sv.add(v);
            return Sv;
        }
        G.restore(v, vEdges);
        return EMPTY_SET;
    }

    /**
     * Compute the vertex cover of a weighted graph G(V, E).
     * 
     * Start with empty vertex cover
     * Repeat until G has no edges:
     *      Select an edge (u,v), add u and v to the vertex cover
     *      Remove u, v and all their edges from G
     * 
     * Vertices are ordered by degree; the selected edge is chosen by taking the
     * vertex u with the highest degree, then choosing v where v is the vertex
     * with highest degree in the edges of u.
     * 
     * @param G
     */
    public Set<T> cover(WeightedGraph<T> G) {
        PriorityQueue<T> Q = new PriorityQueue<>(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return G.edgesOf(o2).size() - G.edgesOf(o1).size();
            }
        });
        for (T v : G.V()) {
            Q.add(v);
        }
        Set<T> cov = new HashSet<>();
        while (G.numEdges()> 0) {
            T u = Q.remove();
            T v = null;
            int max_degree = Integer.MIN_VALUE;
            for (WeightedGraph.Edge<T> e : G.edgesOf(u)) {
                if (G.edgesOf(e.v).size() > max_degree) {
                    max_degree = G.edgesOf(e.v).size();
                    v = e.v;
                }
            }
            cov.add(u);
            G.remove(u);
            if (v != null && G.numEdges() > 0) {
                cov.add(v);           
                G.remove(v);
                Q.remove(v);
            }
        }
        return cov;
    }
}
