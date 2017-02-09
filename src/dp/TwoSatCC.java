package dp;

import graph.DGraphImpl;
import graph.CCCer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.RandGen;

public class TwoSatCC implements TwoSat {

    long N;
    List<TwoSatLS.clause> equation;
    DGraphImpl<Integer> g;
    Map<Integer, Boolean> variables;

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
        this.equation = new ArrayList<>();
        for (int i = 0; i < data.length; i += 2) {
            addToVariables(data[i]);
            addToVariables(data[i + 1]);
            g.add(-data[i], data[i + 1]);
            g.add(-data[i + 1], data[i]);
            this.equation.add(new TwoSatLS.clause(data[i], data[i + 1]));
        }
    }

    @Override
    public boolean eval() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
