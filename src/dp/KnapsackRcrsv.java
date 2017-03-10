package dp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KnapsackRcrsv extends Knapsack {

    private final Map<Integer, Map<Integer, Integer>> recMemo;

    public KnapsackRcrsv(int knapSackWeight, int[] data) {
        super(knapSackWeight);
        items = new ArrayList<>(data.length / 2);
        for (int i = 0; i < data.length; i += 2) {
            items.add(new Item(data[i], data[i + 1]));
        }
        recMemo = new HashMap<>();
        for (int i = -1; i < items.size(); i++) {
            recMemo.put(i, new HashMap<>());
        }
    }
    
    private int calcKnapsack(int i, int W) {
        if (i < 0) {
            return 0;
        }
        Item item = this.items.get(i);
        Integer prev = recMemo.get(i).get(W);
        if (prev != null) {
            return prev;
        }
        Integer prev1 = recMemo.get(i - 1).get(W);
        if (prev1 == null) {
            prev1 = calcKnapsack(i - 1, W);
            recMemo.get(i - 1).put(W, prev1);
        }
        if (item.weight > W) {
            return prev1;
        }
        Integer prev2 = recMemo.get(i - 1).get(W - item.weight);
        if (prev2 == null) {
            prev2 = calcKnapsack(i - 1, W - item.weight);
            recMemo.get(i - 1).put(W - item.weight, prev2);
        }
        Integer thisVal = Integer.max(prev1, item.value + prev2);
        recMemo.get(i).put(W, thisVal);
        return thisVal;
    }

    @Override
    public int knapsack() {
        return calcKnapsack(items.size() - 1, knapSackWeight);
    }
    
}
