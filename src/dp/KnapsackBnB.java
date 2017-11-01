package dp;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import utils.Stack;

public class KnapsackBnB extends Knapsack {

    private Node best;
    private int estimate;
    private List<Item> sortedItems;

    // Branch and Bound knapsack - apply branch and bound strategy
    public static class Node {

        int value;
        int room;
        int estimate;

        public Node(int value, int room, int estimate) {
            this.value = value;
            this.room = room;
            this.estimate = estimate;
        }

        @Override
        public String toString() {
            return "(" + value + ", " + room + ", " + estimate + ")";
        }

    }

    public KnapsackBnB(int knapSackCapacity, int[] valueWeightPairs, boolean fractional) {
        super(knapSackCapacity, valueWeightPairs);
        if (fractional) {
            estimate = KnapsackGrdy.sortedEstimate(items, knapSackCapacity, true);
        } else {
            estimate = 0;
            for (Item item : items) {
                estimate += item.value;
            }
        }
    }

    @Override
    public int knapsack() {
        // Get initial estimate

        Node root = new Node(0, knapSackCapacity, estimate);
        best = root;
        doDepthSearch(root);
        return best.value;
    }

    private void doDepthSearch(Node root) {
        Stack<Node> searchSpace = new Stack<>();
        int level = 0;

        searchSpace.Push(root);

        while (1 == 1) {
            Item next = items.get(level);
            Node top = searchSpace.Top();
            Node nextNode = null;
            if (top.room - next.weight < 0) {
                // Cannot go further along this branch => exclude item from search
                nextNode = new Node(top.value, top.room, estimateExcluding(next));
                level++;
            } else {
                if (top.value + next.value < best.value) {
                   // No point in going down this branch
                } else {
                    nextNode = new Node(top.value + next.value, top.room - next.weight, top.estimate);
                    best = nextNode; 
                    level++;
                }
            }
            if (nextNode != null) {
                searchSpace.Push(nextNode);
            } else {
                // what? 
            }

        }
    }

    private int estimateExcluding(Item excludedItem) {
        // TODO: get the greedy estimate not considering the excluded item.
        // Remove it from INs set and get the estimate.
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    private void bestSearch(Node root) {
        Queue<Node> unexpanded = new ArrayDeque<>();
        unexpanded.add(root);
        while (!unexpanded.isEmpty()) {
            Node nxt = unexpanded.remove();

        }
    }
}
