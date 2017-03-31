package dp;

import org.junit.Test;

public class NSatTest {
    
    @Test
    public void testSatSolveFalse() {
        int[][] unsat = {{1,2,3,4},{-1},{1,2,-3},{1,-2},{1,-4}};
        assert !this.solve(unsat, NSatSolver.strategy.byIndex);
        assert !this.solve(unsat, NSatSolver.strategy.byCard);
        assert !this.solve(unsat, NSatSolver.strategy.byClsSize);
    }
    
    @Test
    public void testSatSolveTrue() {
        int[][] unsat = {{1,2,3},{1,-2,3},{1,2,-3},{1,-2,-3}};
        assert this.solve(unsat, NSatSolver.strategy.byIndex);
        assert this.solve(unsat, NSatSolver.strategy.byCard);
        assert this.solve(unsat, NSatSolver.strategy.byClsSize);
    }
    
    private boolean solve(int[][] clauseData, NSatSolver.strategy strat) {
        NSatSolver inst = new NSatSolver(clauseData);
        inst.run(strat);
        return inst.isIsSat();
    }
}
