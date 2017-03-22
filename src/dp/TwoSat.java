package dp;

import java.util.Map;

/**
 * 2-SAT evaluator API. 
 */
public interface TwoSat {

    public boolean eval();

    public boolean isSat();

    public Map<Integer, Boolean> getAssignment();
}
