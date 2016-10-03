 package graph;


import heap.MinHeap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    
    protected final Map<T, Set<Edge<T>>> rep = new HashMap<>();

    @Override
    public void link(T u, T v, int d) {
        Set<Edge<T>> e = this.edgesOf(u);
        e.add(new Edge<>(v, d));
        this.edgesOf(v); // Add v to the vertices if its not already 
    }

    protected Set<Edge<T>> edgesOf(T u) {
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
        List<T> path = new ArrayList<>();
        d.put(s, 0);
        MinHeap<Edge<T>> heap = new MinHeap<>(); 
        for (T t : this.V()) {
            if (!t.equals(u)) {
                heap.Insert(new Edge<>(t, Integer.MAX_VALUE));
            }
        }
        X.add(u);
        path.add(u);
        computeKeys(heap, X, u, d);
        while (!s.equals(v)) {
            Edge<T> w = heap.Delete();
            d.put(w.v, w.d);
            s = w.v;
            path.add(s);
            X.add(w.v);
            computeKeys(heap, X, w.v, d);
        }  
        path.add(v);
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
    
    public static class SingleSourceResult<T> {
        T source;
        boolean hasNegativeCycles;
        Map<T, Integer> weights;
        Map<T, T> preds;

        public SingleSourceResult(T source, int size) {
            this.source = source;
            this.hasNegativeCycles = false;
            this.weights = new HashMap<>(size);
            this.preds = new HashMap<>(size);
        }       
    }
    
    @Override
    public SingleSourceResult<T> singleSourceShortestPaths(T s) {
        if (rep.get(s) == null) {
            throw new IllegalArgumentException("No such vertex: " + s);
        }
        SingleSourceResult<T> rv = new SingleSourceResult<>(s, rep.size());
        for (T v : this.V()) {
            rv.weights.put(v, Integer.MAX_VALUE);
            rv.preds.put(v, null);
        }
        rv.weights.put(s, 0);
        int numVertices = this.numVertices();
        boolean shortCircuit = true;
        for (int i = 0; i < numVertices; i++) {
            shortCircuit = true;
            for (T u : this.rep.keySet()) {
                Set<Edge<T>> edges = this.rep.get(u);
                for (Edge<T> edge : edges) {
                    int newW = rv.weights.get(u) + edge.d;
                    if (rv.weights.get(edge.v) > newW) {
                        rv.weights.put(edge.v, newW);
                        rv.preds.put(edge.v, u);
                        shortCircuit = false;
                    }
                 }
            }
        }
        if (!shortCircuit) {
            for (T u : this.rep.keySet()) {
                Set<Edge<T>> edges = this.rep.get(u);
                for (Edge<T> edge : edges) {
                    int newW = rv.weights.get(u) + edge.d;
                    if (rv.weights.get(edge.v) > newW) {
                        rv.hasNegativeCycles = true;
                    }
                 }
            }
        }
        return rv;
    }
    
    public static class ShortestPathResult<T> {
        T u; 
        T v;
        int d;

        public ShortestPathResult(T u, T v, int d) {
            this.u = u;
            this.v = v;
            this.d = d;
        }

        @Override
        public String toString() {
            return "(" + u + ", " + v + "), d=" + d;
        }
        
        
    }
    
    @Override
    public List<ShortestPathResult<T>> allPairsShortestPaths() {
        List<T> vertices = new ArrayList<>(this.rep.keySet());
        int n = this.numVertices();
        
        Map<Integer, Map<Integer, Map<Integer, Integer>>> memo = new HashMap<>(this.numVertices());
        for (int i = 1; i <= n; i++) {
            if (memo.get(i) == null) {
                memo.put(i, new HashMap<>());
            }
            Map<Integer, Map<Integer, Integer>> iDim = memo.get(i);
            for (int j = 1; j <= n; j++) {
                if (iDim.get(j) == null) {
                    iDim.put(j, new HashMap<>());
                }
                Map<Integer, Integer> jDim = iDim.get(j);
                if (i == j) {
                    jDim.put(0, 0);
                } else {
                    T u = vertices.get(i-1);
                    T v = vertices.get(j-1);
                    Edge<T> edge = this.edgeTo(u, v);
                    if (edge != null) {
                        jDim.put(0, edge.d);
                    } else {
                        jDim.put(0, Integer.MAX_VALUE);
                    }
                }
            }
        }
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <=n; i++) {
                for (int j = 1; j <= n; j++) {
                    int dPrev = memo.get(i).get(j).get(k-1);
                    int dik = memo.get(i).get(k).get(k-1);
                    int dkj = memo.get(k).get(j).get(k-1);
                    int dPlus = Integer.MAX_VALUE;
                    if (dik != Integer.MAX_VALUE && dkj != Integer.MAX_VALUE) {
                        dPlus = dik + dkj;
                    }
                    memo.get(i).get(j).put(k, Integer.min(dPrev, dPlus));
                }
            }
        }
        List<ShortestPathResult<T>> rv = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                int dMin = memo.get(i).get(j).get(n);
                rv.add(new ShortestPathResult<>(vertices.get(i-1), vertices.get(j-1), dMin));
            }
        }
        return rv;
    }

    protected Edge<T> edgeTo(T u, T v) {
        Edge<T> rv = null;
        Set<Edge<T>> edges = this.rep.get(u);
        for (Edge<T> edge : edges) {
            if (edge.v.equals(v)) {
                rv = edge; 
                break;
            }
        }
        return rv;
    }

    @Override
    public void apsp() {
        T s = null;
        Set<Edge<T>> sEdges = new HashSet<>();
        for (T v : this.V()) {
            sEdges.add(new Edge<>(v, 0));
        }
        this.rep.put(s, sEdges);
        SingleSourceResult<T> stuff = this.singleSourceShortestPaths(s);
        if (stuff.hasNegativeCycles) {
            System.out.println("Negative cycle detected. Bailing...");
            return;
        }
        System.out.println("No negative cycles");
        Map<T, Integer> sps = stuff.weights;
        for (T u : this.V()) {
            Set<Edge<T>> edges = this.rep.get(u);
            for (Edge<T> edge: edges) {
                int Pu = sps.get(u);
                int Pv = sps.get(edge.v);
                edge.d = (edge.d + Pu - Pv);
            }
        }
        System.out.println("Finished weighting");
        this.rep.remove(s);
        int minPath = Integer.MAX_VALUE;
        for (T u : this.V()) {           
            for (T v : this.V()) {
                if (u.equals(v)) {
                    continue;
                }
                int duv = this.sp(u, v);
                int Pu = sps.get(u);
                int Pv = sps.get(v);
                if (minPath > duv - Pu + Pv) {
                    minPath = duv - Pu + Pv;
                }
            }
        }
        System.out.println("sp = " + minPath);
    }    
    

}
