package dp;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KnapsackGrdy extends Knapsack {

    public KnapsackGrdy(int capacity, int[] valueWeightPairs) {
        super(capacity, valueWeightPairs);
    }

    public static void sortItems(List<Item> items) {
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
    }
    
    public static int sortedEstimate(List<Item> items, int capacity) {
        int v = 0, w = 0; 
        int i;
        for (i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (w + item.weight < capacity) {
                w += item.weight;
                v += item.value;
            } else {
                // Item, and subsequent items, cannot fit in the knapsack. Stop.
                break;
            }
        }
        return v;
    }
    
    @Override
    public int knapsack() {
        sortItems(this.items);
        return sortedEstimate(this.items, this.knapSackCapacity);
    }
}
