package graph;

import java.util.Set;

/**
 * A weighted undirected graph is a weighted directed graph with the link 
 * method redefined to create bidirectional edges.
 * 
 * @param <T> 
 */
public class WeightedGraphUndirected<T> extends WeightedGraphDirected<T> {

    @Override
    public void link(T u, T v, int d) {
        super.link(u, v, d); 
        super.link(v, u, d);
        nEdges--;
    }

    @Override
    public long cost() {
        return super.cost() / 2; //Because links are bidirectional in an undirected graph
    }

    @Override
    public Set<Edge<T>> remove(T u) {
        Set<Edge<T>> edges = this.rep.remove(u);
        Edge<T> uEdge = new Edge<>(u, -1);
        for (Edge<T> e : edges) {
            this.edgesOf(e.v).remove(uEdge);
        }
        nEdges -= edges.size();
        return edges;
    }

    @Override
    public WeightedGraph<T> restore(T u, Set<Edge<T>> v) {
        this.rep.put(u, v);
        for (Edge<T> e : v) {
            this.link(e.v, u, e.d);
        }
        return this;
    }
}
