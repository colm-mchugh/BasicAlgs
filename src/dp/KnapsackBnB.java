package dp;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class KnapsackBnB extends Knapsack {

    private Node best;
    private List<Item> sortedItems;
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
        int estimate = getEstimate();
        Node root = new Node(0, knapSackCapacity, estimate);
        best = root;
        doDepthSearch(root, 0);
        System.out.println("Nodes allocated in search tree:" + numNodes);
        return best.value;
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
