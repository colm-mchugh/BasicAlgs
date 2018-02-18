package dp;

/**
 * Sequence Alignment of two given strings.
 * 
 * Uses simple penalty scoring per http://avatar.se/lectures/molbioinfo2001/dynprog/dynamic.html.
 * 
 * TODO: Extend with scoring that produces fewest gaps, as in
 * https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm
 * https://web.stanford.edu/class/cs262/presentations/lecture2.pdf
 * 
 */
public class SequenceAlignment {

    private final static int P_GAP = 0;
    private final static char GAP = '-';

    private static int p_letter(char l1, char l2) {
        return (l1 == l2 ? 1 : 0);
    }

    // Alignment holds all the state for producing an alignment between two 
    // strings X and Y. 
    static class Alignment {
        int needleman_wunsch_score;
        int edit_distance;
        StringBuilder alignX;
        StringBuilder alignY;
        int i, j;

        private Alignment(String x, String y, int needleman_wunsch_score) {
            this.alignX = new StringBuilder();
            this.alignY = new StringBuilder();
            this.i = x.length();
            this.j = y.length();
            this.edit_distance = 0;
            this.needleman_wunsch_score = needleman_wunsch_score;
        }
        
        public void match(char x, char y) {
            assert x == y; // only makes sense when x == y
            alignX.insert(0, x);
            alignY.insert(0, y);
            this.i--;
            this.j--;
        }
        
        public void xWithGap(char x) {
            alignX.insert(0, x);
            alignY.insert(0, GAP);
            this.i--;
            this.edit_distance++;
        }
        
        public void yWithGap(char y) {
            alignX.insert(0, GAP);
            alignY.insert(0, y);
            this.j--;
            this.edit_distance++;
        }
    }

    public static Alignment align(String X, String Y) {
        int M = X.length();
        int N = Y.length();
        int[][] p_memo = new int[M + 1][N + 1];
        // First part: build and populate the penalty memo
        
        // The penalty for matching each prefix of X with the empty string is
        // the length of the prefix times the penalty for a gap, and likewise
        // for matching each prefix of Y with the empty string.
        for (int i = 0; i <= M; i++) {
            p_memo[i][0] = P_GAP * i;
        }
        for (int j = 0; j <= N; j++) {
            p_memo[0][j] = P_GAP * j;
        }
        // Determine the penalty for each slot by taking the maximum of;
        //  1) the cost of an X[i], Y[j] match with the previous penalty
        //  2) the cost of matching X[i] with a gap
        //  3) the cost of matching Y[j] with a gap
        for (int i = 1; i <= M; i++) {
            for (int j = 1; j <= N; j++) {
                int xy = p_memo[i - 1][j - 1] + p_letter(X.charAt(i - 1), Y.charAt(j - 1));
                int xGap = p_memo[i - 1][j] + P_GAP;
                int yGap = p_memo[i][j - 1] + P_GAP;
                p_memo[i][j] = Integer.max(Integer.max(xy, yGap), xGap);
            }
        }
        
        // Second part: build the alignment of X with Y.
        //
        // Working backwards from entry memo[M][N], determine which option 
        // 1), 2) or 3) was used to compute that entry.
        Alignment align = new Alignment(X, Y, p_memo[M][N]);
        while (align.i > 0 && align.j > 0) {
            int i = align.i;
            int j = align.j;
            if (p_memo[i][j] == p_memo[i - 1][j] + P_GAP) {
                align.xWithGap(X.charAt(i - 1));
            } else if (p_memo[i][j] == p_memo[i - 1][j - 1] + p_letter(X.charAt(i - 1), Y.charAt(j - 1))) {
                align.match(X.charAt(i - 1), Y.charAt(j - 1));
            } else {
                align.yWithGap(Y.charAt(j - 1));
            }
        }
        // If not all characters in X were visited, align the remaining chars with a gap.
        while (align.i > 0) {
            align.xWithGap(X.charAt(align.i - 1));
        }
        // likewise for Y
        while (align.j > 0) {
            align.yWithGap(Y.charAt(align.j - 1));
        }
        return align;
    }
}
