package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WeightedGraphDirected<T> implements WeightedGraph<T> {

    protected final Map<T, Set<Edge<T>>> rep = new HashMap<>();
    protected int nEdges = 0;
    
    @Override
    public void link(T u, T v, int w) {
        Set<Edge<T>> e = this.edgesOf(u);
        e.add(new Edge<>(v, w));
        // Add v to the vertices if its not already
        if (this.rep.get(v) == null) {
            this.rep.put(v, new HashSet<>());
        }
        nEdges++;
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
    public int numEdges() {
        return nEdges;
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
    public Set<Edge<T>> remove(T u) {
        Set<Edge<T>> myEdges = this.rep.remove(u);
        Edge<T> uEdge = new Edge<>(u, -1);
        for (Set<Edge<T>> edges : this.rep.values()) {
            if (edges != myEdges) {
                edges.remove(uEdge);
            }
        }
        nEdges -= myEdges.size();
        return myEdges;
    }

    @Override
    public WeightedGraph<T> restore(T u, Set<Edge<T>> v) {
        this.rep.put(u, v);
        nEdges += v.size();
        return this;
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
            StringBuilder sb = new StringBuilder();
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

}
