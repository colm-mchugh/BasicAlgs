package greedy;

import graph.WeightedGraph;
import graph.WeightedGraph.Edge;
import graph.WeightedGraphUndirected;
import heap.MinHeap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrimMST<T> {
    
    private WeightedGraph<T> g = null;
    private Long computedCost = Long.MIN_VALUE;
    
    public PrimMST(WeightedGraph<T> graph) {
        assert g.numVertices() > 0;
        this.g = graph;
    }

    /**
     * Return the graph that this PrimMST operates on.
     * 
     * @return 
     */
    public WeightedGraph<T> getGraph() {
        return g;
    }

    /**
     * Give this PrimMST a new graph to operate on.
     * 
     * @param graph 
     */
    public void setGraph(WeightedGraph<T> graph) {
        this.g = graph;
    }
    
    /**
     * Return the pre-computed cost of the MST.
     * 
     * Note that this may be uninitialized, in which case it will have
     * the value Long.MIN_VALUE
     * 
     * @return 
     */
    public long mstCost() {
        //return this.computedCost;
        return this.heapMstCost();
    }
    
    /**
     * Return the Minimum Spanning Tree of g 
     */ 
    public WeightedGraph<T> mst() {
        return slowMst();
    }
    
    /**
     * Constructs the Minimum Spanning Tree (MST) of g using the following algorithm:
     *      MST = empty graph
     *      X = { a single vertex from g }  note: can be any vertex
     *      while (X != g.V())
     *          (u,v) = cheapest edge of g where u is in X and v is not in X
     *          MST += (u,v)
     *          X += v
     * 
     * It's called slow because it is O( N * M ) where N = #vertices, M = #edges
     * 
     * @return the graph that is the minimum spanning tree of g
     */
    private WeightedGraph<T> slowMst() {
        WeightedGraph<T> MST = new WeightedGraphUndirected<>();
        Set<T> X = new HashSet<>();
        X.add(g.V().iterator().next()); // Put a random vertex of g into X
        long runningCost = 0;
        while (!g.isCoveredBy(X)) {
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
            runningCost += nextEdge.d;
         }
        this.computedCost = runningCost;
        return MST;
    }
    
    /**
     * Constructs the MST cost in O(M log N) time using a minHeap of the vertices
     * not yet processed. The algorithm is as follows:
     *     X = { a single vertex of g }
     *     H = Minheap of all vertices g.V - X, where the key of each entry is the
     *         cost of the cheapest edge (u, v) where u is in X and v is in g.V - X,
     *         or +Inf if no such edge exists. 
     *     While (X != g.V)
     *          Remove min vertex v from H, add it to X, add its key to cost
     *          For all edges (v, w) where w is in g.V - X, recompute its cost
     *          in the heap.
     * 
     * @return 
     */
    private long heapMstCost() {
        long runningCost = 0;
        Set<T> X = new HashSet<>();
        T seedVertex = g.V().iterator().next(); // put one vertex into X
        X.add(seedVertex);
        MinHeap<WeightedGraph.Edge<T>> heap = initHeap(X, seedVertex); 
        while (!g.isCoveredBy(X)) {
        WeightedGraph.Edge<T> next = heap.Delete();
            runningCost += next.d;
            X.add(next.v);
            // readjust cost of all vertices in heap that have an edge with next.v
            for (Edge<T> e : g.edgesOf(next.v)) {
                if (!X.contains(e.v)) {
                    Edge<T> eCurr = heap.DeleteSpecificKey(e);
                    eCurr.d = Integer.min(eCurr.d, e.d);
                    heap.Insert(eCurr);
                }
            }
        }
        return runningCost;
    }

    private MinHeap<WeightedGraph.Edge<T>> initHeap(Set<T> X, T seedVertex) {
        MinHeap<WeightedGraph.Edge<T>> heap = new MinHeap<>();
        Set<Edge<T>> xEdges = g.edgesOf(seedVertex);
        // As a convenience, create a map of the edges of the seed vertex
        // where the key is the other vertex in the edge and value the cost of
        // the edge. This will enable us to retrieve the cost of edges of the
        // seedVertex in O(1) when initializing the heap.
        Map<T, Integer> Xcosts = new HashMap<>(xEdges.size());
        for (Edge<T> edge : xEdges) {
            Xcosts.put(edge.v, edge.d);
        }
        // for each of the vertices in g, insert an edge (v, cost) into the heap
        // where cost is +Inf if v does not share an edge with seedVertex, else
        // cost is the cost of the edge that v does share with seedVertex.
        for (T v : g.V()) {
            if (!X.contains(v)) {
                Edge<T> nextEdge = new WeightedGraph.Edge(v, Integer.MAX_VALUE);
                if (Xcosts.containsKey(nextEdge.v)) {
                    nextEdge.d = Integer.min(nextEdge.d, Xcosts.get(nextEdge.v));
                }
                heap.Insert(nextEdge);
            }
        }
        return heap;
    }
}
