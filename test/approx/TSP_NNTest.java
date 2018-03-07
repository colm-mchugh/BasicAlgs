package approx;

import org.junit.Test;

public class TSP_NNTest {

    @Test
    public void tests() {

        String[] files = {"nn.txt", "input_simple_60_8000.txt", "input_simple_72_40000.txt"};
        int[] expected = {1201055, 635928, 7060869};

        for (int i = 0; i < files.length; i++) {
            TSP_NN tsp = new TSP_NN("resources/" + files[i]);
            Float d = tsp.tourDistance(tsp.TSP());
            assert (int) Math.floor(d) == expected[i];
        }
    }

}
