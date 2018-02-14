package dp;

/**
 * Compute the optimal cost of a binary search tree.
 * 
 * If a set of search keys have frequencies, then organizing them as a
 * perfectly balanced search tree may be sub-optimal in terms of the size of 
 * tree traversals. It may be more efficient to have most frequently searched
 * keys near the root of a tree, with the consequence of an unbalanced tree.
 * 
 * Goal: a valid binary search trees that minimizes the weighted average search time,
 * which is: SUM pi X time to find i
 * where pi is the frequency of item i and time to find i is effectively the 
 * depth of item i in the tree.
 * 
 * Optimal substructure: an optimal search tree with r as root has an optimal
 * left subtree for the items 1 .. r-1 and an optimal right subtree for the 
 * items r+1 .. N 
 * 
 * The items in a subproblem are either in a prefix or a suffix. An algorithm 
 * needs to compute the OST for these subsets of S : { 1,2,3….,N } all contiguous 
 * intervals S: { i, i+1, …, j-1, j } for every i, j in 1..N because of multiple 
 * levels of recursion.
 * 
 * Recurrence: for all i, j in 1..N with i less than or equal to j, 
 *  Oij = MIN r ( SUM Pk + Oir-1 + Or+1j ) for k in i..j, r in i..j
 * 
 * Algorithm: (Solve smallest subsets first (fewest items j - i + 1))
 * A[i,j] = OST value for items { i, .., j }
 * for s in 0 .. (N - 1)  (S represents (j - i))
 *   for i in 1..N
 *      A[i,i+s] = MIN r ( SUM Pk + A[i, r-1] + A[r + 1, i + s] ) for k in i .. i + s, r in i .. i + s
 * 
 * Running time: O(NxN) subproblems with O(j - 1) time to compute each subproblem gives O(N^3) overall
 * 
 * TODO: implement a DP algorithm. This implementation uses recursion.
 */
public class OptmlSrchTree {

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
    public static float optimalSearchTree(float freq[], int n) {
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
        System.out.println(optimalSearchTree(freq, keys.length));
    }

}
