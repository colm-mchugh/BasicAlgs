package dp;

import java.util.Arrays;

/**
 * Dynamic Programming Heuristic
 *
 * Step 1: Vi = floor(Vi) / m  for-all Vi
 * Step 2: DP Algorithm
 * Subproblem: For i in 0..N and X in 0..NxVmax
 *    S[i, x] = min total size needed to achieve value >= x while using only first i items
 * Recurrence: (i >= 1)
 *    S[i, x] = MIN (S[i-1,x], Wi + S[i-1, x - Vi] *)   (* 0 if Vi > x)
 * Algorithm:
 * A[0, x] = 0 if X = 0, +Inf otherwise
 * For i = 1..N
 *  For x in 0..NVmax
 *	A[i, x] = MIN(S[i-1,x], Wi + S[i-1, x - Vi] *)   (* 0 if Vi > x) 
 * Return the largest x such that A[n,x] ≤ W ← O(nvmax) 
 * Running time: O(n2vmax) 
 * 
 */
public class KnapsackHeuristic extends Knapsack {

    private int[][] memo;
    private final int Vmax;
    private final float M;
    private final int N;
    
    public KnapsackHeuristic(int knapSackWeight, int[] valueWeightPairs, float epsilon) {
        super(knapSackWeight, valueWeightPairs);
        int max = valueWeightPairs[0];
        for (int i = 2; i < valueWeightPairs.length; i += 2) {
            if (valueWeightPairs[i] > max) {
                max = valueWeightPairs[i];
            }
        }
        N = items.size();
        M = epsilon * max / N;
        max = 0;
        for (Item item : items) {
            item.value = (int) Math.floor(item.value / M);
            if (item.value > max) {
                max = item.value;
            }
        }
        Vmax = max;
    }
    
    @Override
    public int knapsack() {
        memo = new int[N + 1][N * Vmax];
        for (int[] row : memo) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        memo[0][0] = 0;
        for (int i = 1; i <= N; i++) {
            for (int x = 0; x < N * Vmax; x++) {
                int prev = memo[i-1][x];
                Item item = items.get(i - 1);
                int next = item.weight + (item.value > x ? 0 :  memo[i - 1][x - item.value]);
                memo[i][x] = Integer.min(prev, next);
            }
        }
        int rv = Integer.MIN_VALUE;
        for (int x = 0; x < memo[N].length; x++) {
            if (memo[N][x] < knapSackCapacity && memo[N][x] > rv) {
                rv = memo[N][x];
            }
        }
        return rv;
    }
    
}
