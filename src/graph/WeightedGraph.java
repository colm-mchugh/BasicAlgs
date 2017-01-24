package graph;

import java.util.Collection;
import java.util.List;
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
     * The length of the shortest path between u and v
     * @param u
     * @param v
     * @return 
     */
    int sp(T u, T v);
    
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
     * A graph is covered by a vertex set X if X includes all the
     * vertices of the graph.
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
 
    WeightedGraphDirected.SingleSourceResult<T> singleSourceShortestPaths(T s);
    
    List<WeightedGraphDirected.ShortestPathResult<T>> allPairsShortestPaths();
    
    int apsp();
}
