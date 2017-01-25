package dp;



import org.junit.Test;


public class knapsackTests {
    
    @Test
    public void knapsackGreedyAllItemsSameWeight() {
        // items are listed in pairs: Vi, Wi
        int data[] = {23, 42, 34, 42, 56, 42, 67, 42, 78, 42, 89, 42, 57, 42, 23, 42, 59, 42, 66, 42 };
        int knapsackCap = 396;
        
        KnapsackGrdy g = new KnapsackGrdy();
        KnapsackMemozd k = new KnapsackMemozd();
        g.setKnapSackWeight(knapsackCap);
        k.setKnapSackWeight(knapsackCap);
    
        g.initItems(data);
        k.initItems(data);
        
        int greedyV = g.knapsack();
        int kV = k.knapsack();
        
        assert greedyV == kV;
    }
    
    @Test
    public void knapsackGreedyAllItemsSameValue() {
        // items are listed in pairs: Vi, Wi
        int data[] = {42, 23, 42, 34, 42, 56, 42, 67, 42, 78, 42, 89, 42, 57, 42, 23, 42, 59, 42, 66, 42, 49 };
        int knapsackCap = 500;
        
        KnapsackGrdy g = new KnapsackGrdy();
        KnapsackMemozd k = new KnapsackMemozd();
        g.setKnapSackWeight(knapsackCap);
        k.setKnapSackWeight(knapsackCap);
    
        g.initItems(data);
        k.initItems(data);
        
        int greedyV = g.knapsack();
        int kV = k.knapsack();
        
        assert greedyV == kV;
    }
    
    @Test
    public void knapsackGreedyAllItemsSameValueWeightRatio() {
        // items are listed in pairs: Vi, Wi
        int data[] = { 1, 5, 3, 15, 7, 35, 9, 45, 5, 25, 10, 50, 12, 60, 20, 100, 15, 75, 11, 55, 13, 65, 14, 70 };
        int knapsackCap = 225;
        
        KnapsackGrdy g = new KnapsackGrdy();
        KnapsackMemozd k = new KnapsackMemozd();
        g.setKnapSackWeight(knapsackCap);
        k.setKnapSackWeight(knapsackCap);
    
        g.initItems(data);
        k.initItems(data);
        
        int greedyV = g.knapsack();
        int kV = k.knapsack();
        
        assert greedyV < kV;
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
        KnapsackMemozd kDP = new KnapsackMemozd();
        long now = System.currentTimeMillis();
        kDP.initItems(kDP.readData(knapsackData));
        int resDP = kDP.knapsack();
        System.out.println("DP knapsack computed in " + (System.currentTimeMillis() - now));
        assert resDP == expected;
    }
    
    private void testRecKnapsack(String knapsackData, int expected) {
        KnapsackRcrsv ks = new KnapsackRcrsv();
        long now = System.currentTimeMillis();
        ks.initItems(ks.readData(knapsackData));
        int resRec = ks.knapsack();
        System.out.println("Rec knapsack computed in " + (System.currentTimeMillis() - now));
        assert resRec == expected;
    }
}
