package graph;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public interface Flow<T> {

    /**
     * Create an edge from u to v with capacity c.
     *
     * @param u
     * @param v
     * @param c
     */
    void link(T u, T v, int c);

    /**
     * The vertices in the graph
     *
     * @return
     */
    Set<T> V();

    /**
     * The number of vertices in the graph
     */
    public int numVertices();

    /**
     * The edges incident on vertex u.
     * 
     * @param u
     * @return 
     */
    Set<Edge<T>> edgesOf(T u);
    
    /**
     * The excess flow at a given vertex
     * 
     * @param u
     * @return 
     */
    int excess(T u);
    
    static class Edge<T> {

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
            return u + " -> " + v + ", " + flow + "/" + capacity;
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
    Max<T> getMax(T s, T t);
}
