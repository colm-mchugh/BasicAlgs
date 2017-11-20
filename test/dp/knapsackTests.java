package dp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

public class knapsackTests {

    @Test
    public void testDecisionVecSmall() {
        int data[] = {8, 4, 10, 5, 15, 8, 4, 3};
        int xpctd[] = {0, 0, 1, 1};
        int cap = 11;
        KnapsackMemozd k = new KnapsackMemozd(cap, data);
        int v = k.knapsack();
        int[] dVec = k.decisionVector();
        assert v == 19;
        for (int i = 0; i < xpctd.length; i++) {
            assert xpctd[i] == dVec[i];
        }

        KnapsackRcrsv kR = new KnapsackRcrsv(cap, data);
        assert kR.knapsack() == 19;
        dVec = kR.decisionVector();
        for (int i = 0; i < xpctd.length; i++) {
            assert xpctd[i] == dVec[i];
        }
    }

    @Test
    public void knapsackGreedyAllItemsSameWeight() {
        // items are listed in pairs: Vi, Wi
        int data[] = {23, 42, 34, 42, 56, 42, 67, 42, 78, 42, 89, 42, 57, 42, 23, 42, 59, 42, 66, 42};
        compareGreedyVMemozd(data, 396, true);
    }

    @Test
    public void knapsackGreedyAllItemsSameValue() {
        // items are listed in pairs: Vi, Wi
        int data[] = {42, 23, 42, 34, 42, 56, 42, 67, 42, 78, 42, 89, 42, 57, 42, 23, 42, 59, 42, 66, 42, 49};
        compareGreedyVMemozd(data, 500, true);
    }

    @Test
    public void knapsackGreedyAllItemsSameValueWeightRatio() {
        // items are listed in pairs: Vi, Wi
        int data[] = {1, 5, 3, 15, 7, 35, 9, 45, 5, 25, 10, 50, 12, 60, 20, 100, 15, 75, 11, 55, 13, 65, 14, 70};
        compareGreedyVMemozd(data, 225, false);
    }

    private void compareGreedyVMemozd(int data[], int cap, boolean eq) {
        KnapsackGrdy g = new KnapsackGrdy(cap, data);
        KnapsackMemozd k = new KnapsackMemozd(cap, data);

        int greedyV = g.knapsack();
        int kV = k.knapsack();

        if (eq) {
            assert greedyV == kV;
        } else {
            assert greedyV < kV;
        }
    }

    @Test
    public void mediumKnapsackTest() {
        this.testRecKnapsack("resources/knapsack1.txt", 2493893);
        this.testDPKnapsack("resources/knapsack1.txt", 2493893);
    }

    @Test
    public void bigKnapsackTest() {
        this.testRecKnapsack("resources/knapsack2.txt", 4243395);
        //this.testDPKnapsack("resources/knapsack2.txt", 4243395);
        //knapsackDP chokes on resources/knapsack2.txt
    }

    private void testDPKnapsack(String knapsackData, int expected) {
        KnapsackData k = this.readData(knapsackData);
        KnapsackMemozd kDP = new KnapsackMemozd(k.weight, k.data);
        long now = System.currentTimeMillis();
        int resDP = kDP.knapsack();
        System.out.println("DP knapsack computed in " + (System.currentTimeMillis() - now));
        assert resDP == expected;
    }

    private void testRecKnapsack(String knapsackData, int expected) {
        KnapsackData k = this.readData(knapsackData);
        KnapsackRcrsv ks = new KnapsackRcrsv(k.weight, k.data);
        long now = System.currentTimeMillis();
        int resRec = ks.knapsack();
        System.out.println("Rec knapsack computed in " + (System.currentTimeMillis() - now));
        assert resRec == expected;
    }

    @Test
    public void compareDecisionVecs() {
        String[] ksSpecs = {"ks_100_0", "ks_40_0", "ks_45_0", "ks_200_1"};

        for (String spec : ksSpecs) {
            testRecVMemo("resources/knapsack/" + spec);
        }
    }

    @Test
    public void testDepthSearch() {
        int[] data = {45, 5, 48, 8, 35, 3};
        int cap = 10;
        Knapsack dfSrchr = new KnapsackBnB(cap, data, KnapsackBnB.Strategy.DEPTH_FIRST, false);
        int estimate = dfSrchr.knapsack();
        assert estimate == 80;
    }

