package graph;

import heap.MinHeap;
import java.util.HashSet;
import java.util.Set;

public class WUGraphImpl<T> extends WGraphImpl<T> {

    @Override
    public void link(T u, T v, int d) {
        super.link(u, v, d); 
        super.link(v, u, d);
    }
    
    int mstCost() {
        return slowMstCost();
    }
    
    int slowMstCost() {
        int rv = 0;
        Set<T> X = new HashSet<>();
        for (T v : this.V()) {
            if (X.isEmpty()) {
                X.add(v);
                continue;
            }
            if (X.containsAll(this.rep.keySet())) {
                break;
            }
            int nextMin = Integer.MAX_VALUE;
            T nextEdge = null;
            for (T u : X) {
                for (Edge<T> e : this.edgesOf(u)) {
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
    
    int heapMstCost() {
        int rv = 0;
        Set<T> X = new HashSet<>();
        T v = this.V().iterator().next();
        X.add(v);
        MinHeap<Edge<T>> heap = initHeap(X); 
        while (!X.containsAll(this.rep.keySet())) {
            Edge<T> next = heap.Delete();
            rv += next.d;
            X.add(next.v);
            // readjust cost of all vertices in heap that have an edge with next.v
        }
        return rv;
    }

    private MinHeap<Edge<T>> initHeap(Set<T> X) {
        MinHeap<Edge<T>> heap = new MinHeap<>();
        for (T v : this.V()) {
            if (!X.contains(v)) {
                int cost = Integer.MAX_VALUE;
                for (Edge<T> u : this.edgesOf(v)) {
                    if (X.contains(u.v)) {
                        cost = Integer.min(cost, u.d);
                    }
                }
                heap.Insert(new Edge(v, cost));
            }
        }
        return heap;
    }
}
