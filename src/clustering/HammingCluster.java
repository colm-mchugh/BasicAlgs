package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
   
    private void initDataStructs(String file) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] firstLine = line.trim().split("(\\s)+");
            int numNodes = Integer.parseInt(firstLine[0]);
            int numBits = Integer.parseInt(firstLine[1]);
            while ((line = br.readLine()) != null) {
                String[] bits = line.trim().split("(\\s)+");
                BitSet vertex = new BitSet(numBits);
                //assert bits.length == vertex.length();
                for (int i = 0; i < bits.length; i++) {
                    if (bits[i].equals("1")) {
                        vertex.set(i);
                    }
                }
                if (!vertexMap.containsKey(vertex)) {
                    clusters.addCluster(vertex);
                    vertexMap.put(vertex, 1);
                } else {
                    Integer freq = vertexMap.get(vertex);
                    vertexMap.put(vertex, ++freq);
                }
            }
        } catch (IOException | NumberFormatException e) {
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

    public int doit(String file) {
        initDataStructs(file);
        Iterator<BitSet> vertexItrtr = clusters.iterator();
        while (vertexItrtr.hasNext()) {
            BitSet vertex = vertexItrtr.next();
            doOnePermutations(vertex);
            doTwoPermutaions(vertex);
        }
        return this.clusters.numClusters();
    }

}
