package approx;

import approx.TwoSatLS;
import graph.DGraphImpl;
import graph.CCTarjan;
import graph.CCer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwoSatCC extends TwoSat {

    DGraphImpl<Integer> g;
    final CCer<Integer> scc;
    final Boolean satisfiable;

    private void addVar(Integer Xi) {
        int key = Math.abs(Xi);
        if (!variables.containsKey(key)) {
            variables.put(key, null);
        }
    }

    @Override
    public boolean isSat() {
        return satisfiable;
    }

    public TwoSatCC(int[] data) {
        this.N = data.length / 2;
        this.g = new DGraphImpl<>();
        this.variables = new HashMap<>();
        this.equation = new ArrayList<>(data.length / 2);
        for (int i = 0; i < data.length; i += 2) {
            addVar(data[i]);
            addVar(data[i + 1]);
            g.add(-data[i], data[i + 1]);
            g.add(-data[i + 1], data[i]);
            this.equation.add(new TwoSatLS.clause(data[i], data[i + 1]));
        }
        scc = new CCTarjan<>();
        Map<Integer, List<Integer>> sccs = scc.getComponents(g);
        Boolean isSat = true;
        for (Integer v : this.variables.keySet()) {
            if (scc.sameCC(v, -v)) {
                isSat = false;
                break;
            }
        }
        if (isSat) {
            variables.clear();
            for (List<Integer> cc : sccs.values()) {
                for (Integer var : cc) {                   
                    if (!variables.containsKey(var)) {
                        variables.put(var, Boolean.TRUE);
                        variables.put(-var, Boolean.FALSE);
                    }
                }
            }
        }
        this.satisfiable = isSat;
    }

    @Override
    public Map<Integer, Boolean> getAssignment() {
        return this.variables;
    }
}
