package clustering;

import heap.MinHeap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

    private final UnionFind<Integer> uf = new LazyUnion<>();
    private final MinHeap<Edge<Integer>> minHeap = new MinHeap<>();
    private Edge<Integer> min;
    
    public void init(String file) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] firstLine = line.trim().split("(\\s)+");
            int numNodes = Integer.parseInt(firstLine[0]);
            for (Integer i = 1; i <= numNodes; i++) {
                uf.addCluster(i);
            }
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                int u = Integer.parseInt(split[0]);
                int v = Integer.parseInt(split[1]);
                int d = Integer.parseInt(split[2]);
                minHeap.Insert(new Edge<>(u, v, d));
            }
        } catch (IOException | NumberFormatException e) {
        }
    }

    public KCluster() {
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

    private static void calcKCluster(String file, int k) {
        KCluster kc = new KCluster();
        kc.init(file);
        System.out.println(k + " Cluster spacing = " + kc.doKCluster(k));      
    }
    
    public static void main(String[] args) {
        String file = "resources/clustering.txt";
        calcKCluster(file, 4);
    }

}
