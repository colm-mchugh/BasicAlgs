package greedy;

import clustering.QuickFind;
import clustering.UnionFind;
import graph.WeightedGraph;
import graph.WeightedGraphUndirected;
import java.util.ArrayList;
import java.util.List;
import sort.QuickSorter;

public class KruskalMST<T> {

    static class Edge<T> implements Comparable<Edge<T>> {

        public T p1;
        public T p2;
        public int distance;

        public Edge(T p1, T p2, int distance) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance;
        }

        @Override
        public int compareTo(Edge<T> o) {
            if (distance < o.distance) {
                return -1;
            }
            if (distance > o.distance) {
                return 1;
            }
            return 0;
        }

    }

    private final List<Edge<T>> edges = new ArrayList<>();
    
    public WeightedGraph<T> mst() {
        return kruskalMST();
    }
    
    /**
     * Add an edge (u, v) with weight d to the internal graph representation.
     * 
     * @param u
     * @param v
     * @param d 
     */
    public void addEdge(T u, T v, int d) {
        edges.add(new Edge<>(u, v, d));
    }
    
    /**
     * Create the Minimum Spanning Tree of the graph using Kruskal's algorithm:
     * 
     * MST = {}
     * Sort the edges of the graph in ascending order
     * For each edge (u, v) in the sorted edges:
     *     If u and v are not in the same connected component:
     *          MST += (u, v) 
     *          Put u and v in the same connected component
     * A UnionFind data structure is used to enable O(1) connected component 
     * operations.
     *
     * @return the MST
     */
    private WeightedGraph<T> kruskalMST() {
        Comparable []arr = new Comparable[edges.size()];
        arr = edges.toArray(arr);
        QuickSorter sorter = new QuickSorter();
        sorter.sort(arr);
        UnionFind<T> uf = new QuickFind<>();
        for (Edge<T> edge : edges) {
            uf.addCluster(edge.p1); // potentially adding a vertex more than
            uf.addCluster(edge.p2); // once in this loop
        }
        WeightedGraph<T> MST = new WeightedGraphUndirected<>();
        for (Comparable el : arr) {
            Edge<T> e = (Edge<T>) el;
            if (!uf.find(e.p1, e.p2)) {
                MST.link(e.p1, e.p2, e.distance);
                uf.union(e.p1, e.p2);
            }
        }
        return MST;
    }
    
}
