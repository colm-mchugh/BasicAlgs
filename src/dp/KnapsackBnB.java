package dp;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import utils.Stack;

public class KnapsackBnB extends Knapsack {

    private goal bestGoal;
    private final Set<Item> OUTs;
    private final boolean searchStrategy;
    
    public final static boolean DEPTH_FIRST = true;
    public final static boolean BREADTH_FIRST = false;
    
    private static int numNodes = 0;
    // Branch and Bound knapsack - apply branch and bound strategy

    public KnapsackBnB(int knapSackCapacity, int[] valueWeightPairs, boolean searchStrategy) {
        super(knapSackCapacity, valueWeightPairs);
        KnapsackGrdy.sortItems(items);
        this.OUTs = new HashSet<>(items.size());
        this.searchStrategy = searchStrategy;
    }

    @Override
    public int knapsack() {
        // Get initial estimate
        //int estimate = getEstimate();
        //Node root = new Node(0, knapSackCapacity, estimate);
        //best = root;
        //doDepthSearch(root, 0);
        if (this.searchStrategy == DEPTH_FIRST) {
            this.depthSearchItrtv();
        }
        if (this.searchStrategy == BREADTH_FIRST) {
            this.bestSearch();
        }
        System.out.print("nodes:" + numNodes);
        return bestGoal.value;
    }

    public static class goal implements Comparable<goal> {
        final int value;
        final int room;
        final int estimate;
        final int level;
        final boolean live;
        int levelIndex;
        
        public goal(int value, int room, int estimate, int level, boolean live) {
            this.value = value;
            this.room = room;
            this.estimate = estimate;
            this.level = level;
            this.live = live;
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
    
    private void depthSearchItrtv() {
        goal root = new goal(0, knapSackCapacity, getEstimate(), 0, false);
        bestGoal = root;
        int N = items.size();
        BitSet bestSet;
        BitSet live = new BitSet(N + 1);
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
            if (n.value > bestGoal.value) {
                bestGoal = n;
                bestSet = live.get(0, live.size() - 1);
            }
            if (n.level >= N) {
                continue;
            }
            live.set(n.level, n.live);
            Item item = items.get(n.level);
            S.Push(new goal(n.value + item.value, n.room - item.weight, n.estimate, n.level + 1, false));
            OUTs.remove(item);
            S.Push(new goal(n.value, n.room, getEstimate(live, n.level + 1), n.level + 1, true));
            OUTs.add(item);
        }
    }
    
    private int getEstimate() {
        int v = 0, w = 0; 
        boolean underCapacity = true;
        for (int i = 0; i < items.size() && underCapacity; i++) {
            Item item = items.get(i);
            if (OUTs.contains(item)) {
                continue;
            }
            if (w + item.weight < this.knapSackCapacity) {
                w += item.weight;
                v += item.value;
            } else {
                // Item cannot fit in the knapsack. Include the fraction
                // of the item value that fills up the capacity
                v += ((this.knapSackCapacity - w) * item.value)/item.weight ; 
                underCapacity = false;
            }
        }
        return v;
    }
    
    private int getEstimate(BitSet live, int level) {
        int v = 0, w = 0; 
        boolean underCapacity = true;
        for (int i = 0; i < items.size() && underCapacity; i++) {
            Item item = items.get(i);
            if (OUTs.contains(item)) {
                continue;
            }
            if (i + 1 <= level && !live.get(i+1)) {
                continue;
            }
            if (w + item.weight < this.knapSackCapacity) {
                w += item.weight;
                v += item.value;
            } else {
                // Item cannot fit in the knapsack. Include the fraction
                // of the item value that fills up the capacity
                v += ((this.knapSackCapacity - w) * item.value)/item.weight ; 
                underCapacity = false;
            }
        }
        return v;
    }
    
    private void bestSearch() {
        Deque<goal> unexpanded = new ArrayDeque<>();
        goal root = new goal(0, knapSackCapacity, getEstimate(), 0, false);
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
            if (n.value > bestGoal.value) {
                bestGoal = n;
            }
            if (n.level >= N) {
                continue;
            }
            int levelIndex = 0;
            if (!unexpanded.isEmpty()) {
                goal prevGoal = unexpanded.peekLast();
                int prevLevelIndex = prevGoal.levelIndex;
                int prevLevel = prevGoal.level;
                if (prevLevelIndex < (1 << prevLevel) - 1) {
                    levelIndex = prevLevelIndex + 1;
                } 
            }
            Item item = items.get(n.level);
            unexpanded.add(new goal(n.value + item.value, n.room - item.weight, n.estimate, n.level + 1, true));
            unexpanded.peekLast().levelIndex = levelIndex;
            OUTs.remove(item);
            unexpanded.add(new goal(n.value, n.room, getEstimate(), n.level + 1, false));
            unexpanded.peekLast().levelIndex = levelIndex;
            OUTs.add(item);
        }
        // Starting at bestGoal, compute the set of items that make up the knapsack.
        // If levelIndex % 2 == 0 => include the item, else exclude it.
        // The parent of a goal is: (levelIndex + 1 << level) / 2
        for (int level = bestGoal.level, levelIndex = bestGoal.levelIndex; level > 0; level--) {
            if (levelIndex % 2 == 0) {
                bestSet.set(level);
                this.items.get(level - 1).isLive = true;
            }
            levelIndex = (levelIndex + 1 << level) / 2 - (1 << (level - 1)) ; 
        }
    }
    
    // TODO: Limited Discrepancy Search
    // For N items, generate all possible set permutatatios with k zeroes, (N - k) ones
    // for k in 0..N
}
