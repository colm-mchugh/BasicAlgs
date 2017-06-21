package dp;

import java.util.ArrayDeque;
import java.util.Queue;

public class KnapsackBnB extends Knapsack {

    private Node best;
    private int estimate;
    
    // Branch and Bound knapsack - apply branch and bound strategy
    public static class Node {
        int value;
        int room;
        int estimate;
        
        Node left;
        Node right;

        public Node(int value, int room, int estimate) {
            this.value = value;
            this.room = room;
            this.estimate = estimate;
            this.left = null;
            this.right = null;
        }    

        @Override
        public String toString() {
            return "(" + value + ", " + room + ", " + estimate + ")";
        }
        
        
    }
    
    private void printSearchTree(Node root) {
        Queue<Node> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node nxt = q.remove();
            if (nxt.left != null) {
                q.add(nxt.left);
            }
            if (nxt.right != null) {
                q.add(nxt.right);
            }
            System.out.println(nxt);
        }
    }
    
    public KnapsackBnB(int knapSackCapacity, int[] data, boolean fractional) {
        super(knapSackCapacity, data);
        if (fractional) {
            estimate = KnapsackGrdy.fractionalEstimate(items, knapSackCapacity);
        } else {
            estimate = 0;
            for (Item item : items) {
                estimate += item.value;
            }
        }
    }

    @Override
    public int knapsack() {
        Node root = new Node(0, knapSackCapacity, estimate);
        best = root;
        depthSearch(root, 0);
        printSearchTree(root);
        return best.value;
    }
    
    private void depthSearch(Node n, int level) {
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
        depthSearch(n.left, level + 1);
        n.right = new Node(n.value, n.room, n.estimate - item.value);
        depthSearch(n.right, level + 1);
    }
    
    private void bestSearch(Node root) {
        Queue<Node> unexpanded = new ArrayDeque<>();
        unexpanded.add(root);
        while (!unexpanded.isEmpty()) {
            Node nxt = unexpanded.remove();
            
        }
    }
}
