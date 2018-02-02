package greedy;

import clustering.LazyUnion;
import clustering.QuickFind;
import clustering.UnionFind;
import graph.WeightedGraph;
import graph.WeightedGraphUndirected;
import java.util.ArrayList;
import java.util.List;
import sort.QuickSorter;

public class KruskalMST<T> {

    static class Edge<T> implements Comparable<Edge<T>> {

        public T u;
        public T v;
        public int weight;

        public Edge(T p1, T p2, int distance) {
            this.u = p1;
            this.v = p2;
            this.weight = distance;
        }

        @Override
        public int compareTo(Edge<T> o) {
            if (weight < o.weight) {
                return -1;
            }
            if (weight > o.weight) {
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
     * Create the Minimum Spanning Tree of the graph using Kruskals algorithm:
     * 
     * MST = {}
     * Put each vertex in its own connected component
     * Sort the edges of the graph in ascending order
     * For each edge (u, v) of the sorted edges:
     *     If u and v are not in the same connected component:
     *          MST += (u, v) 
     *          Put u and v in the same connected component
     *
     * @return the MST
     */
    private WeightedGraph<T> kruskalMST() {
        Comparable []arr = new Comparable[edges.size()];
        arr = edges.toArray(arr);
        QuickSorter sorter = new QuickSorter();
        sorter.sort(arr);
        UnionFind<T> uf = new LazyUnion<>();
        for (Edge<T> edge : edges) {
            uf.addCluster(edge.u); 
            uf.addCluster(edge.v); 
        }
        WeightedGraph<T> MST = new WeightedGraphUndirected<>();
        for (Comparable el : arr) {
            Edge<T> e = (Edge<T>) el;  
            if (!uf.find(e.u, e.v)) {
                MST.link(e.u, e.v, e.weight);
                uf.union(e.u, e.v);
            }
        }
        return MST;
    }
    
}
