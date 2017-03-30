package dp;

import java.util.List;
import java.util.Map;

public class NSat {
    
    public enum TriState {
        False, True, DontKnow;
    }
    
    public static class clause {

        public final List<Integer> vars;

        public clause(List<Integer> vars) {
            this.vars = vars;
        }

        /**
         * A clause evaluates to false if all of its variables are false.
         * It evaluates to true if one or more of its variables are true
         * and all the others are false.
         * It evaluates to DontKnow if at least one of its variables is unassigned.
         * This means its variables still need an assignment to know if it can
         * be true or false.
         * 
         * @param ctxt
         * @return 
         */
        public TriState eval(Map<Integer, Boolean> ctxt) {
            TriState val = TriState.False;
            for (Integer var : vars) {
                if (!(ctxt.containsKey(var))) {
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
    
    protected Map<Integer, Boolean> variables;
    protected long N;
    protected List<clause> equation;
    
    public TriState eval() {
        Boolean allTrue = true;
        TriState val = TriState.True;
        for (clause c : equation) {
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

    public boolean SatSolve(List<clause> formula, int var) {
        TriState satVal = eval();
        if (satVal.equals(TriState.False)) {
            return false;
        }
        if (satVal.equals(TriState.True)) {
            return true;
        }
        variables.put(var, Boolean.FALSE);
        if (variables.containsKey(-var)) {
            variables.put(-var, Boolean.TRUE);
        }
        if (SatSolve(formula, var + 1)) {
            return true;
        }
        variables.put(var, Boolean.TRUE);
        if (variables.containsKey(-var)) {
            variables.put(-var, Boolean.FALSE);
        }
        if (SatSolve(formula, var + 1)) {
            return true;
        }
        return false;
    }
}
