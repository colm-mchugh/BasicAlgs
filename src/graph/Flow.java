package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class Flow<T> {

    protected final Map<T, Set<Edge<T>>> graph;

    public Flow() {
        graph = new HashMap<>();
    }
    
    public void clear() {
        graph.clear();
    }
    
    /**
     * Create an edge from u to v with capacity c.
     *
     * @param u
     * @param v
     * @param c
     */
    public void link(T u, T v, int c) {
        Edge<T> newEdge = new Edge<>(u, v, c);
        if (linked(newEdge)) {
            throw new IllegalArgumentException("Edge already present: " + newEdge);
        }        
        addToGraph(u, newEdge);
        addToGraph(v, newEdge);
    }
    
    /**
     * The vertices in the graph
     *
     * @return
     */
    public Set<T> V() {
        return graph.keySet();
    }

    /**
     * The number of vertices in the graph
     */
    public int numVertices() {
        return graph.keySet().size();
    }

    /**
     * The edges incident on vertex u.
     * 
     * @param u
     * @return 
     */
    public Set<Edge<T>> edgesOf(T u) {
        return this.graph.get(u);
    }
    
    /**
     * The excess flow at a given vertex
     * 
     * @param u
     * @return 
     */
    public int excess(T w) {
        assert this.graph.keySet().contains(w);       
        int xs = 0;
        for (Edge<T> e : this.graph.get(w)) {
            if (w.equals(e.u)) {
                xs -= e.flow;
            } else {
                xs += e.flow;
            }
        }
        return xs;
    }
    
    private void addToGraph(T v, Edge<T> e) {
        if (!graph.containsKey(v)) {
            graph.put(v, new HashSet<>());
        }
        Set<Edge<T>> vEdges = graph.get(v);
        vEdges.add(e);
    }
    
    private boolean linked(Edge<T> e) {
        Set<Edge<T>> uSet = this.graph.get(e.u);
        Set<Edge<T>> vSet = this.graph.get(e.v);
        if (uSet != null && vSet != null) {
            return uSet.contains(e) && vSet.contains(e);
        }
        return false;
    }

    public static class Edge<T> {

        public T u;
        public T v;
        public int capacity;
        public int flow;

        /**
         * Create an edge from u to v with capacity c.
         * Flow on a newly created edge is 0.
         * 
         * @param u
         * @param v
         * @param c 
         */
        public Edge(T u, T v, int c) {
            this.u = u;
            this.v = v;
            this.capacity = c;
            this.flow = 0;
        }

        public T other(T w) {
            if (w.equals(v)) {
                return u;
            }
            if (w.equals(u)) {
                return v;
            }
            throw new IllegalArgumentException("Invalid endpoint: " + w);
        }

        public int residualCapacity(T w) {
            if (w.equals(v)) {
                return flow;
            }
            if (w.equals(u)) {
                return capacity - flow;
            }
            throw new IllegalArgumentException("Invalid endpoint: " + w);
        }

        public void addResidualFlow(T w, int amount) {
            if (w.equals(v)) {
                flow -= amount;
            } else if (w.equals(u)) {
                flow += amount;
            } else {
                throw new IllegalArgumentException("Invalid endpoint: " + w);
            }
            assert flow >= 0 && flow <= capacity;
        }

        @Override
        public String toString() {
            return "(" + u + ", " + v + ") " + flow + "/" + capacity;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Edge<?> other = (Edge<?>) obj;
            if (this.capacity != other.capacity) {
                return false;
            }
            if (!Objects.equals(this.u, other.u)) {
                return false;
            }
            return Objects.equals(this.v, other.v);
        }

        
    }

    /**
     * Data for a maximum flow in the graph.
     * @param <T> 
     */
    static class Max<T> {
        T source;
        T sink;
        int value;
        Set<T> mincut;
        int augmentations;
                
        public Max(T source, T sink, Flow<T> g) {
            assert g.V().contains(source) && g.V().contains(sink);
            assert !source.equals(sink);
            this.source = source;
            this.sink = sink;
            this.mincut = new HashSet<>(g.numVertices());
            this.value = g.excess(sink);
            this.augmentations = 0;
        }
               
    }
    
    /**
     * Return the maximum flow possible in the graph using the given source and 
     * sink.
     * 
     * @param s The source vertex; must be in the graph
     * @param t The sink vertex; must be in the graph
     * @return 
     */
    abstract Max<T> getMax(T s, T t);
}
