package graph.shortestpath;

import graph.WeightedGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Compute the shortest paths between all points of a weighted graph.
 * 
 * Input: a weighted graph G(V, E) that may or may not have a negative cost cycle.
 * Order the vertices V = { 1, 2, ..., N }. The ordering can be arbitrary.
 * V(k) is a prefix { 1, 2, ..., k } of this ordering (k is less than or equal to N).
 * For all i in V, j in V, k in 1..N:
 *      P = shortest i-j path with all internal vertices in V(k)
 * The recurrence has two cases:
 *      1) k is not internal to P => P = shortest i-j path in V(k-1)
 *      2) k is internal to P => P = P1 + P2 where:
 *          - P1 = shortest i-k path in V(k-1)
 *          - P2 = shortest k-j path in V(k-1)
 * 
 * The algorithm uses a memo A to record sub-problems:
 *      A[ i, j, k ] = length of shortest i-j path with all internal vertices in 1..k
 * Initialization of the empty prefix (i.e. 0 internal vertices, k == 0)
 *      A[ i, j, 0 ] = 0 if i==j (the empty path)
 *                   = Wij if E(i,j) (i, j are connected with 0 internal vertices)
 *                   = +Inf otherwise (there's at least 1 internal vertex between i,j)
 * 
 * For k in 1..N (Solve smaller subproblems first)
 *      For i in 1..N
 *          For j in 1..N
 *              A[ i, j, k ] = MIN ( A[i, j, k-1], A[i, k, k-1] + A[k, j, k - 1]
 * 
 * Running time: O(N^3). Note this is independent of the number of edges.
 * 
 * Report a negative-cost cycle:
 * If there is a -ve path length A[i, i, N] for some i in 1..N, then G has a 
 * -ve cost cycle. 
 * 
 * Reconstructing actual paths:
 * Maintain B such that B[i, j] is the highest index of all internal vertices on the
 * shortest i-j path. B[i, j] = k whenever the second case of the recurrence is
 * used for A[i, j, k].
 * When the algorithm has completed, a path s-t can be reconstructed recursively:
 *      s-t = path(s-k) + k + path(k-t) where k = B[s, t]
 * 
 * @param <T> 
 */
public class FloydWarshall<T> {
    final int[][][] A;
    final int N;
    final WeightedGraph<T> G;
    boolean hasNegativeCostCycle;

    public FloydWarshall(WeightedGraph<T> G) {
        this.G = G;
        this.N = G.numVertices();
        this.A = new int[N][N][N];
        this.hasNegativeCostCycle = false;
    }    
    
    public List<Path<T>> apsp() {
        // Define an ordering of the vertices of G
        List<T> ordering = new ArrayList<>(G.numVertices());
        Map<T, Integer> index = new HashMap<>(N);
        for (T v : G.V()) {
            ordering.add(v);
            index.put(v, ordering.size() - 1);
            assert v.equals(ordering.get(index.get(v)));
        }
        // Initialize shortest paths for the empty path (k = 0)
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j][0] = Integer.MAX_VALUE;
            }
            T u = ordering.get(i);
            // For connected vertices u,v the shortest path is the edge weight
            for (WeightedGraph.Edge<T> e : G.edgesOf(u)) {
                A[i][index.get(e.v)][0] = e.d;
            }
            // The shortest path from a vetex to itself is 0
            A[i][i][0] = 0;
        }
        
        // Main algorithm; build up shortest paths for prefix k of the ordering
        // from prefix (k-1). Straightforward application of the recurrence.
        for (int k = 1; k < N; k++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    int dPrev = A[i][j][k - 1];
                    int d1 = A[i][k][k - 1];
                    int d2 = A[k][j][k - 1];
                    int d = Integer.MAX_VALUE;
                    if (d1 != Integer.MAX_VALUE && d2 != Integer.MAX_VALUE) {
                        d = d1 + d2;
                    }
                    A[i][j][k] = Integer.min(dPrev, d);
                }
            }
        }
        // For all pairs of vertices in the ordering, A[i,j,N] gives the shortest
        // path i-j, or no path if it is equal to +Inf.
        List<Path<T>> rv = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                rv.add(new Path<>(ordering.get(i), ordering.get(j), A[i][j][N - 1]));    
            }
            if (A[i][i][N - 1] < 0) {
                hasNegativeCostCycle = true;
            }
        }
        // TODO: path reconstruction.
        return rv;
    }
 
    boolean hasNegCostCycle() {
        return hasNegativeCostCycle;
    }
}
