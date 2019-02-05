package graph.flow;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Graph<T> {
    
    public static class Edge<T> {
        public T v; // edge tail
        public int cap; // edge capacity

        public Edge(T v, int cap) {
            this.v = v;
            this.cap = cap;
        }
        
        
    }
    
    private final Map<T, Map<T, Edge<T>>> rep;

    public Graph() {
        rep = new HashMap<>();
    }
    
    public Collection<Edge<T>> edgesOf(T v) {
        if (!rep.containsKey(v)) {
            return null;
        }    
        Map<T, Edge<T>> edgeMap = rep.get(v);
        Collection<Edge<T>> edges = edgeMap.values();
        return edges;
    }
 
    public Edge<T> reverseEdge(T u, T v) {
        if (!rep.containsKey(v)) {
            return null;
        }     
        Map<T, Edge<T>> edgeMap = rep.get(v);
        Edge<T> revEdge = edgeMap.get(u);
        return revEdge;
    }
    
    public int numVertices() {
        return rep.size();
    }
    
    public Set<T> Vertices() {
        return rep.keySet();
    }
    
    public void link(T u, T v, int cap) {
        if (!rep.containsKey(u)) {
            rep.put(u, new HashMap<>());
        }
        Map<T, Edge<T>> edges = this.rep.get(u);
        edges.put(v, new Edge<>(v, cap));
    }
    
}
