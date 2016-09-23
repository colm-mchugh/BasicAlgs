package graph;

import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class StrongCCTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

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

    @Test
    public void test3() {
        DGraphImpl<Integer> g = new DGraphImpl<>();
        g.add(2, 3);
        g.add(3, 4);
        g.add(4, 5);
        g.add(5, 6);
        g.add(6, 1);
        g.add(1, 5);
        g.add(4, 2);
        g.add(7, 8);
        g.add(8, 9);
        g.add(9, 7);
        g.add(6, 9);
        g.add(6, 8);
        StrongCC scc = new StrongCC(g);
        scc.ccSizes(); 
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

    /**
     * Test of ccs method, of class StrongCC.
     */
    @Test
    public void testCcs() {
        System.out.println("ccs");
        StrongCC instance = null;
        Map expResult = null;
        Map result = instance.ccs();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ccSizes method, of class StrongCC.
     */
    @Test
    public void testCcSizes() {
        System.out.println("ccSizes");
        StrongCC instance = null;
        instance.ccSizes();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of finishingTimes method, of class StrongCC.
     */
    @Test
    public void testFinishingTimes() {
        System.out.println("finishingTimes");
        StrongCC instance = null;
        Iterable expResult = null;
        Iterable result = instance.finishingTimes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of print method, of class StrongCC.
     */
    @Test
    public void testPrint() {
        System.out.println("print");
        StrongCC instance = null;
        instance.print();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
