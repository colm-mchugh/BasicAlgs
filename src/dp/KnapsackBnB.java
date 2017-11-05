package dp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import utils.Stack;

public class KnapsackBnB extends Knapsack {

    private Node best;
    private goal bestGoal;
    private Set<Item> OUTs;
    
    private static int numNodes = 0;
    // Branch and Bound knapsack - apply branch and bound strategy
    public static class Node {

        final int value;
        final int room;
        final int estimate;
        Node left, right;

        public Node(int value, int room, int estimate) {
            this.value = value;
            this.room = room;
            this.estimate = estimate;
            this.left = null;
            this.right = null;
            numNodes++;
        }

        @Override
        public String toString() {
            return "(" + value + ", " + room + ", " + estimate + ")";
        }

    }

    public KnapsackBnB(int knapSackCapacity, int[] valueWeightPairs) {
        super(knapSackCapacity, valueWeightPairs);
        KnapsackGrdy.sortItems(items);
        this.OUTs = new HashSet<>(items.size());
    }

    @Override
    public int knapsack() {
        // Get initial estimate
        //int estimate = getEstimate();
        //Node root = new Node(0, knapSackCapacity, estimate);
        //best = root;
        //doDepthSearch(root, 0);
        this.depthSearchItrtv();
        System.out.print("nodes:" + numNodes);
        return bestGoal.value;
    }

    public static class goal implements Comparable<goal> {
        final int value;
        final int room;
        final int estimate;
        final int level;
        final boolean live;
        
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
            S.Push(new goal(n.value + item.value, n.room - item.weight, n.estimate, n.level + 1, true));
            OUTs.remove(item);
            S.Push(new goal(n.value, n.room, getEstimate(), n.level + 1, false));
            OUTs.add(item);
        }
    }
    
    private void doDepthSearch(Node n, int level) {
        if (n.room <= 0) {
            return;
        }
        if (n.estimate < best.value) {
            return;
        }
        if (n.value > best.value) {
            best = n;
        }
        if (level >= items.size()) {
            return;
        }
        Item item = items.get(level);
        n.left = new Node(n.value + item.value, n.room - item.weight, n.estimate);
        doDepthSearch(n.left, level + 1);
        OUTs.remove(item);
        n.right = new Node(n.value, n.room, getEstimate());
        doDepthSearch(n.right, level + 1);
        OUTs.add(item);      
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
    
    private void bestSearch(Node root) {
        Queue<Node> unexpanded = new ArrayDeque<>();
        unexpanded.add(root);
        while (!unexpanded.isEmpty()) {
            Node nxt = unexpanded.remove();

        }
    }
}
