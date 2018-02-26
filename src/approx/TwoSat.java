package approx;

import approx.TwoSatLS;
import java.util.List;
import java.util.Map;
import utils.RandGen;

/**
 * 2-SAT evaluator API. 
 */
abstract class TwoSat {

    public static class clause {

        public Integer lVar;
        public Integer rVar;

        public clause(Integer lVar, Integer rVar) {
            this.lVar = lVar;
            this.rVar = rVar;
        }

        public boolean eval(Map<Integer, Boolean> ctxt) {
            Boolean lval = this.lookup(lVar, ctxt);
            Boolean rval = this.lookup(rVar, ctxt);
            return lval || rval;
        }

        public boolean lookup(Integer k, Map<Integer, Boolean> ctxt) {
            Boolean v = ctxt.get(Math.abs(k));
            if (v == null) {
                return true;
            }
            return (k < 0 ? !v : v);
        }

        public void flip(Map<Integer, Boolean> ctxt) {
            // uniformly flip one of lVar or rVar
            Integer toflip = Math.abs((RandGen.uniformBool() ? lVar : rVar));
            ctxt.put(toflip, !ctxt.get(toflip));
        }

        @Override
        public String toString() {
            return "(" + lVar + " || " + rVar + ')';
        }
    }

    protected Map<Integer, Boolean> variables;
    protected long N;
    protected List<TwoSatLS.clause> equation;
    
    public boolean eval() {
        for (clause c : equation) {
            if (!c.eval(variables)) {
                return false;
            }
        }
        return true;
    }

    public abstract boolean isSat();

    public abstract Map<Integer, Boolean> getAssignment();
}
