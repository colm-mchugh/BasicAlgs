package dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NSatSolver {

    /**
     * strategy - how to select the next variable for assignment when solving 
     * a formula.
     */
    public enum strategy {
        byIndex, // variables are selected in order of index, e.g. x1, x2, x3, x4, etc
        byCard,  // variables are selected in order of most frequently used
        byClsSize // " are selected in order of smallest clause (#vars in a clause)
    };

    private final NSat instance;
    private List<NSat.pvar> varQueue;
    private boolean isSat;

    /**
     * Create a clause conjunction from the given variable index listings.
     * The formula: (x1 + !x2 + x3).(!x1 + !x3).(x1 + !x2 + !x3) 
     * has clauseData: {{1,-2,3},{-1,-3},{1,-2-3}}
     * 
     * @param clauseData 
     */
    public NSatSolver(int[][] clauseData) {
        List<NSat.clause> formula = new ArrayList<>(clauseData.length);
        for (int[] data : clauseData) {
            List<Integer> nextClause = new ArrayList<>(data.length);
            for (int c : data) {
                nextClause.add(c);
            }
            formula.add(new NSat.clause(nextClause));
        }
        instance = new NSat(formula);
    }

    /**
     * Run sat solver using the given strategy.
     * 1) Make the priority queue of the variables for the given strategy.
     * 2) apply the priority queue to the sat solver.
     * 
     * @param s
     * @return 
     */
    public boolean run(strategy s) {
        makeVarQueue(s);
        isSat = instance.SatSolve(varQueue, 0);
        return isSat;
    }

    public NSat getInstance() {
        return instance;
    }

    public boolean isIsSat() {
        return isSat;
    }

    /**
     * Create the priority queue of variables for the given strategy.
     * 
     * @param s 
     */
    private void makeVarQueue(strategy s) {
        List<NSat.clause> formula = instance.formula;
        Map<Integer, Integer> varVals = new HashMap<>();
        for (NSat.clause cl : formula) {
            for (int v : cl.vars) {
                int kV = Math.abs(v);
                switch (s) {
                    case byIndex:
                        // The priority is just the variables index, e.g. p(x1) = 1,
                        varVals.put(kV, kV);
                        break;
                    case byClsSize:
                        // The priority is the size of the smallest clause containing the var
                        if (varVals.containsKey(kV)) {
                            varVals.put(kV, Integer.min(kV, cl.vars.size()));
                        } else {
                            varVals.put(kV, cl.vars.size());
                        }
                        break;
                    case byCard:
                        // The priority is the number of times the var is used
                        // -ve, because a minimum priority queue is used.
                        if (varVals.containsKey(kV)) {
                            varVals.put(kV, varVals.get(kV) - 1);
                        } else {
                            varVals.put(kV, -1);
                        }
                        break;
                        
                }
            }
        }
        varQueue = new ArrayList<>(varVals.size());
        for (Integer var : varVals.keySet()) {
            varQueue.add(new NSat.pvar(var, varVals.get(var)));
        } 
        Collections.sort(varQueue);
    }


}
