package greedy;

import graph.WeightedGraph;
import heap.MinHeap;
import java.util.HashSet;
import java.util.Set;

public class PrimMST<T> {
    
    private WeightedGraph<T> graph;

    public PrimMST(WeightedGraph<T> graph) {
        this.graph = graph;
    }

    public WeightedGraph<T> getGraph() {
        return graph;
    }

    public void setGraph(WeightedGraph<T> graph) {
        this.graph = graph;
    }
    
    public long mstCost() {
        return slowMstCost();
    }
    
    private long slowMstCost() {
        long rv = 0;
        Set<T> X = new HashSet<>();
        for (T v : graph.V()) {
            if (X.isEmpty()) {
                X.add(v);
                continue;
            }
            if (graph.hasAll(X)) {
                break;
            }
            int nextMin = Integer.MAX_VALUE;
            T nextEdge = null;
            for (T u : X) {
                for (WeightedGraph.Edge<T> e : graph.edgesOf(u)) {
                    if (!X.contains(e.v)) {
                        if (nextMin > e.d) {
                            nextMin = e.d;
                            nextEdge = e.v;
                        }
                    }
                }
            }
            X.add(nextEdge);
            rv += nextMin;
         }
        return rv;
    }
    
    private long heapMstCost() {
        long rv = 0;
        Set<T> X = new HashSet<>();
        T v = graph.V().iterator().next();
        X.add(v);
        MinHeap<WeightedGraph.Edge<T>> heap = initHeap(X); 
        while (!graph.hasAll(X)) {
            WeightedGraph.Edge<T> next = heap.Delete();
            rv += next.d;
            X.add(next.v);
            // readjust cost of all vertices in heap that have an edge with next.v
        }
        return rv;
    }

    private MinHeap<WeightedGraph.Edge<T>> initHeap(Set<T> X) {
        MinHeap<WeightedGraph.Edge<T>> heap = new MinHeap<>();
        for (T v : graph.V()) {
            if (!X.contains(v)) {
                int cost = Integer.MAX_VALUE;
                for (WeightedGraph.Edge<T> u : graph.edgesOf(v)) {
                    if (X.contains(u.v)) {
                        cost = Integer.min(cost, u.d);
                    }
                }
                heap.Insert(new WeightedGraph.Edge(v, cost));
            }
        }
        return heap;
    }
}
