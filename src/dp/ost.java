package dp;

public class ost {

    private static float optCost(float freq[], int i, int j) {
        // Base cases
        if (j < i) {
            return 0;
        }
        if (j == i) {
            return freq[i];
        }

        // Get sum of freq[i], freq[i+1], ... freq[j]
        float fsum = sum(freq, i, j);

        // Initialize minimum value
        float min = Float.MAX_VALUE;

        // One by one consider all elements as root and recursively find cost
        // of the BST, compare the cost with min and update min if needed
        for (int r = i; r <= j; ++r) {
            float cost = optCost(freq, i, r - 1) + optCost(freq, r + 1, j);
            if (cost < min) {
                min = cost;
            }
        }
        return min + fsum;
    }

// The main function that calculates minimum cost of a Binary Search Tree.
// It mainly uses optCost() to find the optimal cost.
    public static float optimalSearchTree(int keys[], float freq[], int n) {
        // Here array keys[] is assumed to be sorted in increasing order.
        // If keys[] is not sorted, then add code to sort keys, and rearrange
        // freq[] accordingly.
        return optCost(freq, 0, n - 1);
    }

    private static float sum(float freq[], int i, int j) {
        float s = 0;
        for (int k = i; k <= j; k++) {
            s += freq[k];
        }
        return s;
    }

    public static void main(String[] args) {
        float[] freq = {0.05f, 0.4f, 0.08f, 0.04f, 0.1f, 0.1f, 0.23f};
        int[] keys = {1, 2, 3, 4, 5, 6, 7};
        System.out.println(optimalSearchTree(keys, freq, keys.length));
    }

}
