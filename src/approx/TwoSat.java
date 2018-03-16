package approx;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 61 * hash + Objects.hashCode(this.lVar);
            hash = 61 * hash + Objects.hashCode(this.rVar);
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
            final clause other = (clause) obj;
            if (!Objects.equals(this.lVar, other.lVar)) {
                return false;
            }
            return Objects.equals(this.rVar, other.rVar);
        }
        
        
    }

    protected Map<Integer, Boolean> variables;
    protected long N;
    protected List<clause> equation;
    
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
