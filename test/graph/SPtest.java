package graph;

import java.util.List;
import org.junit.Test;

public class SPtest {

    @Test
    public void reweighting() {
        WGraphExps<Integer> g = new WGraphExps<>();
        g.link(1, 5, -1);
        g.link(2, 1, 1);
        g.link(2, 4, 2);
        g.link(3, 2, 2);
        g.link(3, 6, -8);
        g.link(4, 1, -4);
        g.link(4, 5, 3);
        g.link(5, 2, 7);
        g.link(6, 2, 5);
        g.link(6, 3, 10);
        g.apsp();

        g = new WGraphExps<>();
        g.link(1, 2, 10);
        g.link(1, 3, -1);
        g.link(1, 4, 1);
        g.link(3, 5, -1);
        g.link(3, 6, -1);
        g.link(4, 7, 100);
        g.link(4, 6, 1);
        g.apsp();
    }
    
    
}