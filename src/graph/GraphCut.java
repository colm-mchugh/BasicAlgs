package graph;

import java.util.Set;

/**
 * A graph cut is a partition of a graph g into two subsets A and B such that: 
 * A U B == g.V() and A intersection B == {} 
 * crossings() gives the number of edges between A and B
 * 
 * The class does not enforce these conditions.
 *
 * @param <T>
 */
public class GraphCut<T> {

    public Set<T> A;
    public Set<T> B;
    public Graph<T> graph;
    private int crossings;

    public GraphCut(Set<T> A, Set<T> B, Graph<T> graph) {
        this.A = A;
        this.B = B;
        this.graph = graph;
        this.crossings = 0;
    }

    public int crossings() {
        if (crossings == 0) {
            for (T v : A) {
                for (T x : B) {
                    if (graph.connections(v).contains(x)) {
                        crossings++;
                    }
                }
            }
        }
        return crossings;
    }

}
