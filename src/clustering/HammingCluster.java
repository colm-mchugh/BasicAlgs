package clustering;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;

public class HammingCluster {

    private final UnionFind<BitSet> clusters;
    private final HashMap<BitSet, Integer> vertexMap;

    public HammingCluster(UnionFind<BitSet> clusters) {
        this.clusters = clusters;
        this.vertexMap = new HashMap<>(); // provide size?
    }

    private void init(BitSet[] vertices) {
        for (BitSet vertex : vertices) {
            if (!vertexMap.containsKey(vertex)) {
                clusters.addCluster(vertex);
                vertexMap.put(vertex, 1);
            } else {
                Integer freq = vertexMap.get(vertex);
                vertexMap.put(vertex, ++freq);
            }
        }
    }

    private void permuteAndCombine(BitSet permutation, final int index, final BitSet vertex) {
        permutation.flip(index);
        if (vertexMap.containsKey(permutation) && !clusters.find(permutation, vertex)) {
            clusters.union(permutation, vertex);
        }
        permutation.flip(index); // BitSet is mutable => restore argument to its initial value
    }

    private void doOnePermutations(final BitSet vertex) {
        BitSet vertexCopy = vertex.get(0, vertex.length());
        for (int i = 0; i < vertexCopy.length(); i++) {
            // permute one bit of vertex copy and check if its in a cluster:
            permuteAndCombine(vertexCopy, i, vertex);
        }
    }

    private void doTwoPermutaions(final BitSet vertex) {
        BitSet vertexCopy = vertex.get(0, vertex.length());
        for (int i = 0; i < vertexCopy.length(); i++) {
            vertexCopy.flip(i);
            for (int j = 0; j < vertexCopy.length(); j++) {
                if (j == i) {
                    continue;
                }
                permuteAndCombine(vertexCopy, j, vertex);
            }
            vertexCopy.flip(i);
        }
    }

    public int doit(BitSet[] vertices) {
        init(vertices);
        Iterator<BitSet> vertexItrtr = clusters.iterator();
        while (vertexItrtr.hasNext()) {
            BitSet vertex = vertexItrtr.next();
            doOnePermutations(vertex);
            doTwoPermutaions(vertex);
        }
        return this.clusters.numClusters();
    }

}
