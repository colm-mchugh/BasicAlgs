package dp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import utils.Stack;

public class KnapsackBnB extends Knapsack {

    public enum Strategy {
        DEPTH_FIRST,
        BREADTH_FIRST,
        LDS
    }

    private goal bestGoal;
    private final Set<Item> OUTs;
    private final List<Item> sortedItems;
    private final Strategy searchStrategy;

    public static int numNodes = 0;
    // Branch and Bound knapsack - apply branch and bound strategy

    public KnapsackBnB(int knapSackCapacity, int[] valueWeightPairs, Strategy searchStrategy, boolean useSortedItems) {
        super(knapSackCapacity, valueWeightPairs);
        if (useSortedItems) {
            sortedItems = items;
        } else {
            sortedItems = new ArrayList<Item>(items);
        }
        KnapsackGrdy.sortItems(sortedItems);
        this.OUTs = new HashSet<>(items.size());
        this.searchStrategy = searchStrategy;
    }

    @Override
    public int knapsack() {
        switch (this.searchStrategy) {
            case BREADTH_FIRST:
                this.breadthSearch();
                break;
            case DEPTH_FIRST:
                this.depthSearch();
                break;
            case LDS:
                this.ldsSearch();
                break;
        }
        return bestGoal.value;
    }

    public static class goal implements Comparable<goal> {

        final int value;
        final int room;
        final int estimate;
        final int level;
        final boolean live;
        final int levelIndex;

        public goal(int value, int room, int estimate, int level, boolean live, int levelIndex) {
            this.value = value;
            this.room = room;
            this.estimate = estimate;
            this.level = level;
            this.live = live;
            this.levelIndex = levelIndex;
            numNodes++;
        }

        @Override
        public int compareTo(goal o) {
            if (this.value < o.value) {
                return -1;
            }
            if (this.value == o.value) {
                return 0;
            }
            return 1;
        }
    }

    private void depthSearch() {
        goal root = new goal(0, knapSackCapacity, getEstimate(), 0, false, 0);
        bestGoal = root;
        int N = items.size();
        BitSet live = new BitSet(N + 1);
        BitSet bestSet = live;
        Stack<goal> S = new Stack<>();
        S.Push(root);
        while (!S.isEmpty()) {
            goal n = S.Pop();
            if (n.room <= 0) {
                continue;
            }
            if (n.estimate < bestGoal.value) {
                continue;
            }
            if (n.level > N) {
                continue;
            }
            live.set(n.level, n.live);
            if (n.value > bestGoal.value) {
                bestGoal = n;
                bestSet = live.get(0, live.size() - 1);
            }
            Item item = items.get(n.level);
            OUTs.remove(item);
            S.Push(new goal(n.value, n.room, getEstimate(live, n.level + 1), n.level + 1, false, 0));
            OUTs.add(item);
            S.Push(new goal(n.value + item.value, n.room - item.weight, n.estimate, n.level + 1, true, 0));
        }
        for (int i = 1; i <= N; i++) {
            items.get(i - 1).isLive = bestSet.get(i);
        }
    }

    private int getEstimate() {
        int v = 0, w = 0;
        boolean underCapacity = true;
        for (int i = 0; i < sortedItems.size() && underCapacity; i++) {
            Item item = sortedItems.get(i);
            if (OUTs.contains(item)) {
                continue;
            }
            if (w + item.weight < this.knapSackCapacity) {
                w += item.weight;
                v += item.value;
            } else {
                // Item cannot fit in the knapsack. Include the fraction
                // of the item value that fills up the capacity
                v += ((this.knapSackCapacity - w) * item.value) / item.weight;
                underCapacity = false;
            }
        }
        return v;
    }

    private int getEstimate(BitSet live, int level) {
        int v = 0, w = 0;
        boolean underCapacity = true;
        for (int i = 0; i < sortedItems.size() && underCapacity; i++) {
            Item item = sortedItems.get(i);
            if (OUTs.contains(item)) {
                continue;
            }
            if (i + 1 <= level && !live.get(i + 1)) {
                continue;
            }
            if (w + item.weight < this.knapSackCapacity) {
                w += item.weight;
                v += item.value;
            } else {
                // Item cannot fit in the knapsack. Include the fraction
                // of the item value that fills up the capacity
                v += ((this.knapSackCapacity - w) * item.value) / item.weight;
                underCapacity = false;
            }
        }
        return v;
    }

