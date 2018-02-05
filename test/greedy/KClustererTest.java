package greedy;

import clustering.KCluster;
import clustering.LazyUnion;
import clustering.QuickFind;
import clustering.UnionFind;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class KClustererTest {

    private final int spacing = 4;
    private final int result = 106;
    private final boolean withPathCompression = true;
    private final boolean withFollowers = true;
    private int numNodes;
    private List<Integer> edgeData;

    public KClustererTest() {
        String file = "resources/clustering.txt";
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] firstLine = line.trim().split("(\\s)+");
            numNodes = Integer.parseInt(firstLine[0]);
            edgeData = new ArrayList<>(numNodes);
            while ((line = br.readLine()) != null) {
                String[] split = line.trim().split("(\\s)+");
                int u = Integer.parseInt(split[0]);
                int v = Integer.parseInt(split[1]);
                int d = Integer.parseInt(split[2]);
                edgeData.add(u);
                edgeData.add(v);
                edgeData.add(d);
            }
        } catch (IOException | NumberFormatException e) {
        }
    }

    @Test
    public void testKCErQuickFind() {
        verify("kClusterer-QuickFind", new QuickFind<>(!withFollowers), spacing, result);
    }

    @Test
    public void testKCErQuickFind2() {
        verify("kClusterer-QuickFind w/followers", new QuickFind<>(withFollowers), spacing, result);
    }

    @Test
    public void testKCErLazyUnion() {
        verify("kClusterer-LazyUnion w/path cmprss", new LazyUnion<>(withPathCompression), spacing, result);
    }

    @Test
    public void testKCErLazyUnion2() {
        verify("kClusterer-LazyUnion", new QuickFind<>(!withPathCompression), spacing, result);
    }

    @Test
    public void testKCQuickFind() {
        verifyKcluster("kCluster-QuickFind", new QuickFind<>(!withFollowers), spacing, result);
    }

    @Test
    public void testKCQuickFind2() {
        verifyKcluster("kCluster-QuickFind w/followers", new QuickFind<>(withFollowers), spacing, result);
    }

    @Test
    public void testKCLazyUnion() {
        verifyKcluster("kCluster-LazyUnion w/path cmprss", new LazyUnion<>(withPathCompression), spacing, result);
    }

    @Test
    public void testKCLazyUnion2() {
        verifyKcluster("kCluster-LazyUnion", new QuickFind<>(!withPathCompression), spacing, result);
    }

    private void verify(String test, UnionFind<Integer> uf, int spacing, int expected) {
        long now = System.currentTimeMillis();
        KClusterer<Integer> instance = new KClusterer(uf);
        this.init(instance);
        assert instance.getKDistance(spacing) == expected;
        long timeTaken = System.currentTimeMillis() - now;
        System.out.println(test + ": Time taken: " + timeTaken);
    }

    private void verifyKcluster(String test, UnionFind<Integer> uf, int spacing, int expected) {
        long now = System.currentTimeMillis();
        KCluster kc = new KCluster(uf, numNodes, edgeData);
        assert kc.doKCluster(spacing) == expected;
        long timeTaken = System.currentTimeMillis() - now;
        System.out.println(test + ": Time taken: " + timeTaken);
    }

    public void init(KClusterer<Integer> instance) {
        for (Integer i = 1; i <= numNodes; i++) {
            instance.addCluster(i);
        }
        for (int i = 0; i < edgeData.size(); i += 3) {
            instance.addEdge(edgeData.get(i), edgeData.get(i+1), edgeData.get(i+2));
        }
    }

}
