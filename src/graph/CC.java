package graph;

import java.util.HashMap;
import java.util.Map;

public class CC<T> {
    private Map<T, Integer> components;
    
    
    public CC(Graph<T> rep) {
        this.components = new HashMap<>();
        int id = 0;
        for (T v : rep.V()) {
            if (this.components.containsKey(v)) {
                continue;
            }
            DFS<T> g = new DFS(rep, v);
            for (T n : g.visited) {
                this.components.put(n, id);
            }
            id++;
        }
    }
    
    boolean connected(T x, T y) {
        return components.get(x).equals(components.get(y));
    }
    
    int count() {
        return components.values().size();
    }
    
    int id(T x) {
        return components.get(x);
    }
    
}
