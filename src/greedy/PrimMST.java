package greedy;

import graph.WeightedGraph;
import graph.WeightedGraph.Edge;
import graph.WeightedGraphUndirected;
import heap.MinHeap;
import java.util.HashSet;
import java.util.Set;

/**
 * Compute the Minimum Spanning Tree (MST) of a given Undirected Weighted Graph
 * using Prim's algorithm.
 *
 * A Spanning Tree of an undirected weighted graph G is a subgraph of G that
 * contains no cycles, and is connected (includes all the vertices of G). The
 * Minimum Spanning Tree (MST) of G is the spanning tree that has the least cost
 * of all the spanning trees of G.
 *
 * The algorithm depends on the following properties of a undirected graph G: A
 * cut A,B of G is a partition of its vertices into subsets A and B such that
 * for all v E A, v !E B and for all u E B, u !E A. Empty Cut Lemma: G is not
 * connected <=> there exists a cut A, B of G such that 0 edges cross A and B.
 * Double Crossing Lemma: if a cycle C of G has an edge E1 that crosses a cut
 * A,B then it must have at least one other edge E2 that also crosses A,B.
 * Lonely Cut Lemma: if there is exactly one edge crossing a cut A,B then it
 * cannot be part of a cycle. Cut property: Given a cut A,B of G, if E is the
 * cheapest edge that crosses A,B then E is in the MST of G.
 *
 */
public class PrimMST<T> {

    private WeightedGraph<T> g = null;

    public PrimMST(WeightedGraph<T> graph) {
        assert graph.numVertices() > 0;
        this.g = graph;
    }

    /**
     * slowMST(): return the Minimum Spanning Tree (MST) of g. MST = empty graph
     * X = { a single vertex from g, randomly chosen } while (X != g.V()) (u,v)
     * = cheapest edge of g s.t. u E X and v !E X MST += (u,v) X += v
     *
     * Running time: O( N * M ) where N = #vertices, M = #edges
     *
     * @return the graph that is the minimum spanning tree of g
     */
    public WeightedGraph<T> slowMst() {
        WeightedGraph<T> MST = new WeightedGraphUndirected<>();
        Set<T> X = new HashSet<>();
        X.add(g.V().iterator().next()); // Put a random vertex of g into X
        while (X.size() < g.numVertices()) {
            Edge<T> nextEdge = new Edge<>(null, Integer.MAX_VALUE);
            T fromVertex = null;
            for (T u : X) {
                for (WeightedGraph.Edge<T> e : g.edgesOf(u)) {
                    if (!X.contains(e.v) && (nextEdge.d > e.d)) {
                        nextEdge = e;
                        fromVertex = u;
                    }
                }
            }
            X.add(nextEdge.v);
            MST.link(fromVertex, nextEdge.v, nextEdge.d);
        }
        return MST;
    }

    /**
     * heapMST(): return the MST cost of an undirected, weighted graph g.
     *
     * Runs in O(M log N) time using a minHeap of the vertices not yet processed:
     *     X = { a single vertex of g }
     *     gMinusX = heap of all vertices g - X, where the key of each entry is the
     *         cost of the cheapest edge (u, v) where u is in X and v is in g.V - X,
     *         or +Inf if no such edge exists. 
     *     While (X != g.V)
     *          Remove min vertex v from H, add it to X, add its key to cost
     *          For all edges (v, w) where w is in g - X, recompute its cost
     *          in the heap.
     *
     * The heap gMinusX maintains two invariants:
     * 1) It contains all the vertices not in the MST, i.e. vertices of g - X
     * 2) The minimum element in gMinusX gives the cost of the cheapest edge (u,v)
     *    where u E X and v E g - X
     *
     * When a vertex v is transferred to X, the vertices of the edges of v that
     * are not in X need to be reentered into gMinusX because they now cross
     * between the cuts X and g - X.
     *
     * @return
     */
    public long heapMST() {
        long cost = 0;
        Set<T> X = new HashSet<>();
        T seed = g.V().iterator().next();
        X.add(seed);
        MinHeap<WeightedGraph.Edge<T>> gMinusX = new MinHeap<>();
        Set<Edge<T>> seedEdges = g.edgesOf(seed);
        for (Edge<T> e : seedEdges) {
            gMinusX.Insert(new WeightedGraph.Edge<>(e.v, e.d));
        }
        Edge<T> vEntry = new WeightedGraph.Edge<>(null, Integer.MAX_VALUE);
        for (T v : g.V()) {
            if (v.equals(seed)) {
                continue;
            }
            vEntry.v = v;
            if (seedEdges.contains(vEntry)) {
                continue;
            }
            gMinusX.Insert(new WeightedGraph.Edge<>(v, Integer.MAX_VALUE));
        }
        assert (X.size() == 1);
        assert (gMinusX.size() == g.numVertices() - 1);
        while (X.size() != g.numVertices()) {
            WeightedGraph.Edge<T> next = gMinusX.Delete();
            cost += next.d;
            X.add(next.v);
            Set<Edge<T>> nextEdges = g.edgesOf(next.v);
            for (Edge<T> w : nextEdges) {
                if (X.contains(w.v)) {
                    continue;
                }
                WeightedGraph.Edge<T> heapW = gMinusX.DeleteSpecificKey(w);
                heapW.d = Integer.min(heapW.d, w.d);
                gMinusX.Insert(heapW);
            }
        }
        return cost;
    }

}
