package string;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Transform a string X into string Y.
 * 
 * Transformation is driven by a cost model, where each operation (copy, replace,
 * insert, delete) has a cost. The transformation should be the cheapest possible.
 * 
 */
public class Transform {

    public enum Op {
        NOP,
        COPY,
        REPL,
        INS,
        DEL;

        int cost() {
            switch (this) {
                case NOP:
                    return 0;
                case COPY:
                    return -1;
                case REPL:
                    return 1;
                case INS:
                case DEL:
                    return 2;
            }
            throw new RuntimeException("Unsupported cost for " + this);
        }
    }

    static public class Step {

        public char c;
        public Op op;

        public Step(char c, Op op) {
            this.c = c;
            this.op = op;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 73 * hash + this.c;
            hash = 73 * hash + Objects.hashCode(this.op);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Step other = (Step) obj;
            if (this.c != other.c) {
                return false;
            }
            return this.op == other.op;
        }

        @Override
        public String toString() {
            return "[" + op + " " + c + "]";
        }
      
        
    };

    private final Op[][] ops;
    private final String X;
    private final String Y;

    /**
     * Build the operation table ops for transforming X to Y.
     * 
     * ops[i][j] is the operation to apply on characters X(i), Y(j)  
     * 
     * @param X
     * @param Y 
     */
    public Transform(String X, String Y) {
        final int m = X.length();
        final int n = Y.length();
        final int[][] cost;
        
        this.X = X;
        this.Y = Y;

        cost = new int[m+1][n+1];
        ops = new Op[m+1][n+1];

        // Transforming empty string to itself is a NOP
        cost[0][0] = Op.NOP.cost();
        ops[0][0] = Op.NOP;

        // Transforming X(i) to empty string is a series of deletes: 
        for (int i = 1; i <= m; i++) {
            cost[i][0] = Op.DEL.cost() * i;
            ops[i][0] = Op.DEL;
        }

        // Transforming empty string to Y(i) is a series of inserts:
        for (int i = 1; i <= n; i++) {
            cost[0][i] = Op.INS.cost() * i;
            ops[0][i] = Op.INS;
        }

        // Set ops[i][j] for all i,j in X.length(), Y.length()
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char Xi = X.charAt(i - 1);
                char Yj = Y.charAt(j - 1);

                // Set ops[i][j] to the minimum of:
                // 1) COPY or REPL, depending on if X(i) == Y(j)
                // 2) DEL, if deleting X(i) is cheaper than 1
                // 3) INS, if inserting Y(j) is cheaper than 1 and 2
                // The cost table is used to determine the op, and is updated also.
                
                if (Xi == Yj) {
                    cost[i][j] = cost[i - 1][j - 1] + Op.COPY.cost();
                    ops[i][j] = Op.COPY;
                } else {
                    cost[i][j] = cost[i - 1][j - 1] + Op.REPL.cost();
                    ops[i][j] = Op.REPL;
                }

                if (cost[i - 1][j] + Op.DEL.cost() < cost[i][j]) {
                    cost[i][j] = cost[i - 1][j] + Op.DEL.cost();
                    ops[i][j] = Op.DEL;
                }

                if (cost[i][j - 1] + Op.INS.cost() < cost[i][j]) {
                    cost[i][j] = cost[i][j - 1] + Op.INS.cost();
                    ops[i][j] = Op.INS;
                }
            }
        }
    }

    /**
     * Create the list of steps required to transform X(i) to Y(j).
     * 
     * The ops table, previously created in the constructor, is used to drive 
     * the assembly of steps. It proceeds using a simple recurrence:
     * 
     * Append Step(op[i][j], C) to Assembly(i', j'), where values of i' and j' 
     * and C depend on the op.
     * 
     * Runs in O(m + n). 
     * 
     * @param i
     * @param j
     * @return 
     */
    public List<Step> Assemble(int i, int j) {
        List<Step> assembly = null;

        switch (ops[i][j]) {
            case NOP: // Base case: empty assembly
                assert i == 0 && j == 0;
                assembly = new ArrayList<>();
                break;
            case COPY:
            case REPL:
                // Either append a COPY of X(i) or REPL with Y(j)
                assembly = Assemble(i - 1, j - 1);
                if (ops[i][j] == Op.COPY) {
                    assembly.add(new Step(X.charAt(i - 1), Op.COPY));
                } else {
                    assembly.add(new Step(Y.charAt(j - 1), Op.REPL));
                }
                break;
            case DEL: // the character X(i)
                assembly = Assemble(i - 1, j);
                assembly.add(new Step(X.charAt(i - 1), Op.DEL));
                break;
            case INS: // the character Y(j)
                assembly = Assemble(i, j - 1);
                assembly.add(new Step(Y.charAt(j - 1), Op.INS));
                break;
        }
        return assembly;
    }

    /**
     * Count the number of non COPY steps in the assembly of X and Y.
     * 
     * Gives a simple edit distance, where the value of each operation is 1.
     * 
     * @return 
     */
    public int distance() {
        List<Step> a = this.Assemble(X.length(), Y.length());
        int score = 0;
        for (Step step : a) {
            if (step.op != Op.COPY) {
                score += 1;
            }
        }
        return score;
    }
    
    static public List<Step> Of(String X, String Y) {
        Transform t = new Transform(X, Y);
        return t.Assemble(X.length(), Y.length());
    }
    
}
