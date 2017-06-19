package dp;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KnapsackGrdy extends Knapsack {

    public KnapsackGrdy(int capacity, int[] data) {
        super(capacity, data);
    }

    public static int fractionalEstimate(List<Item> items, int capacity) {
        // Sort the items in descending order of value / weight ratio
        Collections.sort(items, new Comparator<Knapsack.Item>() {
            @Override
            public int compare(Knapsack.Item o1, Knapsack.Item o2) {
                float myRatio = o1.value / (float) o1.weight;
                float itsRatio = o2.value / (float) o2.weight;
                // items are sorted in order of decreasing ratios.
                if (myRatio > itsRatio) {
                    // if my ratio is greater than my counterpart's, I 
                    // am before my counterpart in the sorted order.              
                    return -1;
                } else if (myRatio < itsRatio) {
                    return 1;
                }
                return 0;
            }
        });
        int v = 0, w = 0; 
        for (Item item : items) {
            if (w + item.weight < capacity) {
                w += item.weight;
                v += item.value;
                item.decision = true;
            } else {
                // Item cannot fit in the knapsack. Include the fraction
                // of the item value that fills up the capacity
                return v + ((capacity - w) * item.value)/item.weight ; 
            }
        }
        // items did not exhaust capacity
        return v;
    }
    
    @Override
    public int knapsack() {
        return fractionalEstimate(this.items, this.knapSackCapacity);
    }
}
