package dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple backtracking NSat solver. Avoids evaluating the formula, instead
 * removing a clause if a variable assignment results in true, or removing 
 * a variable from the clause if its assignment is false. 
 * 
 */
public class NSatSimple {
 
    private List<Integer> varQueue;
    
    /**
     * Return true if the given conjunction of disjunctions is satisfiable.
     * 
     * @param formula
     * @return 
     */
    public boolean isSat(List<Set<Integer>> formula) {
        // Create a list of all the distinct variables in the formula
        Map<Integer, Integer> varVals = new HashMap<>();
        for (Set<Integer> clause : formula) {
            for (Integer var :clause) {
                var = Math.abs(var);
                varVals.put(var, var);
            }
        }
        varQueue = new ArrayList<>(varVals.size());
        for (Integer var : varVals.keySet()) {
            varQueue.add(var);
        } 
        Collections.sort(varQueue);
        // at this point, varQueue holds all the distinct variables in the formula
        return SatSolve(formula, 0);
    }
    
    /**
     * Determine if the given formula can be resolved to true or false.
     * 
     * @param formula a satisfiability formula
     * @param i the next unassigned variable
     * @return 
     */
    public boolean SatSolve(List<Set<Integer>> formula, int i) {
        if (formula.isEmpty()) {
            return true;
        }
        for (Set<Integer> clause : formula) {
            if (clause.isEmpty()) {
                return false;
            }
        }
        // The formula cannot be determined satisfiable or not.
        // Take the next unassigned variable, evaluate the formula 
        // with that variable set to false, and true 
        int x = varQueue.get(i);// next unassigned variable
        if (SatSolve(applyVariable(formula, x, false), i + 1)) {
            return true;
        }   
        return SatSolve(applyVariable(formula, x, true), i + 1);
    }
    
    /**
     * Return the formula that results from applying the given variable assignment
     * x = val to the given formula
     * 
     * @param formula
     * @param x
     * @param val
     * @return 
     */
    private List<Set<Integer>> applyVariable(List<Set<Integer>> formula, int x, boolean val) {
        List<Set<Integer>> newFormula = new ArrayList<>();
        for (Set<Integer> clause : formula) {
            if ((val && clause.contains(x)) || (!val && clause.contains(-x))) {
                // x = val => clause is true => no need to include it in resultant formula
                continue;
            }
            HashSet<Integer> newClause = new HashSet<>(clause);
            if (val && clause.contains(-x)){
                // x = true => if clause contains !x, remove it, becuase it is redundant
                newClause.remove(-x);
            }
            if ((!val && clause.contains(x))) {
                // x = false => if clause contains x remove it, it is redundant
                newClause.remove(x);
            }
            newFormula.add(newClause);
        }
        return newFormula;
    }
   
}
