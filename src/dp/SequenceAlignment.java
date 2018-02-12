package dp;

import java.util.Arrays;

/**
 * Sequence Alignment of two given strings.
 * 
 * Uses simple penalty scoring per http://avatar.se/lectures/molbioinfo2001/dynprog/dynamic.html.
 * 
 * Extend with scoring that produces fewest gaps, as in
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

    static class Alignment {

        int needleman_wunsch_score;
        int edit_distance;
        StringBuilder alignX;
        StringBuilder alignY;
        int i, j;

        private Alignment(String x, String y) {
            this.alignX = new StringBuilder();
            this.alignY = new StringBuilder();
            this.i = x.length();
            this.j = y.length();
            this.edit_distance = 0;
        }
        
        public void case1(char x, char y) {
            alignX.insert(0, x);
            alignY.insert(0, y);
            this.i--;
            this.j--;
        }
        
        public void case2(char x) {
            alignX.insert(0, x);
            alignY.insert(0, GAP);
            this.i--;
            this.edit_distance++;
        }
        
        public void case3(char y) {
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
        for (int i = 0; i <= M; i++) {
            p_memo[i][0] = P_GAP * i;
        }
        for (int j = 0; j <= N; j++) {
            p_memo[0][j] = P_GAP * j;
        }
        for (int i = 1; i <= M; i++) {
            for (int j = 1; j <= N; j++) {
                int xy = p_memo[i - 1][j - 1] + p_letter(X.charAt(i - 1), Y.charAt(j - 1));
                int xGap = p_memo[i - 1][j] + P_GAP;
                int yGap = p_memo[i][j - 1] + P_GAP;
                p_memo[i][j] = Integer.max(Integer.max(xy, yGap), xGap);
            }
        }
        Alignment align = new Alignment(X, Y);
        while (align.i > 0 && align.j > 0) {
            int i = align.i;
            int j = align.j;
            if (p_memo[i][j] == p_memo[i - 1][j] + P_GAP) {
                align.case2(X.charAt(i - 1));
            } else if (p_memo[i][j] == p_memo[i - 1][j - 1] + p_letter(X.charAt(i - 1), Y.charAt(j - 1))) {
                align.case1(X.charAt(i - 1), Y.charAt(j - 1));
            } else {
                align.case3(Y.charAt(j - 1));
            }
        }
        while (align.i > 0) {
            align.case2(X.charAt(align.i - 1));
        }
        while (align.j > 0) {
            align.case3(Y.charAt(align.j - 1));
        }
        System.out.println(Arrays.deepToString(p_memo));
        align.needleman_wunsch_score = p_memo[M][N];
        return align;
    }
}
