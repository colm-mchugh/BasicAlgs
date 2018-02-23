package dp;

import graph.WeightedGraph;
import java.util.HashSet;
import java.util.Set;

public class VertexCover<T> {
    
    public final Set<T> NULL_SET = new HashSet<>(); // immutable
    
    public Set<T> cover(WeightedGraph<T> G, int k) {
        if (G.numVertices() == 0) {
            return new HashSet<>();
        }
        if (k == 0 && G.numVertices() > 0) {
            return NULL_SET;
        }
        // Pick an edge (u,v) of G
        T u = null; 
        T v = null;
        do {
            u = G.V().iterator().next();
            WeightedGraph.Edge<T> ev = G.edgesOf(u).iterator().next();
            v = (ev != null ? ev.v : null);
        } while (v != null) ;
        
        // Remove u and its edges from G, recurse with cover size (k - 1)
        Set<WeightedGraph.Edge<T>> uEdges = G.remove(u);
        Set<T> Su = cover(G, k - 1);
        if (Su != NULL_SET && Su.size() == k - 1) {
            Su.add(u);
            return Su;
        }
        
        // Remove v and its edges from G, recurse with cover size (k - 1)
        Set<WeightedGraph.Edge<T>> vEdges = G.restore(u, uEdges).remove(v);
        Set<T> Sv = cover(G, k - 1);
        if (Sv != NULL_SET && Sv.size() == k - 1) {
            Sv.add(v);
            return Sv;
        }
        G.restore(v, vEdges);
        return NULL_SET;
    }
}
