package graph;

import java.util.Set;
import org.junit.Test;

public class VertexCoverTest {
    
    @Test
    public void testVertexCover() {
        int[] links = {1,8, 1,3, 2,8, 2,3, 3,6, 3,7, 3,4, 4,5, 5,6, 5,7, 5,8, 7,8};
        Graph<Integer> gU = new UGraphMapImpl<>();
        Graph<Integer> gD = new DGraphImpl<>();
        
        GraphIO.populateGraph(gU, links);
        
        Set<Integer> vcU = gU.vertexCover();
        Set<Integer> vcD = gD.vertexCover();     
    }
    
    private void testGraph(int[] links, int expectedSz, int[] expectedCntnts) {
        Graph<Integer> gU = new UGraphMapImpl<>();
        Graph<Integer> gD = new DGraphImpl<>();
        
        GraphIO.populateGraph(gU, links);
        GraphIO.populateGraph(gD, links);
        
        Set<Integer> vcU = gU.vertexCover();
        Set<Integer> vcD = gD.vertexCover();
        
        assert vcU.size() <= expectedSz * 2;
        assert vcD.size() <= expectedSz * 2;
         
        for (int v : expectedCntnts) {
            assert vcU.contains(v);
            assert vcD.contains(v); 
        }
    }
    
    @Test
    public void testVertexCoverSimpleStar() {
        int[] links = {0,3, 1,3, 2,3, 4,3, 5,3, 6,3, 7,3, 8,3, 9,3 };
        int[] expcted = { 3 };
        testGraph(links, 1, expcted);
    }
    
    @Test
    public void testVertexCoverSimpleCLRS() {
        int[] links = {0,1, 1,2, 2,3, 3,4, 4,5, 6,3, 2,4, 3,5 };
        int[] expcted = { 1, 3 };
        testGraph(links, 3, expcted);
    }
    
    @Test
    public void testVertexCoverBig() {
        String file = "resources/graph500-04.txt";
        Graph<Integer> gU = GraphIO.readGraphUndirected(file);
        Graph<Integer> gD = GraphIO.readGraphDirected(file);
        
        long nowU = System.currentTimeMillis();
        Set<Integer> vcU = gU.vertexCover();
        long timeU = System.currentTimeMillis() - nowU;
        
        long nowD = System.currentTimeMillis();
        Set<Integer> vcD = gD.vertexCover();
        long timeD = System.currentTimeMillis() - nowD;
        
        System.out.println("Undirected: size=" + vcU.size() + " millis=" + timeU);
        System.out.println("Directed: size=" + vcD.size() + " millis=" + timeD);
    }
    
    private void verifyVertexCover(Set<Integer> vc, Graph<Integer> g) {
        // TODO: create an edge set of the graph
    }
    
    static class A {
        int x;
        public A() {
            x = 3;
        }
        public int f() {
            return x;
        }
        public int g() {
            return x + f();
        }
    }
    
    static class B extends A {
        int x;
        public B() {
            x = 30;
        }
        public int f() {
            return x;
        }
    }
    
    static class C extends B {
        int x;
        public C() {
            x = 300;
        }
        public int g() {
            return x + f();
        }
    }
    
    @Test
    public void testABC() {
        A ainst = new C();
        System.out.println(ainst.x);
        System.out.println(ainst.f());
        System.out.println(ainst.g());
    }
}
