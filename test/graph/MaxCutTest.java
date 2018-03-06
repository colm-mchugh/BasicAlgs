package graph;

import junit.framework.TestCase;

public class MaxCutTest extends TestCase {
    
    public void testStarGraph() {
        MaxCut instance = new MaxCut();
        int[] star = {0,3, 1,3, 2,3, 4,3, 5,3, 6,3, 7,3, 8,3, 9,3 };
        GraphCut mcut = instance.maxCut(makeUGraph(star));
        int crossings = mcut.crossings();
        assert crossings == 9;
    }
    
    public void testSimpleCLRS() {
        MaxCut instance = new MaxCut();
        int[] CLRS = {0,1, 1,2, 2,3, 3,4, 4,5, 6,3, 2,4, 3,5 };
        GraphCut mcut = instance.maxCut(makeUGraph(CLRS));
        int crossings = mcut.crossings();
        assert crossings == 6;
    }
 
    Graph<Integer> makeDGraph(int[] links) {
        Graph<Integer> gD = new DGraphImpl<>();
        GraphIO.populateGraph(gD, links);
        return gD;
    }
    
    Graph<Integer> makeUGraph(int[] links) {
        Graph<Integer> gD = new UGraphMapImpl<>();
        GraphIO.populateGraph(gD, links);
        return gD;
    }
}
