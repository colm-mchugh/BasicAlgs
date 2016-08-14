package graph;

import java.util.Map;
import java.util.Set;
import org.junit.Test;


public class StrongCCTest {

    @Test
    public void test1() {
        DGraphImpl<Integer> g = new DGraphImpl<>();
        g.add(9, 7);
        g.add(8, 9);
        g.add(7, 8);
        g.add(6, 9);
        g.add(6, 1);
        g.add(5, 6);
        g.add(4, 5);
        g.add(4, 2);
        g.add(3, 4);
        g.add(2, 3);
        g.add(1, 5);
        //g.add(7, 3); // Counter example for "ccs never change", "ccs never decrease by more than 1"
        g.add(3, 6);
        StrongCC scc = new StrongCC(g);
        scc.ccSizes();       
    }

    @Test
    public void test2() {
        DGraphImpl<Integer> g = new DGraphImpl<>();
        g.add(1, 2);
        g.add(2, 3);
        g.add(3, 1);
        g.add(2, 4);
        g.add(3, 5);
        g.add(3, 6);
        g.add(5, 6);
        g.add(6, 7);
        g.add(6, 8);
        g.add(8, 5);
        g.add(7, 8);
        g.add(4, 9);
        g.add(4, 10);
        g.add(10, 11);
        g.add(11, 9);
        g.add(9, 10);
        g.add(6, 9);
        g.add(7, 11);
        g.print();
    }

    private void doit(DGraphImpl<Integer> g) {
        StrongCC<Integer> CCs = new StrongCC<>(g);
        System.out.println();
        Map<Integer, Set<Integer>> pish = CCs.ccs();
        for (Integer foo : pish.keySet()) {
            Set<Integer> comps = pish.get(foo);
            for (Integer c : comps) {
                System.out.print(c);
                System.out.print(' ');
            }
            System.out.println();
        }
    }
}
