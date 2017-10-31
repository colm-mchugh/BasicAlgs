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
        for (Item item : items) {
            item.isLive = true;
        }
    }
    
    public static int sortedEstimate(List<Item> items, int capacity, boolean doFractionalEstimate) {
        int v = 0, w = 0; 
        int i;
        boolean underCapacity = true;
        for (i = 0; i < items.size() && underCapacity; i++) {
            Item item = items.get(i);
            if (!item.isLive) {
                continue;
            }
            if (w + item.weight < capacity) {
                w += item.weight;
                v += item.value;
                item.isLive = true;
            } else {
                // Item cannot fit in the knapsack. Include the fraction
                // of the item value that fills up the capacity
                if (doFractionalEstimate) {
                    v += ((capacity - w) * item.value)/item.weight ; 
                }
                underCapacity = false;
            }
        }
        for (; i < items.size(); i++) {
            items.get(i).isLive = false;
        }
        return v;
    }
    
    @Override
    public int knapsack() {
        sortItems(this.items);
        return sortedEstimate(this.items, this.knapSackCapacity, false);
    }
}
