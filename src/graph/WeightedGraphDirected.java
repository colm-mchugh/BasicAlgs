package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
