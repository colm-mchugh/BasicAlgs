
import dp.TwoSat;
import dp.TwoSatCC;
import dp.TwoSatLS;
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

    protected void runTests(TwoSat inst) {
        for (int i = 0; i < tests.length; i++) {
            inst.init(tests[i]);
            assert inst.isSat() == expected[i];
        }
    }

    @Test
    public void testLS() {
        TwoSatLS ts = new TwoSatLS();
        runTests(ts);
    }

    @Test
    public void testCC() {
        TwoSatCC ts = new TwoSatCC();
        runTests(ts);
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
}
