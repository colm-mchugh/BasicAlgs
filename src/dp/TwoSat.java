package dp;

/**
 * 2-SAT evaluator API. 
 */
public interface TwoSat {

    public boolean eval();

    public boolean isSat();

    public void init(String s);
}
