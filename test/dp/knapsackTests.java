package dp;



import org.junit.Test;


public class knapsackTests {
    
    @Test
    public void knapsackGreedyAllItemsSameWeight() {
        // items are listed in pairs: Vi, Wi
        int data[] = {23, 42, 34, 42, 56, 42, 67, 42, 78, 42, 89, 42, 57, 42, 23, 42, 59, 42, 66, 42 };
        int knapsackCap = 396;
        
        knapsackGreedy g = new knapsackGreedy();
        knapsackDP k = new knapsackDP();
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
        
        knapsackGreedy g = new knapsackGreedy();
        knapsackDP k = new knapsackDP();
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
        
        knapsackGreedy g = new knapsackGreedy();
        knapsackDP k = new knapsackDP();
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
        this.testDPKnapsack("resources/knapsack1.txt", 2493893);
        this.testRecKnapsack("resources/knapsack1.txt", 2493893);
    }
    
    @Test 
    public void bigKnapsackTest() {
        this.testRecKnapsack("resources/knapsack2.txt", 4243395);
        // knapsackDP chokes on resources/knapsack2.txt
    }
    
    private void testDPKnapsack(String knapsackData, int expected) {
        knapsackDP kDP = new knapsackDP();
        kDP.initItems(kDP.readData(knapsackData));
        int resDP = kDP.knapsack();
        assert resDP == expected;
    }
    
    private void testRecKnapsack(String knapsackData, int expected) {
        knapsackRec ks = new knapsackRec();
        ks.initItems(ks.readData(knapsackData));
        int resRec = ks.knapsack();
        assert resRec == expected;
    }
}
