package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class HammingClusterTest {

    private final int expected = 6118;
    private final boolean doPathCompression = true;
    private final boolean withFollowers = true;
    private final BitSet[] vertices = this.readVertices("resources/clustering_big.txt");

    @Test
    public void testDoitQF() {
        System.out.println("QuickFind");
        this.verify(new QuickFind<>(!withFollowers), expected);
    }

    @Test
    public void testDoitQFwLS() {
        System.out.println("QuickFind - w follower sets");
        this.verify(new QuickFind<>(withFollowers), expected);
    }

    @Test
    public void testDoitLU() {
        System.out.println("LazyUnion");
        this.verify(new LazyUnion<>(!doPathCompression), expected);
    }

    @Test
    public void testDoitLUwPC() {
        System.out.println("LazyUnion - with path compression");
        this.verify(new LazyUnion<>(doPathCompression), expected);
    }

    /**
     * Test HammingCluster with specific union find instance
     */
    private void verify(UnionFind<BitSet> uf, int expected) {
        long now = System.currentTimeMillis();
        HammingCluster instance = new HammingCluster(uf);
        int result = instance.doit(vertices);
        assertEquals(expected, result);
        long timeTaken = System.currentTimeMillis() - now;
        System.out.println("Time taken: " + timeTaken);
    }

    private BitSet[] readVertices(String file) {
        FileReader fr;
        BitSet[] vertices = null;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] firstLine = line.trim().split("(\\s)+");
            int numNodes = Integer.parseInt(firstLine[0]);
            int numBits = Integer.parseInt(firstLine[1]);
            vertices = new BitSet[numNodes];
            for (int j = 0; (line = br.readLine()) != null; j++) {
                String[] bits = line.trim().split("(\\s)+");
                BitSet vertex = new BitSet(numBits);
                //assert bits.length == vertex.length();
                for (int i = 0; i < bits.length; i++) {
                    if (bits[i].equals("1")) {
                        vertex.set(i);
                    }
                }
                vertices[j] = vertex;
            }
        } catch (IOException | NumberFormatException e) {
        }
        return vertices;
    }

}
