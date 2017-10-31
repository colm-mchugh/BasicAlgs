package dp;

import java.io.PrintStream;

public class KnapsackMemozd extends Knapsack {

    private int[][] memo;

    public KnapsackMemozd(int knapSackWeight, int[] valueWeightPairs) {
        super(knapSackWeight, valueWeightPairs);
    }

    protected void printItems(PrintStream pw) {
        for (Knapsack.Item item : items) {
            pw.println(item);
        }
    }

    @Override
    public int knapsack() {
        memo = new int[knapSackCapacity + 1][items.size() + 1];
        for (int w = 0; w <= knapSackCapacity; w++) {
            memo[w][0] = 0;
        }
        for (int i = 1; i <= items.size(); i++) {
            Item itemi = items.get(i - 1);
            for (int w = 0; w <= knapSackCapacity; w++) {
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
        int rv = memo[knapSackCapacity][0];
        for (int i = 1; i <= items.size(); i++) {
            if (rv < memo[knapSackCapacity][i]) {
                rv = memo[knapSackCapacity][i];
            }
        }
        // Determine the items that go in the knapsack;
        // For each item i, if its memoized value is the same as the value of
        // item i - 1, it is not in the knapsack. Otherwise include it and 
        // prune the weight.
        int remainingCapacity = knapSackCapacity;
        for (int n = items.size(); n > 0; n--) {
            int Vn = memo[remainingCapacity][n];
            Item item = items.get(n - 1);
            if (memo[remainingCapacity][n-1] != Vn) {
                item.isLive = true;
                remainingCapacity = remainingCapacity - item.weight;
            } else {
                item.isLive = false;
            }
        }
        return rv;
    }

}
