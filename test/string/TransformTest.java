package string;

import java.util.List;
import org.junit.Test;

public class TransformTest {

    @Test
    public void testOf1() {
        String X = "ATGATCGGCAT";
        String Y = "CAATGTGAATC";
        Transform.Step[] expResult = {
            new Transform.Step('A', Transform.Op.DEL),
            new Transform.Step('C', Transform.Op.REPL),
            new Transform.Step('A', Transform.Op.REPL),
            new Transform.Step('A', Transform.Op.COPY),
            new Transform.Step('T', Transform.Op.COPY),
            new Transform.Step('G', Transform.Op.REPL),
            new Transform.Step('T', Transform.Op.REPL),
            new Transform.Step('G', Transform.Op.COPY),
            new Transform.Step('A', Transform.Op.REPL),
            new Transform.Step('A', Transform.Op.COPY),
            new Transform.Step('T', Transform.Op.COPY),
            new Transform.Step('C', Transform.Op.INS)
        };
        List<Transform.Step> result = Transform.Of(X, Y);
        assert result.size() == expResult.length;
        for (int i = 0; i < result.size(); i++) {
            assert result.get(i).equals(expResult[i]);
        }
    }

    @Test
    public void testOf2() {
        String X = "ACAAGC";
        String Y = "CCGT";
        Transform.Step[] expResult = {
            new Transform.Step('A', Transform.Op.DEL),
            new Transform.Step('C', Transform.Op.COPY),
            new Transform.Step('A', Transform.Op.DEL),
            new Transform.Step('C', Transform.Op.REPL),
            new Transform.Step('G', Transform.Op.COPY),
            new Transform.Step('T', Transform.Op.REPL),
        };
        List<Transform.Step> result = Transform.Of(X, Y);
        assert result.size() == expResult.length;
        for (int i = 0; i < result.size(); i++) {
            assert result.get(i).equals(expResult[i]);
        }
    }

    @Test
    public void testDistance1() {
        String[][] tests = {
            {"sunday", "saturday"},
            {"intention", "execution"},
            {"geek", "gesek"},
            {"cat", "cut"},
            {"horse", "ros"},
            {"kitten", "sitting"},
        };
        int[] results = {3, 5, 1, 1, 3, 3};

        for (int i = 0; i < tests.length; i++) {
            Transform t = new Transform(tests[i][0], tests[i][1]);
            assert t.distance() == results[i];
        }
    }
    
    @Test
    public void testDistance2() {
        String word = "graffe";
        String[] matches = { "graf", "graft", "grail", "giraffe" };
        int minIndex = -1;
        int curMin = Integer.MAX_VALUE;
        for (int i = 0; i < matches.length; i++) {
            Transform t = new Transform(word, matches[i]);
            int dist = t.distance();
            if (dist < curMin) {
                curMin = dist;
                minIndex = i;
            }
        }
        assert minIndex == 3;
        assert curMin == 1;
    }
}
