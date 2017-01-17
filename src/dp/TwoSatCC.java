package dp;

import graph.DGraphImpl;
import graph.CCCer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
    
    
    @Override
    public void init(String file) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            this.N = Long.parseLong(line);
            this.g = new DGraphImpl<>();
            this.variables = new HashMap<>();
            this.equation = new ArrayList<>();
            Short c = 0;
            while ((line = br.readLine()) != null) {
                if ("".equals(line)) {
                    continue;
                }
                String[] split = line.trim().split("(\\s)+");
                Integer Xi = Integer.parseInt(split[0]);
                Integer Yi = Integer.parseInt(split[1]);
                addToVariables(Xi);
                addToVariables(Yi);
                g.add(-Xi, Yi);
                g.add(-Yi, Xi);
                TwoSatLS.clause cl = new TwoSatLS.clause(Xi, Yi);
                this.equation.add(cl);
            }
            
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        String[] files = {
            "resources/2sat1.txt",
            "resources/2sat2.txt",
            "resources/2sat3.txt",
            "resources/2sat4.txt",
            "resources/2sat5.txt",
            "resources/2sat6.txt",
        };
        TwoSatCC ts = new TwoSatCC();
        StringBuilder ans = new StringBuilder(6);
        for (String file : files) {
            long startTime = System.nanoTime();

            ts.init(file);
            if (ts.isSat()) {
                ans.append('1');
            } else {
                ans.append('0');
            }
            long elapsedTime = TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            System.out.println("Finished file " + file + " found to be " + (ans.charAt(ans.length()-1) == '0' ? " not ": "") + " satisfiable in (" + elapsedTime + " seconds)");
        }
        System.out.println(ans.toString());
    }

    @Override
    public boolean eval() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
