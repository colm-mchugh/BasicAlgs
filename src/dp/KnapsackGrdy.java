package dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class KnapsackGrdy extends Knapsack {

    public KnapsackGrdy(int knapSackWeight, int[] data) {
        super(knapSackWeight);
        items = new ArrayList<>(data.length / 2);
        for (int i = 0; i < data.length / 2; i++) {
            items.add(new Knapsack.Item(data[2 * i], data[2 * i + 1]));
        }
        Collections.sort(items, new Comparator<Knapsack.Item>() {
            @Override
            public int compare(Knapsack.Item o1, Knapsack.Item o2) {
                float myRatio = o1.value / (float) o1.weight;
                float itsRatio = o2.value / (float) o2.weight;
                if (myRatio > itsRatio) {
                    return -11;
                } else if (myRatio < itsRatio) {
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override
    public int knapsack() {
        int v = 0; 
        for (int w = 0, i = 0;i < items.size() && w + items.get(i).weight < this.knapSackWeight; i++) {            
                w += items.get(i).weight;
                v += items.get(i).value;
        }
        return v;
    }
}
