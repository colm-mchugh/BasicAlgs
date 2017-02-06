package greedy;

import clustering.QuickFind;
import clustering.UnionFind;
import heap.MinHeap;

/**
 * KClusterer (or "Kruskal Clusterer") is an adaptation of Kruskal's algorithm
 * for determining the Minimum Spanning Tree (MST) of an undirected graph.
 * 
 */
public class KClusterer<T> {
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

    private final UnionFind<T> uf = new QuickFind<>();
    private final MinHeap<Edge<T>> minHeap = new MinHeap<>();
    private Edge<T> min;
    
    public void addCluster(T c) {
        uf.addCluster(c);
    }
    
    public void addEdge(T u, T v, int d) {
        minHeap.Insert(new Edge<>(u, v, d));
    }
    
    // Set the invariant that the current spacing is the minimum 
    // distance between points in separate clusters
    private void adjustCurrentSpacing() {
        for (min = minHeap.Peek(); uf.find(min.p1, min.p2); min = minHeap.Peek()) {
            minHeap.Delete();
        }
    }
    
    public int getKDistance(int K) {
        while (uf.numClusters() > K) {
            adjustCurrentSpacing();
            uf.union(min.p1, min.p2);
        }
        adjustCurrentSpacing();
        if (uf.numClusters() != K) {
            throw new RuntimeException("doKCluster Postcondition error: K=" + K + ", NumberClusters=" + uf.numClusters());
        }
        return min.distance;
    }
    
}
