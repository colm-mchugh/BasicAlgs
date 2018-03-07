package graph;

import java.util.HashSet;
import java.util.Set;
import utils.RandGen;

/**
 * Approximation for Maximum Cut.
 * 
 * Given a graph G(V,E) a maximum cut is a partition of V into A, B such that 
 * the number of edges between A and B is maximized.
 * 
 * Algorithm:
 * Divide the set of vertices into two sets A, B. Randomly decide set membership.
 * Repeat until no improvements can be made:
 * If the size of the cut A - B can be improved(*) by moving a vertex u to the other set, 
 * then move it. 
 * 
 * (*) If a given vertex u has more edges within its set than crossing the cut, 
 * then moving it will improve the cut.
 */
public class MaxCut<T> {
    
    public GraphCut<T> maxCut(Graph<T> G) {
        Set<T> A = new HashSet<>(G.numVertices() / 2);
        Set<T> B = new HashSet<>(G.numVertices() / 2);
        for (T v : G.V()) {
            (RandGen.uniformBool() ? A : B).add(v);
        }         
        for (boolean not_done = true; not_done;) {
            not_done = false;
            for (T u : G.V()) {
                Set<T> S = (A.contains(u) ? A : B);
                int nedgesIn = edgesIn(u, G, S);
                if (nedgesIn > G.connections(u).size() - nedgesIn) {
                    (A.contains(u) ? B : A).add(u);
                    S.remove(u);
                    not_done = true;
                }
            }
        }
        return new GraphCut<>(A, B, G);
    }
    
    int edgesIn(T u, Graph<T> G, Set<T> s) {
        int edges = 0;
        for (T v : G.connections(u)) {
            edges += (s.contains(v) ? 1 : 0);
        }
        return edges;
    }
}
