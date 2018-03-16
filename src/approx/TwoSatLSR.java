package approx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import utils.RandGen;

public class TwoSatLSR extends TwoSat {

    Map<Integer, Set<clause>> varFrequencies;
    Stack<Integer> reducedVars;
    Stack<clause> reducedClauses;

    public TwoSatLSR(int[] data) {
        varFrequencies = new HashMap<>();
        reducedVars = new Stack<>();
        reducedClauses = new Stack<>();
        
        this.N = data.length / 2;
        this.variables = new HashMap<>();
        this.equation = new ArrayList<>();
        for (int i = 0; i < data.length; i += 2) {
            clause cl = new clause(data[i], data[i + 1]);
            addToVariables(data[i], cl);
            addToVariables(data[i + 1], cl);
            this.equation.add(cl);
        }
        this.applyReduction();
    }
    
    private void addToVariables(Integer Xi, clause cl) {
        int key = Math.abs(Xi);
        if (!variables.containsKey(key)) {
            variables.put(key, RandGen.uniformBool());
            varFrequencies.put(key, new HashSet<>());
        } 
        varFrequencies.get(key).add(cl);
    }
    
    private void applyReduction() {
        int nreductions = 0;
        while (true) {
            int singleVar = -1;
            boolean noSingleUseVars = true;
            for (Integer var : varFrequencies.keySet()) {
                if (varFrequencies.get(var).size() == 1) {
                    singleVar = var;
                    noSingleUseVars = false;
                    break;
                } 
            }
            if (noSingleUseVars) {
                break;
            }
            this.variables.remove(singleVar);
            Set<clause> cls = varFrequencies.get(singleVar);
            assert cls.size() == 1;
            clause cl = cls.iterator().next();
            Integer other = Math.abs(cl.lVar) == singleVar ? Math.abs(cl.rVar) : Math.abs(cl.lVar);
            assert varFrequencies.get(other).contains(cl);
            
            this.equation.remove(cl);
            this.varFrequencies.remove(singleVar);
            this.variables.remove(singleVar);
            this.varFrequencies.get(other).remove(cl);
            
            this.reducedVars.push(singleVar);
            this.reducedClauses.push(cl);
            N = N - 1;
            nreductions++;
        }
        
        System.out.println("Number of reductions: " + nreductions);
    }
    
    @Override
    public boolean isSat() {
        long trials = (int) (Math.log(N) / Math.log(2));
        long inner = 2 * N * N;
        System.out.println("Beginning " + trials + " trials of " + inner + " iterations");
        for (long trial = 0; trial < trials; trial++) {
            // choose random initial assignment
            for (Integer Xi : this.variables.keySet()) {
                this.variables.put(Xi, RandGen.uniformBool());
            }
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

    @Override
    public Map<Integer, Boolean> getAssignment() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
