package dp;

import graph.DGraphImpl;
import graph.CCCer;
import graph.TopologicalSort;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.RandGen;

public class TwoSatCC implements TwoSat {

    long N;
    DGraphImpl<Integer> g;
    Map<Integer, Boolean> variables;
    Map<Integer, Boolean> varAssignment;
    
    private void addToVariables(Integer Xi) {
        int key = Math.abs(Xi);
        if (!variables.containsKey(key)) {
            variables.put(key, RandGen.uniformBool());
        }
    }

    @Override
    public boolean isSat() {
        CCCer scc = new CCCer(g);
        for (Integer v : this.variables.keySet()) {
            if (scc.sameCC(v, -v)) {
                return false;
            }
        }
        return true;
    }

    public TwoSatCC(int[] data) {
        this.N = data.length / 2;
        this.g = new DGraphImpl<>();
        this.variables = new HashMap<>();
        for (int i = 0; i < data.length; i += 2) {
            addToVariables(data[i]);
            addToVariables(data[i + 1]);
            g.add(-data[i], data[i + 1]);
            g.add(-data[i + 1], data[i]);
        }
    }

    @Override
    public boolean eval() {
        TopologicalSort<Integer> gSorter = new TopologicalSort<>();
        List<Integer> varOrder = gSorter.sort(g);
        Map<Integer, Integer> varIndex = new HashMap<>(this.variables.size());
        varAssignment = new HashMap<>(this.variables.size());
        for (int i = 0; i < varOrder.size(); i++) {
            varIndex.put(varOrder.get(i), i);
        }
        for (Integer var : this.variables.keySet()) {
            if (varIndex.containsKey(-var) && varIndex.get(var) > varIndex.get(-var)) {
                varAssignment.put(var, Boolean.FALSE);
            } else {
                varAssignment.put(var, Boolean.TRUE);
            }
        }
        return true;
    }

    @Override
    public Map<Integer, Boolean> getAssignment() {
        return this.varAssignment;
    }
}
