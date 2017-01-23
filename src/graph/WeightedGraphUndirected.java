package graph;

/**
 * A weighted undirected graph is a weighted directed graph with the link 
 * method redefined to create bidirectional edges.
 * 
 * @author colm_mchugh
 * @param <T> 
 */
public class WeightedGraphUndirected<T> extends WeightedGraphDirected<T> {

    @Override
    public void link(T u, T v, int d) {
        super.link(u, v, d); 
        super.link(v, u, d);
    }

    @Override
    public long cost() {
        return super.cost() / 2; //Because links are bidirectional in an undirected graph
    }
    
    
}
