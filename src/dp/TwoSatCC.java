package dp;

import graph.DGraphImpl;
import graph.CCKosaraju;
import graph.TopologicalSort;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.RandGen;

public class TwoSatCC extends TwoSat {

    DGraphImpl<Integer> g;
    final CCKosaraju scc;
    Boolean satisfiability = null;

    private void addToVariables(Integer Xi) {
        int key = Math.abs(Xi);
        if (!variables.containsKey(key)) {
            variables.put(key, RandGen.uniformBool());
        }
    }

    @Override
    public boolean isSat() {
        if (satisfiability == null) {
            satisfiability = true;
            for (Integer v : this.variables.keySet()) {
                if (scc.sameCC(v, -v)) {
                    satisfiability = false;
                    break;
                }
            }
        }
        return satisfiability;
    }

    public TwoSatCC(int[] data) {
        this.N = data.length / 2;
        this.g = new DGraphImpl<>();
        this.variables = new HashMap<>();
        this.equation = new ArrayList<>(data.length / 2);
        for (int i = 0; i < data.length; i += 2) {
            addToVariables(data[i]);
            addToVariables(data[i + 1]);
            g.add(-data[i], data[i + 1]);
            g.add(-data[i + 1], data[i]);
            this.equation.add(new TwoSatLS.clause(data[i], data[i + 1]));
        }
        scc = new CCKosaraju();
        scc.getComponents(g);
        if (this.isSat()) {
            TopologicalSort<Integer> gSorter = new TopologicalSort<>();
            List<Integer> varOrder = gSorter.sort(g);
            Map<Integer, Integer> varIndex = new HashMap<>(this.variables.size());
            for (int i = 0; i < varOrder.size(); i++) {
                varIndex.put(varOrder.get(i), i);
            }
            for (Integer var : this.variables.keySet()) {
                if (varIndex.containsKey(-var) && varIndex.get(var) > varIndex.get(-var)) {
                    variables.put(var, Boolean.FALSE);
                } else {
                    variables.put(var, Boolean.TRUE);
                }
            }
        }
    }

    @Override
    public Map<Integer, Boolean> getAssignment() {
        return this.variables;
    }
}
