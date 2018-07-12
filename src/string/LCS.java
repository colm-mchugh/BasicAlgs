package string;

/**
 * Longest Common Subsequence.
 * 
 * Given two strings X and Y return the longest common subsequence.
 * The characters in a subsequence do not need to be contiguous 
 * (unlike substring)
 * 
 * Example:
 * LCS ("CATCGA", "GTACCGTCA") == "CTCA".
 *
 * The implementation uses a dynamic programming approach; a table
 * of the lengths of the LCS of all prefixes of X and Y is built, 
 * which is then used to drive building the LCS of X and Y, using 
 * the recurrence:
 *      if X(i) == Y(j):
 *	    LCS(X(0..i), Y(0..j)) == LCS(X(0..i-1), Y(0..j-1)) + X(i) 
 *      else:
 *	    use lengths table to determine LCS as the maximum of:
 *		LCS(X(0..i-1), Y(0..j)),  LCS(X(0..i), Y(0..j-1))
 */
public class LCS {
   
    /**
     * 
     * Build the lengths table L given strings X and Y.
     * 
     * @param X
     * @param Y
     * @return the lengths table L such that
     *         L[i][j] gives length of LCS (X.substring(0,i), Y.substring(0,j))
     */
    public static int[][] getLCSTable(String X, String Y) {
        int m = X.length();
        int n = Y.length();
        int[][] L = new int[m+1][n+1];
        
        // implicit init: L[i][0] = 0 && L[0][j] = 0 because LCS("", X) == ""
        
        // Examine every character pair X(i), Y(i). If equal, L[i][j] is 
        // LCS(X(i-1), Y(j-1)) + 1, otherwise its max LCS(X(i-1), Y(j)), LCS(X(i), Y(j-1))
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (X.charAt(i-1) == Y.charAt(j-1)) {
                    L[i][j] = L[i-1][j-1] + 1;
                 } else {
                    L[i][j] = Integer.max(L[i][j-1], L[i-1][j]);
                 }
            }
        }
        return L;
    }
    
    /**
     * Return the longest common subsequence of X.substring(0, i), Y.substring(0, j).
     * 
     * Note: in the code comments, X(i) is shorthand for X.substring(0, i)
     * 
     * @param X
     * @param Y
     * @param L
     * @param i
     * @param j
     * @return 
     */
    public static StringBuilder getLCS(String X, String Y, int[][] L, int i, int j) {
        // X(i) or Y(j) is empty string => LCS is ""
        if (L[i][j] == 0) {
            return new StringBuilder();
        }
        
        // X(i) == Y(j) => LCS is LCS of X(i-1) and Y(j-1) appended with X(i)
        if (X.charAt(i - 1) == Y.charAt(j - 1)) {
            return getLCS(X, Y, L, i - 1, j - 1).append(X.charAt(i - 1));
        }
        
        // Otherwise use the LCS lengths table to determine if LCS should be
        // LCS of X(i-1), Y(j) or LCS of X(i), Y(j-1)
        if (L[i][j-1] > L[i-1][j]) {
            return getLCS(X, Y, L, i, j - 1);
        } 
        
        return getLCS(X, Y, L, i - 1, j); 
    }
    
    public static String Of(String X, String Y) {
        return getLCS(
                X, Y,
                getLCSTable(X, Y),
                X.length(),
                Y.length()
        ).toString();
    }
}
