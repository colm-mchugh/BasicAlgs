package dp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NSat {

    /**
     * TriState is used to indicate if a formula is satisfiable, not
     * satisfiable, or not possible to know. It enables evaluation of 
     * partially assigned formulas during a solve.
     */
    public enum TriState {
        False, True, DontKnow;
    }

    /**
     * pvar is a prioritized variable; when performing a backtracking solve, the
     * next variable to try is determined by picking a pvar off a priority
     * queue.
     */
    public static class pvar implements Comparable<pvar> {
        int var;
        float score;

        public pvar(int var, float score) {
            this.var = var;
            this.score = score;
        }

        @Override
        public int compareTo(pvar o) {
            if (this.score < o.score) {
                return -1;
            }
            if (this.score > o.score) {
                return 1;
            }
            return 0;
        }

    }

    public static class clause {

        public final List<Integer> vars;

        public clause(List<Integer> vars) {
            this.vars = vars;
        }

        /**
         * A clause - a disjunction of boolean variables - evaluates to false if
         * all of its variables are false. It evaluates to true if one or more
         * of its variables are true and all the others are false. It evaluates
         * to DontKnow if at least one of its variables is unassigned. This
         * means its variables still need an assignment to know if it can be
         * true or false.
         *
         * @param ctxt
         * @return
         */
        public TriState eval(Map<Integer, Boolean> ctxt) {
            TriState val = TriState.False;
            for (Integer var : vars) {
                if (!(ctxt.containsKey(Math.abs(var)))) {
                    return TriState.DontKnow;
                }
                if (this.lookup(var, ctxt)) {
                    val = TriState.True;
                }
            }
            return val;
        }

        public boolean lookup(Integer k, Map<Integer, Boolean> ctxt) {
            Boolean v = ctxt.get(Math.abs(k));
            if (v == null) {
                return true;
            }
            return (k < 0 ? !v : v);
        }

        @Override
        public String toString() {
            return "(" + this.vars + ')';
        }
    }

    /**
     * The actual boolean formula being solved
     */
    protected List<clause> formula;

    /**
     * The current variable assignment
     */
    protected Map<Integer, Boolean> variables;

    /**
     * Initialize NSat with a formula and empty (no variables assigned)
     * assignment.
     *
     * @param formula
     */
    public NSat(List<clause> formula) {
        this.formula = formula;
        this.variables = new HashMap<>();
    }

    /**
     * A conjunction of clauses evaluates to false if any is false,
     * true if all are true, DontKnow otherwise.
     *
     * @return
     */
    public TriState eval() {
        Boolean allTrue = !this.formula.isEmpty();
        TriState val = TriState.True;
        for (clause c : formula) {
            val = c.eval(variables);
            if (val == TriState.False) {
                break;
            }
            allTrue = allTrue && val.equals(TriState.True);
        }
        if (val == TriState.False) {
            return TriState.False;
        }
        if (allTrue) {
            return TriState.True;
        }
        return TriState.DontKnow;
    }

    /**
     * Determine if the formula is satisfiable, using backtracking. If the
     * current evaluation is neither true nor false, take the next unassigned
     * variable and re-evaluate the formula with the variable alternately
     * assigned false and true.
     *
     * @param varQueue
     * @param varIndex
     * @return
     */
    public boolean SatSolve(List<pvar> varQueue, int varIndex) {
        // Evaluate the formula with the curent variable assignment.
        TriState satVal = this.eval();
        if (satVal.equals(TriState.False)) {
            return false;
        }
        if (satVal.equals(TriState.True)) {
            return true;
        }
        // The satVal is DontKnow; try setting the next var to false,
        // and recursively solving the formula.
        pvar var = varQueue.get(varIndex);
        variables.put(var.var, Boolean.FALSE);
        if (SatSolve(varQueue, varIndex + 1)) {
            return true;
        }
        // Setting var to false didn't work; try true.
        variables.put(var.var, Boolean.TRUE);
        return SatSolve(varQueue, varIndex + 1);
    }
}
