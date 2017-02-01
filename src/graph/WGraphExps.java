package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WGraphExps<T> extends WeightedGraphDirected<T> {
    
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
                jDim.put(0, (this.edgeTo(vertices.get(i-1), vertices.get(j-1)) != null? 1 : 0));
            }
        }
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <=n; i++) {
                for (int j = 1; j <= n; j++) {
                    int dPrev = memo.get(i).get(j).get(k-1);
                    int dik = memo.get(i).get(k).get(k-1);
                    int dkj = memo.get(k).get(j).get(k-1);
                    memo.get(i).get(j).put(k, dPrev + dik * dkj);
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
    
    // all pairs shortest path (Johnsons algorithm)
    public int apsp() {
        T s = null;
        Set<Edge<T>> sEdges = new HashSet<>();
        for (T v : this.V()) {
            sEdges.add(new Edge<>(v, 0));
        }
        this.rep.put(s, sEdges);
        SingleSourceResult<T> stuff = this.singleSourceShortestPaths(s);
        if (stuff.hasNegativeCycles) {
            System.out.println("Negative cycle detected. Bailing...");
            return Integer.MAX_VALUE;
        }
        System.out.println("No negative cycles");
        Map<T, Integer> sps = stuff.weights;
        for (T u : this.V()) {
            Set<Edge<T>> edges = this.rep.get(u);
            for (Edge<T> edge: edges) {
                int Pu = sps.get(u);
                int Pv = sps.get(edge.v);
                int oldW = edge.d;
                edge.d = (edge.d + Pu - Pv);
                System.out.println("(" + u + ", " + edge.v + ") d.orig=" + oldW + ", reweighting=" + edge.d);
            }
        }
        System.out.println("Finished weighting");
        this.rep.remove(s);
        int minPath = Integer.MAX_VALUE;
        ShortestPathDijkstra<T> sper = new ShortestPathDijkstra<>(this);
        for (T u : this.V()) {           
            for (T v : this.V()) {
                if (u.equals(v)) {
                    continue;
                }
                int duv = sper.sp(u, v);
                int Pu = sps.get(u);
                int Pv = sps.get(v);
                if (minPath > duv - Pu + Pv) {
                    minPath = duv - Pu + Pv;
                }
            }
        }
        System.out.println("sp = " + minPath);
        return minPath;
    } 
    
    // shortest path to the given source. Bellman Ford algorithm.
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
}
