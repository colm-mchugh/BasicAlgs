package dp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;

public class MWISTest extends TestCase {

    public void testCalc() {
        List<Integer> V = readVertices("resources/mwis.txt");
        Set<Integer> result = MWIS.calc(V);
        System.out.println(result);
        boolean pish = result.contains(1);
        assert !result.contains(2);
        assert result.contains(3);
        assert !result.contains(4);
        assert !result.contains(17);
        assert result.contains(117);
        assert result.contains(517);
        assert !result.contains(997);
    }

    private List<Integer> readVertices(String file) {
        FileReader fr;
        List<Integer> vertices = null;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            vertices = new ArrayList<>(Integer.parseInt(line));
            for (int j = 0; (line = br.readLine()) != null; j++) {
                int weight = Integer.parseInt(line);
                vertices.add(weight);
            }
        } catch (IOException | NumberFormatException e) {
        }
        return vertices;
    }
}
