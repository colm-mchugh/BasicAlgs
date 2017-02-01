package dp;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;


public class knapsackTests {
    
    @Test
    public void knapsackGreedyAllItemsSameWeight() {
        // items are listed in pairs: Vi, Wi
        int data[] = {23, 42, 34, 42, 56, 42, 67, 42, 78, 42, 89, 42, 57, 42, 23, 42, 59, 42, 66, 42 };
        compareGreedyVMemozd(data, 396, true);
    }
    
    @Test
    public void knapsackGreedyAllItemsSameValue() {
        // items are listed in pairs: Vi, Wi
        int data[] = {42, 23, 42, 34, 42, 56, 42, 67, 42, 78, 42, 89, 42, 57, 42, 23, 42, 59, 42, 66, 42, 49 };
        compareGreedyVMemozd(data, 500, true);
    }
    
    @Test
    public void knapsackGreedyAllItemsSameValueWeightRatio() {
        // items are listed in pairs: Vi, Wi
        int data[] = { 1, 5, 3, 15, 7, 35, 9, 45, 5, 25, 10, 50, 12, 60, 20, 100, 15, 75, 11, 55, 13, 65, 14, 70 };
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
            k.weight = Integer.parseInt(firstLine[0]);
            int numItems = Integer.parseInt(firstLine[1]);
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
