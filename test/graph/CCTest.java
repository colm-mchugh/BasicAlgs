package graph;

import org.junit.Test;

public class CCTest {
    @Test
    public void hello() {
        Graph<Integer> g = new UGraphMapImpl<>();
        
        g.add(0, 1);
        g.add(0, 2);
        g.add(0, 6);
        g.add(6, 4);
        g.add(4, 3);
        g.add(3, 5);
        g.add(5, 0);
        
        g.add(7, 8);
        
        g.add(9, 10);
        g.add(9, 11);
        g.add(9, 12);
        g.add(11, 12);
        
        CC cc = new CC(g);
        
        System.out.println("cc.count()=" + cc.count());
        for (int i = 0; i < 13; i++ ) {
            System.out.println("cc.id(" + i + ")="+ cc.id(i));
        }
    }
}
