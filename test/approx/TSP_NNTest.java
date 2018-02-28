package approx;

import org.junit.Test;

public class TSP_NNTest {
    
    @Test
    public void testSomeMethod() {
        TSP_NN tsp = new TSP_NN("resources/nn.txt");
        Float d = tsp.tourDistance(tsp.TSP());
        int pish = (int)Math.floor(d);
        System.out.println("Calculated distance: " + pish);
        assert pish == 1201055;
    }
    
}
