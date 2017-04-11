package heap;

public class MinStack {
    private int[] stack;
    private int[] minStack;
    private int N;
    private int minN;

    public MinStack() {
        stack = new int[2];
        minStack = new int[2];
        N = 0;
        minN = 0;
    }
    
    public int top() {
        if (N == 0) {
            return -1;
        }
        return stack[N - 1];
    }
    
    public void pop() {
        if (N > 0) {
            if (top() == getMin()) {
                minN--;
            }
            N--;
        }
    }
    
    public int getMin() {
        if (N == 0) {
            return -1;
        }
        return minStack[minN - 1];
    }
    
    public void push(int x) {
        if (N == stack.length) {
            int[] newStack = new int[2*stack.length];
            System.arraycopy(stack, 0, newStack, 0, N);
            stack = newStack;
        }
        if (N == 0 || x < getMin()) {
            if (minN == minStack.length) {
                int[] newStack = new int[2*minStack.length];
                System.arraycopy(minStack, 0, newStack, 0, minN);
                minStack = newStack;
            }
            minStack[minN++] = x;
        }
        stack[N++] = x;
    }
    
}
