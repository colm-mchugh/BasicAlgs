package graph;

import java.util.Objects;

public interface Flow<T> {

    /**
     * Create an edge from u to v with capacity capacity and flow flow.
     *
     * @param u
     * @param v
     * @param c
     * @param f
     */
    void link(T u, T v, int c, int f);

    /**
     * The vertices in the graph
     *
     * @return
     */
    Iterable<T> V();

    /**
     * The number of vertices in the graph
     */
    public int numVertices();

    static class Edge<T> implements Comparable<Edge<T>> {

        public T u;
        public T v;
        public int capacity;
        public int flow;

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
        public int compareTo(Edge<T> o) {
            if (this.capacity < o.capacity) {
                return -1;
            }
            if (this.capacity > o.capacity) {
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
            return u + " -> " + v + ", " + flow + "/" + capacity;
        }

    }

}
