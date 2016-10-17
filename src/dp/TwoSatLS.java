package dp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import utils.RandGen;

/* TwoSatLS - 2SAT using local search
*/
public class TwoSatLS implements TwoSat {

    Map<Integer, Boolean> variables;
    long N;
    List<clause> equation;
    Set<Integer> appearOnce;

    private void addToVariables(Integer Xi) {
        int key = Math.abs(Xi);
        if (!variables.containsKey(key)) {
            variables.put(key, RandGen.uniformBool());
            appearOnce.add(key);
        } else {
            appearOnce.remove(key);
        }
    }

    public static class clause {

        public Integer lVar;
        public Integer rVar;

        public clause(Integer lVar, Integer rVar) {
            this.lVar = lVar;
            this.rVar = rVar;
        }

        public boolean eval(Map<Integer, Boolean> ctxt) {
            Boolean lval = this.lookup(lVar, ctxt);
            Boolean rval = this.lookup(rVar, ctxt);
            return lval || rval;
        }

        public boolean lookup(Integer k, Map<Integer, Boolean> ctxt) {
            Boolean v = ctxt.get(Math.abs(k));
            if (v == null) {
                return true;
            }
            return (k < 0 ? !v : v);
        }

        public void flip(Map<Integer, Boolean> ctxt) {
            // uniformly flip one of lVar or rVar
            Integer toflip = Math.abs((RandGen.uniformBool() ? lVar : rVar));
            ctxt.put(toflip, !ctxt.get(toflip));
        }

        @Override
        public String toString() {
            return "(" + lVar + " || " + rVar + ')';
        }
    }

    @Override
    public boolean eval() {
        for (clause c : equation) {
            if (!c.eval(variables)) {
                return false;
            }
        }
        return true;
    }

    public void randomizeEquation() {
        for (Integer Xi : this.variables.keySet()) {
            this.variables.put(Xi, RandGen.uniformBool());
        }
    }

    @Override
    public boolean isSat() {
        long trials = (int) (Math.log(N) / Math.log(2));
        long inner = 2 * N * N;
        System.out.println("Beginning " + trials + " trials of " + inner + " iterations");
        for (long trial = 0; trial < trials; trial++) {
            // choose random initial assignment
            this.randomizeEquation();
            for (long i = 0; i < inner; i++) {
                if (eval()) {
                    // current assignment satisfies all clauses => report and halt
                    System.out.println("Satisfiability determined on " + i + "th iteration of " + trial + "th trial");
                    return true;
                }
                // pick arbitrary unsatisfied clause and flip one of its variables
                for (clause c : this.equation) {
                    if (!c.eval(variables)) {
                        c.flip(variables);
                        break;
                    }
                }
            }
        }
        System.out.println("not satisfiable");
        return false;
    }

    @Override
    public void init(String file) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            this.N = Long.parseLong(line);
            this.variables = new HashMap<>();
            this.equation = new ArrayList<>();
            this.appearOnce = new HashSet<>();
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
                clause cl = new clause(Xi, Yi);
                this.equation.add(cl);
            }
            if (this.appearOnce.size() > 0) {
                for (Integer once : this.appearOnce) {
                    this.variables.remove(once);
                }
                System.out.println("Removed " + this.appearOnce.size());
                for (Integer once : this.appearOnce) {
                    for (int i = 0; i < this.equation.size(); i++) {
                        if (this.equation.get(i).lVar.equals(once) || this.equation.get(i).rVar.equals(once)) {
                            this.equation.remove(i);
                            break;
                        }
                    }
                }
                this.N = this.N - this.appearOnce.size();
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
            "resources/2sat6.txt",};
        TwoSatLS ts = new TwoSatLS();
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
            System.out.println("Finished file " + file + "(" + elapsedTime + " seconds)");
        }
        System.out.println(ans.toString());
    }
}
