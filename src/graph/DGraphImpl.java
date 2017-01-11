package graph;

import java.util.HashSet;

/**
 * A Directed Graph is one where there is a well defined direction between the
 * vertices of the graph. The edges have a direction. For example if there is an
 * edge X -> Y, then it is possible to go from X to Y along this edge, but it is
 * not possible to go from Y to X on the same edge.
 * 
 * @author colm_mchugh
 * @param <T> 
 */
public class DGraphImpl<T> extends UGraphMapImpl<T> implements DGraph<T> {

    @Override
    public Graph<T> reverse() {
        Graph<T> rev = new DGraphImpl<>();
        for (T v : this.V()) {
            for (T u : this.connections(v)) {
                rev.add(u, v);
            }
        }
        return rev;
    }

    @Override
    public void add(T from, T to) {
        this.link(from, to);
        if (!rep.containsKey(to)) {
            rep.put(to, new HashSet<>());
        }
    }

    @Override
    public boolean isAcyclic() {
        for (T v : this.V()) {
            if (this.rep.get(v).isEmpty()) {
                return true;
            }
        }
        return false;
    }   
    
}
