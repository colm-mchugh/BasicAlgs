package clustering;

import heap.MinHeap;
import java.util.List;

public class KCluster {

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

    private final UnionFind<Integer> uf;
    private final MinHeap<Edge<Integer>> minHeap = new MinHeap<>();
    private Edge<Integer> min;

    public KCluster(UnionFind<Integer> uf, int numNodes, List<Integer> edgeData) {
        this.uf = uf;
        for (Integer i = 1; i <= numNodes; i++) {
            uf.addCluster(i);
        }
        for (int i = 0; i < edgeData.size(); i += 3) {
            minHeap.Insert(new Edge<>(edgeData.get(i), edgeData.get(i+1), edgeData.get(i+2)));
        }
    }

    // Set the invariant that the current spacing is the minimum 
    // distance between points in separate clusters
    private void adjustCurrentSpacing() {
        min = minHeap.Peek();
        while (uf.find(min.p1, min.p2)) {
            minHeap.Delete();
            min = minHeap.Peek();
        }
        for (min = minHeap.Peek(); uf.find(min.p1, min.p2); min = minHeap.Peek()) {
            minHeap.Delete();
        }
    }

    public int doKCluster(int K) {
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
