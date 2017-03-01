package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class BinaryTree {

    int val;
    BinaryTree left;
    BinaryTree right;

    BinaryTree(int x) {
        val = x;
    }

    @Override
    public String toString() {
        return "" + val;
    }

    public int isSymmetric() {
        ArrayDeque<BinaryTree> queue = new ArrayDeque<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            ArrayList<Integer> nextLevel = new ArrayList<>();
            ArrayDeque<BinaryTree> childQueue = new ArrayDeque<>();
            while (!queue.isEmpty()) {
                BinaryTree tn = queue.remove();
                if (tn.left != null) {
                    nextLevel.add(tn.left.val);
                    childQueue.add(tn.left);
                } else {
                    nextLevel.add(null);
                }
                if (tn.right != null) {
                    nextLevel.add(tn.right.val);
                    childQueue.add(tn.right);
                } else {
                    nextLevel.add(null);
                }

            }
            int N = nextLevel.size();
            for (int i = 0; i < N / 2; i++) {
                if (!Objects.equals(nextLevel.get(i), nextLevel.get((N - 1) - i))) {
                    return 0;
                }
            }
            queue = childQueue;
        }
        return 1;
    }

    public ArrayList<ArrayList<Integer>> zigzagLevelOrder() {
        ArrayList<ArrayList<Integer>> order = new ArrayList<>();
        ArrayDeque<BinaryTree> queue = new ArrayDeque<>();
        Stack<BinaryTree> stack = new Stack<>();
        queue.add(this);
        while (!queue.isEmpty() || !stack.isEmpty()) {
            ArrayList<Integer> nextLevel = new ArrayList<>();

            if (!queue.isEmpty()) {
                while (!queue.isEmpty()) {
                    BinaryTree tn = queue.remove();
                    nextLevel.add(tn.val);
                    if (tn.left != null) {
                        stack.push(tn.left);
                    }
                    if (tn.right != null) {
                        stack.push(tn.right);
                    }
                }
            } else {
                if (!stack.isEmpty()) {
                    while (!stack.isEmpty()) {
                        BinaryTree tn = stack.pop();
                        nextLevel.add(tn.val);
                        if (tn.right != null) {
                            queue.addFirst(tn.right);
                        }
                        if (tn.left != null) {
                            queue.addFirst(tn.left);
                        }
                    }
                }
            }
            order.add(nextLevel);
        }
        return order;
    }

}
