package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class BinaryTree<T> {

    T val;
    BinaryTree<T> left;
    BinaryTree<T> right;

    BinaryTree(T x) {
        val = x;
    }

    @Override
    public String toString() {
        return "" + val;
    }

    public int isSymmetric() {
        ArrayDeque<BinaryTree<T>> queue = new ArrayDeque<>();
        ArrayDeque<BinaryTree<T>> childQueue = new ArrayDeque<>();
        ArrayList<T> nextLevel = new ArrayList<>();
        queue.add(this);
        while (!queue.isEmpty()) {           
            while (!queue.isEmpty()) {
                BinaryTree<T> tn = queue.remove();
                nextLevel.add(enQueue(childQueue, tn.left));
                nextLevel.add(enQueue(childQueue, tn.right));
            }
            int N = nextLevel.size();
            for (int i = 0; i < N / 2; i++) {
                if (!Objects.equals(nextLevel.get(i), nextLevel.get((N - 1) - i))) {
                    return 0;
                }
            }
            ArrayDeque<BinaryTree<T>> tmp = queue;
            queue = childQueue;
            childQueue = tmp;
            nextLevel.clear();
        }
        return 1;
    }

    private T enQueue(ArrayDeque<BinaryTree<T>> queue, BinaryTree<T> n) {
        T val = null;
        if (n != null) {
            queue.add(n);
            val = n.val;
        }
        return val;
    }
    
    public ArrayList<ArrayList<T>> zigzagLevelOrder() {
        ArrayList<ArrayList<T>> order = new ArrayList<>();
        ArrayDeque<BinaryTree<T>> queue = new ArrayDeque<>();
        Stack<BinaryTree> stack = new Stack<>();
        queue.add(this);
        while (!queue.isEmpty() || !stack.isEmpty()) {
            ArrayList<T> nextLevel = new ArrayList<>();
            if (!queue.isEmpty()) {
                while (!queue.isEmpty()) {
                    BinaryTree<T> tn = queue.remove();
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
                        BinaryTree<T> tn = stack.pop();
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
