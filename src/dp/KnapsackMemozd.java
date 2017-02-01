package dp;

import java.io.PrintStream;
import java.util.ArrayList;

public class KnapsackMemozd extends Knapsack {

    private int[][] memo;

    public KnapsackMemozd(int knapSackWeight, int[] data) {
        super(knapSackWeight);
        items = new ArrayList<>(data.length / 2);
        for (int i = 0; i < data.length; i += 2) {
            items.add(new Item(data[i], data[i + 1]));
        }
    }

    protected void printItems(PrintStream pw) {
        for (Knapsack.Item item : items) {
            pw.println(item);
        }
    }

    @Override
    public int knapsack() {
        memo = new int[knapSackWeight + 1][items.size() + 1];
        for (int w = 0; w <= knapSackWeight; w++) {
            memo[w][0] = 0;
        }
        for (int i = 1; i <= items.size(); i++) {
            Item itemi = items.get(i - 1);
            for (int w = 0; w <= knapSackWeight; w++) {
                int prevValue = memo[w][i - 1];
                if (itemi.weight > w) {
                    // item i exceeds capacity w => retain previous value
                    memo[w][i] = prevValue;
                } else {
                    // Can include item i => set value to item i's value 
                    // plus the value of the bag with capacity w - (i's weight) for i-1 items
                    int withI = itemi.value + memo[w - itemi.weight][i - 1];
                    memo[w][i] = Integer.max(prevValue, withI);
                }
            }
        }
        int rv = memo[knapSackWeight][0];
        for (int i = 1; i <= items.size(); i++) {
            if (rv < memo[knapSackWeight][i]) {
                rv = memo[knapSackWeight][i];
            }
        }
        return rv;
    }

}
