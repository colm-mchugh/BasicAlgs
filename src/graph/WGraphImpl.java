package graph;


import heap.MinHeap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WGraphImpl<T> implements WeightedGraph<T> {

    static class Edge<T> implements Comparable<Edge<T>> {
        T v;
        int d;

        public Edge(T v, int d) {
            this.v = v;
            this.d = d;
        }

        @Override
        public int compareTo(Edge<T> o) {
            if (this.d < o.d) {
                return -1;
            }
            if (this.d > o.d) {
                return 1;
            }
            return 0;
        }  

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Edge<?> other = (Edge<?>) obj;
            if (!Objects.equals(this.v, other.v)) {
                return false;
            }
            return true;
        }      

        @Override
        public String toString() {
            return v + ", " + d;
        }
        
        
    }
    
    private final Map<T, Set<Edge<T>>> rep = new HashMap<>();

    @Override
    public void link(T u, T v, int d) {
        Set<Edge<T>> e = this.edgesOf(u);
        e.add(new Edge<>(v, d));
        this.edgesOf(v); // Add v to the vertices if its not already 
    }

    private Set<Edge<T>> edgesOf(T u) {
        Set<Edge<T>> edges = this.rep.get(u);
        if (edges == null) {
            edges = new HashSet<>();
            this.rep.put(u, edges);
        }
        return edges;
    }
    
    public int sp(T u, T v) {
        T s = u;
        Set<T> X = new HashSet<>();
        Map<T, Integer> d = new HashMap<>();
        d.put(s, 0);
        MinHeap<Edge<T>> heap = new MinHeap<>(); 
        for (T t : this.V()) {
            if (!t.equals(u)) {
                heap.Insert(new Edge<>(t, Integer.MAX_VALUE));
            }
        }
        X.add(u);
        computeKeys(heap, X, u, d);
        while (!s.equals(v)) {
            Edge<T> w = heap.Delete();
            d.put(w.v, w.d);
            s = w.v;
            X.add(w.v);
            computeKeys(heap, X, w.v, d);
        }  
        return d.get(s);
    }

    private void computeKeys(MinHeap<Edge<T>> heap, Set<T> X, T w, Map<T, Integer> d) {
        for (Edge<T> e : this.edgesOf(w)) {
            if (!X.contains(e.v)) {
                Edge<T> heapV = heap.DeleteSpecificKey(e);
                heapV.d = Integer.min(heapV.d, d.get(w) + e.d);
                heap.Insert(heapV);
            }
        }
    }

    
    @Override
    public int numVertices() {
        return this.rep.keySet().size();
    }
    
    @Override
    public Iterable<T> V() {
        return this.rep.keySet();
    }
    
    
}
