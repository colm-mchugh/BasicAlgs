package graph;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Weighted Graph API
 * 
 * A Weighted Graph is a graph where each edge has a weight. 
 */ 
public interface WeightedGraph<T> {
    
    /**
     * Create an edge between u and v with weight w
     * @param u
     * @param v
     * @param w
     */ 
    void link(T u, T v, int w);
    
    /**
     * An iterable of all vertices in the graph
     * @return 
     */
    Iterable<T> V();
    
    /**
     * The number of vertices in the graph
     */
    public int numVertices();

    /**
     * Return the edges connected to vertex u.
     * 
     * @param u
     * @return 
     */
    Set<Edge<T>> edgesOf(T u);
    
    /**
     * Return true if the graph is covered by the given vertices, 
     * else return false.
     * 
     * A graph g is covered by a vertex set X if X includes all the
     * vertices of the graph g.
     * 
     * @param vertexSet
     * @return 
     */
    boolean isCoveredBy(Collection<T> vertexSet);
    
    /**
     * The cost of a graph is the sum of all its weights
     * @return 
     */
    long cost();
    
    /**
     * Remove the vertex u from the graph and its edges.
     * 
     * Nop if the graph does not contain a vertex u.
     * 
     * TODO: remove this. graphs should be immutable. its only used by the
     * Johnson shortest paths calculator, which should clone the graph instead.
     * 
     * @param u 
     */
    void remove(T u);
    
    /**
     * An edge consists of a weight d and target vertex v
     * An edge is comparable on its weight and identity is its vertex
     * 
     * @param <T> 
     */
    static class Edge<T> implements Comparable<Edge<T>> {

        public T v;
        public int d;

        public Edge(T v, int d) {
            this.v = v;
            this.d = d;
        }

        @Override
        public int compareTo(Edge<T> o) {
            if (this.d < o.d) {
                return -1;
            }
            if (this.d > o.d) {
                return 1;
            }
            return 0;
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
            if (!Objects.equals(this.v, other.v)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return v + ", " + d;
        }

    }
    
}