    @Test
    public void debugDecisionVectors() {
        String[] ksSpecs = {"ks_45_0",};
        KnapsackBnB.Strategy[] strategies = {
            KnapsackBnB.Strategy.DEPTH_FIRST, 
        };
        for (KnapsackBnB.Strategy strategy : strategies) {
            System.out.println((strategy == KnapsackBnB.Strategy.DEPTH_FIRST ? "DepthSearch" : "BreadthSearch"));
            for (String spec : ksSpecs) {
                KnapsackData k = this.readData("resources/knapsack/" + spec);
                System.out.print(spec + ": ");
                Knapsack ks = new KnapsackBnB(k.weight, k.data, strategy, false);
                long now = System.currentTimeMillis();
                int ksW = ks.knapsack();
                System.out.println(" value:" + ksW + ", time:" + (System.currentTimeMillis() - now));
                int[] dvKs = ks.decisionVector();
                System.out.print("Decision vector: [");
                for (int d : dvKs) {
                    System.out.print(d);
                    System.out.print(", ");
                }
                System.out.println("]");
                System.out.println("Sum liveitems =" + ks.sumLiveItems());
            }
        }
        for (String spec : ksSpecs) {
            KnapsackData k = this.readData("resources/knapsack/" + spec);
            System.out.print(spec + ": ");
            Knapsack ks = new KnapsackMemozd(k.weight, k.data);
            long now = System.currentTimeMillis();
            int ksW = ks.knapsack();
            System.out.println(" value:" + ksW + ", time:" + (System.currentTimeMillis() - now));
            int[] dvKs = ks.decisionVector();
            System.out.print("Decision vector: [");
            for (int d : dvKs) {
                System.out.print(d);
                System.out.print(", ");
            }
            System.out.println("]");
            System.out.println("Sum liveitems =" + ks.sumLiveItems());
        }
    }

    @Test
    public void timeBBSerach() {
        String[] ksSpecs = { "ks_40_0", "ks_45_0", "ks_100_0", 
            "ks_100_1", "ks_100_2", "ks_200_0", "ks_200_1", 
            "ks_300_0", "ks_400_0", "ks_1000_0", "ks_10000_0"
        };
        KnapsackBnB.Strategy[] strategies = {
            KnapsackBnB.Strategy.DEPTH_FIRST,
            KnapsackBnB.Strategy.BREADTH_FIRST,
            KnapsackBnB.Strategy.LDS
        };
        for (KnapsackBnB.Strategy strategy : strategies) {
            System.out.println(strategy);
            for (String spec : ksSpecs) {
                KnapsackData k = this.readData("resources/knapsack/" + spec);
                System.out.print(spec + ": ");
                KnapsackBnB ks = new KnapsackBnB(k.weight, k.data, strategy, true);
                long now = System.currentTimeMillis();
                int ksW = ks.knapsack();
                System.out.println(" value:" + ksW + ", time:" + (System.currentTimeMillis() - now) + ", Sum_liveitems=" + ks.sumLiveItems() + ", numNodes=" + ks.numNodes);
            }
        }
    }

    @Test
    public void timeRecursive() {
        String[] ksSpecs = {"ks_40_0", "ks_45_0", "ks_100_0",
            "ks_100_1", "ks_100_2", "ks_200_0", "ks_200_1",  
        };
        System.out.println("timeRecursive");
        for (String spec : ksSpecs) {
            KnapsackData k = this.readData("resources/knapsack/" + spec);
            Knapsack ks = new KnapsackMemozd(k.weight, k.data);
            long now = System.currentTimeMillis();
            int ksW = ks.knapsack();
            System.out.println(spec + " value:" + ksW + ", time:" + (System.currentTimeMillis() - now));
        }
    }

    private void testRecVMemo(String knapsackSpec) {
        KnapsackData k = this.readData(knapsackSpec);

        Knapsack ks = new KnapsackRcrsv(k.weight, k.data);
        Knapsack km = new KnapsackMemozd(k.weight, k.data);
        long now = System.currentTimeMillis();
        int ksW = ks.knapsack();
        System.out.println(knapsackSpec + " Recursive " + (System.currentTimeMillis() - now));
        now = System.currentTimeMillis();
        int kmW = km.knapsack();
        System.out.println(knapsackSpec + " Memoized " + (System.currentTimeMillis() - now));
        int[] dvKs = ks.decisionVector();
        int[] dvKm = km.decisionVector();

        assert ksW == kmW;
        for (int i = 0; i < dvKm.length; i++) {
            assert dvKm[i] == dvKs[i];
        }
    }

    public static class KnapsackData {

        int weight;
        int[] data;
    }

    private KnapsackData readData(String file) {
        KnapsackData k = new KnapsackData();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] firstLine = line.trim().split("(\\s)+");
            int numItems = Integer.parseInt(firstLine[0]);
            k.weight = Integer.parseInt(firstLine[1]);
            k.data = new int[numItems * 2];
            for (int i = 0; (line = br.readLine()) != null; i += 2) {
                String[] itemData = line.trim().split("(\\s)+");
                k.data[i] = Integer.parseInt(itemData[0]);
                k.data[i + 1] = Integer.parseInt(itemData[1]);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println(e.getMessage());
        }
        return k;
    }

}
