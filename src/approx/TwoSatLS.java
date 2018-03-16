package approx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import utils.RandGen;

/* TwoSatLS - 2SAT using local search
 */
public class TwoSatLS extends TwoSat {

    Map<Integer, clause> appearOnce;

    private void addToVariables(int Xi, clause cl) {
        int key = Math.abs(Xi);
        if (!variables.containsKey(key)) {
            variables.put(key, RandGen.uniformBool());
            appearOnce.put(key, cl);
        } else {
            appearOnce.remove(key);
        }
    }

    @Override
    public Map<Integer, Boolean> getAssignment() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void randomizeEquation() {
        for (Integer Xi : this.variables.keySet()) {
            this.variables.put(Xi, RandGen.uniformBool());
        }
    }

    @Override
    public boolean isSat() {
        long trials = (int) (Math.log(N) / Math.log(2));
        long inner = 2 * N * N;
        System.out.println("Beginning " + trials + " trials of " + inner + " iterations");
        for (long trial = 0; trial < trials; trial++) {
            // choose random initial assignment
            this.randomizeEquation();
            for (long i = 0; i < inner; i++) {
                if (eval()) {
                    // current assignment satisfies all clauses => report and halt
                    System.out.println("Satisfiability determined on " + i + "th iteration of " + trial + "th trial");
                    return true;
                }
                // pick arbitrary unsatisfied clause and flip one of its variables
                for (clause c : this.equation) {
                    if (!c.eval(variables)) {
                        c.flip(variables);
                        break;
                    }
                }
            }
        }
        System.out.println("not satisfiable");
        return false;
    }

    public TwoSatLS(int[] data) {
        this.N = data.length / 2;
        this.variables = new HashMap<>();
        this.equation = new ArrayList<>();
        this.appearOnce = new HashMap<>();
        for (int i = 0; i < data.length; i += 2) {
            clause cl = new clause(data[i], data[i + 1]);
            this.equation.add(cl);
            addToVariables(data[i], cl);
            addToVariables(data[i + 1], cl);
        }
        if (this.appearOnce.size() > 0) {
            for (Integer once : this.appearOnce.keySet()) {
                this.variables.remove(once);
            }
            for (Integer once : this.appearOnce.keySet()) {
                clause cl = this.appearOnce.get(once);
                this.equation.remove(cl);
            }
            System.out.println("Removed " + this.appearOnce.size());
            this.N = this.N - this.appearOnce.size();
        }
    }

}