    private void breadthSearch() {
        Deque<goal> unexpanded = new ArrayDeque<>();
        goal root = new goal(0, knapSackCapacity, getEstimate(), 0, false, 1);
        bestGoal = root;
        int N = items.size();
        BitSet bestSet = new BitSet(N + 1);

        unexpanded.add(root);
        while (!unexpanded.isEmpty()) {
            goal n = unexpanded.remove();
            if (n.room <= 0) {
                continue;
            }
            if (n.estimate < bestGoal.value) {
                continue;
            }
            if (n.level >= N) {
                continue;
            }
            if (n.value > bestGoal.value) {
                bestGoal = n;
            }
            int levelIndex = 1;
            if (!unexpanded.isEmpty()) {
                goal prevGoal = unexpanded.peekLast();
                int prevLevelIndex = prevGoal.levelIndex;
                int prevLevel = prevGoal.level;
                if (prevLevelIndex < (1 << prevLevel) - 1) {
                    levelIndex = prevLevelIndex + 1;
                }
            }
            Item item = items.get(n.level);
            unexpanded.add(new goal(n.value + item.value, n.room - item.weight, n.estimate, n.level + 1, true, levelIndex));
            OUTs.remove(item);
            unexpanded.add(new goal(n.value, n.room, getEstimate(), n.level + 1, false, levelIndex));
            OUTs.add(item);
        }
        // Starting at bestGoal, compute the set of items that make up the knapsack.
        // If levelIndex % 2 == 0 => include the item, else exclude it.
        // The parent of a goal is: (levelIndex + 1 << level) / 2
        for (int level = bestGoal.level, levelIndex = bestGoal.levelIndex; level > 0; level--) {
            System.out.println("level=" + level + ", levelindex=" + levelIndex);
            if (levelIndex % 2 == 0) {
                bestSet.set(level);
                this.items.get(level - 1).isLive = true;
            }
            levelIndex = (levelIndex + 1 << level) / 2 - (1 << (level - 1));
        }
    }

    // TODO: Limited Discrepancy Search
    // For N items, generate all possible set permutatatios with k zeroes, (N - k) ones
    // for k in 0..N
    public static class ldsGoal extends goal {

        int d;
        int t;

        public ldsGoal(int value, int room, int estimate, int level, boolean live, int levelIndex, int t, int d) {
            super(value, room, estimate, level, live, levelIndex);
            this.d = d;
            this.t = t;
        }
    }

    private void ldsSearch() {
        int N = items.size();
        BitSet live = new BitSet(N + 1);
        BitSet bestSet = live;
        int baseEstimate = getEstimate();
        Stack<ldsGoal> S = new Stack<>();
        for (int d = 0; d <= N; d++) {
            ldsGoal root = new ldsGoal(0, knapSackCapacity, baseEstimate, 0, false, 0, d, N - d);
            bestGoal = root;
            S.Push(root);
            while (!S.isEmpty()) {
                ldsGoal n = S.Pop();
                if (n.room <= 0) {
                    continue;
                }
                if (n.estimate < bestGoal.value) {
                    continue;
                }
                if (n.level > N) {
                    continue;
                }
                live.set(n.level, n.live);
                if (n.value > bestGoal.value) {
                    bestGoal = n;
                    bestSet = live.get(0, live.size() - 1);
                }
                Item item = items.get(n.level);
                if (n.d > 0) {
                    OUTs.remove(item);
                    S.Push(new ldsGoal(n.value, n.room, getEstimate(live, n.level + 1), n.level + 1, false, 0, n.d - 1, n.t));
                    OUTs.add(item);
                }
                if (n.t > 0) {
                    S.Push(new ldsGoal(n.value + item.value, n.room - item.weight, n.estimate, n.level + 1, true, 0, n.d, n.t - 1));
                }
            }
            for (int i = 1; i <= N; i++) {
                items.get(i - 1).isLive = bestSet.get(i);
            }
        }
    }

}
