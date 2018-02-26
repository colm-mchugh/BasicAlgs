package approx;

import approx.TwoSatCC;
import approx.TwoSat;
import approx.TwoSatLS;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.Ignore;
import org.junit.Test;
import utils.RandGen;

public class TwoSatTest {

    @Test
    public void testRandBoolean() {
        int falseCount = 0, trueCount = 0;
        int N = 1000000;
        for (int i = 0; i < N; i++) {
            boolean b = RandGen.uniformBool();
            if (b) {
                trueCount++;
            } else {
                falseCount++;
            }
        }
        System.out.println("trues=" + ((double) trueCount) / N + ", falses=" + ((double) falseCount) / N);
    }

    String[] tests = {
        "resources/2satsm1.txt",
        "resources/2satsm2.txt",
        "resources/2satsm3.txt",
        "resources/2satsm4.txt",
        "resources/2satsm5.txt",};

    boolean[] expected = {
        true, true, false, false, true,};

    protected void runTests(boolean doCC) {
        for (int i = 0; i < tests.length; i++) {
            TwoSat ts = (doCC? new TwoSatCC(readData(tests[i])) : new TwoSatLS(readData(tests[i])));
            assert ts.isSat() == expected[i];
            if (expected[i]) {
                assert ts.eval();
            }
        }
    }

    @Test
    public void testLS() {
        runTests(false);
    }

    @Test
    public void testCC() {
        runTests(true);
    }
    
    @Test
    public void testCCAssignment() {
        int[] equation = {1,-3, -1,2, -2,-3}; // (x1^!x3)(!x1^x2)(!x2^!x3)
        TwoSat solver = new TwoSatCC(equation);
        assert solver.eval();
    }

    @Test
    public void testBigMath() {
        long l = 1000000L;
        long inner = l * l;
        assert inner == 1000000000000L;

        int x = 1000000;
        int xx = x * x;
        assert xx < -1;
    }
    
    @Test
    public void testTwoSatCC() {
        this.testTwoSat(true);
    }
    
    @Ignore // this test is whack - takes way too long
    @Test
    public void testTwoSatLS() {
        this.testTwoSat(false);
    }
    
    private void testTwoSat(boolean doCC) {
        String[] files = {
            "resources/2sat1.txt",
            "resources/2sat2.txt",
            "resources/2sat3.txt",
            "resources/2sat4.txt",
            "resources/2sat5.txt",
            "resources/2sat6.txt",
        };
        StringBuilder ans = new StringBuilder(6);
        for (String file : files) {
            System.out.println("Starting file: " + file);
            long startTime = System.nanoTime();
            TwoSat ts = (doCC? new TwoSatCC(readData(file)) : new TwoSatLS(readData(file)));
            if (ts.isSat()) {
                ans.append('1');
                assert ts.eval();
            } else {
                ans.append('0');
            }
            long elapsedTime = TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            System.out.println("Finished file " + file + " found to be " + (ans.charAt(ans.length()-1) == '0' ? " not ": "") + " satisfiable in (" + elapsedTime + " seconds)");
        }
        System.out.println(ans.toString());
        
        assert ans.toString().equals("101100");
    }

    public int[] readData(String file) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int N = Integer.parseInt(line);
            int[] data = new int[N * 2];
            int i = 0;
            while ((line = br.readLine()) != null) {
                if ("".equals(line)) {
                    continue;
                }
                String[] split = line.trim().split("(\\s)+");
                data[i++] = Integer.parseInt(split[0]);
                data[i++] = Integer.parseInt(split[1]);      
            }
            return data;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

}
