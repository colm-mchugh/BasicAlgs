package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WeightedGraphDirected<T> implements WeightedGraph<T> {

    protected final Map<T, Set<Edge<T>>> rep = new HashMap<>();

    @Override
    public void link(T u, T v, int w) {
        Set<Edge<T>> e = this.edgesOf(u);
        e.add(new Edge<>(v, w));
        // Add v to the vertices if its not already
        if (this.rep.get(v) == null) {
            this.rep.put(v, new HashSet<>());
        } 
    }

    @Override
    public  Set<Edge<T>> edgesOf(T u) {
        Set<Edge<T>> edges = this.rep.get(u);
        if (edges == null) {
            edges = new HashSet<>();
            this.rep.put(u, edges);
        }
        return edges;
    }

    @Override
    public int numVertices() {
        return this.rep.keySet().size();
    }
    
    @Override
    public Iterable<T> V() {
        return this.rep.keySet();
    }

    @Override
    public boolean isCoveredBy(Collection<T> vertexSet) {
        return vertexSet.containsAll(this.rep.keySet());
    }

    @Override
    public long cost() {
        long theCost = 0;
        for (Set<Edge<T>> edgeSet : this.rep.values()) {
            for (Edge<T> edge : edgeSet) {
                theCost += edge.d;
            }
        }
        return theCost;
    }

    @Override
    public void remove(T u) {
        this.rep.remove(u);
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
                    int newW = rv.weights.get(u);
                    if (newW == Integer.MAX_VALUE) {
                        continue;
                    }
                    newW += edge.d;
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
            StringBuffer sb = new StringBuffer();
            sb.append('(');
            sb.append(u);
            sb.append(", ");
            sb.append(v);
            sb.append(')');
            if (d == Integer.MAX_VALUE) {
                sb.append( " NO PATH");
            } else {
                sb.append(" d=");
                sb.append(d);
            }
            return sb.toString();
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
                    T u = vertices.get(i - 1);
                    T v = vertices.get(j - 1);
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
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    int dPrev = memo.get(i).get(j).get(k - 1);
                    int dik = memo.get(i).get(k).get(k - 1);
                    int dkj = memo.get(k).get(j).get(k - 1);
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
                rv.add(new ShortestPathResult<>(vertices.get(i - 1), vertices.get(j - 1), dMin));
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
            for (Edge<T> edge : edges) {
                int Pu = sps.get(u);
                int Pv = sps.get(edge.v);
                edge.d = (edge.d + Pu - Pv);
            }
        }
        System.out.println("Finished weighting");
        this.rep.remove(s);
        int minPath = Integer.MAX_VALUE;
        int count = 0;
        ShortestPathDijkstra<T> sper = new ShortestPathDijkstra<>(this);
        for (T u : this.V()) {
            for (T v : this.V()) {
                count++;
                if (u.equals(v)) {
                    continue;
                }
                int duv = sper.sp(u, v);
                if (duv == Integer.MAX_VALUE) {
                    continue;
                }
                int Pu = sps.get(u);
                int Pv = sps.get(v);
                if (minPath > duv - Pu + Pv) {
                    minPath = duv - Pu + Pv;
                }
            }
            if (count % 5000 == 0) {
                System.out.print('.');
            }
            if (count % 100000 == 0) {
                System.out.println();
            }
        }
        System.out.println("sp = " + minPath);
        return minPath;
    }

}
