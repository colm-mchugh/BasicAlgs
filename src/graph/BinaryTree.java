package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
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
        inOrderIterator<T> lr = new inOrderIterator<>(this, inOrderIterator.LR);
        inOrderIterator<T> rl = new inOrderIterator<>(this, inOrderIterator.RL);
        while (lr.hasNext() && rl.hasNext() && (lr.next() == rl.next())) {
        }
        if (lr.hasNext() || rl.hasNext()) {
            return 0;
        }
        return 1;
    }

    public static class inOrderIterator<T> implements Iterator<T> {
        final Stack<BinaryTree<T>> stack;
        final boolean direction;
        
        final static boolean LR = true;
        final static boolean RL = false;

        public inOrderIterator(BinaryTree<T> root, boolean direction) {
            this.stack = new Stack<>();
            this.direction = direction;
            advance(root);
        }
        
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            BinaryTree<T> n = stack.pop();
            T val = n.val;
            BinaryTree<T> nextNext = (this.direction == LR? n.right : n.left);
            if (nextNext != null) {
                n = nextNext;
                advance(n);
            }
            return val;
        }

        private void advance(BinaryTree<T> n) {
            while (n != null) {
                stack.push(n);
                n = (this.direction == LR? n.left : n.right);
            }
        }
        
        
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
