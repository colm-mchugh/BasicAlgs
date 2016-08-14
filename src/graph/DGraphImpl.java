package graph;

import java.util.HashSet;

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
