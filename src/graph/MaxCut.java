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
    
    public GraphCut<T> maxCut(Graph<T> graph) {
        Set<T> A = new HashSet<>(graph.numVertices() / 2);
        Set<T> B = new HashSet<>(graph.numVertices() / 2);
        for (T v : graph.V()) {
            if (RandGen.uniformBool()) {
                A.add(v);
            } else {
                B.add(v);
            }
        }
        boolean done = false;  
        while (!done) {
            done = true;
            for (T u : graph.V()) {
                Set<T> s = (A.contains(u) ? A : B);
                int nedges = graph.connections(u).size();
                int nedgesIn = edgesIn(u, graph, s);
                int nedgesCrossing = nedges - nedgesIn;
                if (nedgesIn > nedgesCrossing) {
                    Set<T> other = (A.contains(u) ? B : A);
                    s.remove(u);
                    other.add(u);
                    done = false;
                }
            }
        }
        return new GraphCut<>(A, B, graph);
    }
    
    int edgesIn(T u, Graph<T> G, Set<T> s) {
        int edges = 0;
        for (T v : G.connections(u)) {
            edges += (s.contains(v) ? 1 : 0);
        }
        return edges;
    }
}
