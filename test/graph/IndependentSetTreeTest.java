package graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class IndependentSetTreeTest {
    
    @Test
    public void test1() {
        int[] weights = {1,5,3,7,5};
        int[] links = {1,2, 2,3, 2,4, 4,5};
        int[] iset = {1,3,4};
        int expected = 11;
        assert deriveIndependentSetWeigh(weights, links, iset) == expected;
    }
    
    @Test
    public void test2() {
        int[] weights = {3,5,1,6,2,3,7,2,1,2,1};
        int[] links = {1,2, 1,3, 1,4, 2,5, 3,6, 4,7, 4,8, 7,9, 7,10, 7,11};
        int[] iset = {2,4,6,9,10,11};
        int expected = 18;
        assert deriveIndependentSetWeigh(weights, links, iset) == expected;
    }
    
    private Map<Integer, Integer> makeWeights(int[] wts) {
        Map<Integer, Integer> rv = new HashMap<>(wts.length);
        for (int i = 0; i < wts.length; i++) {
            rv.put(i+1, wts[i]);
        }
        return rv;
    }
    
    private DGraphImpl<Integer> makeGraph(int[] links) {
        DGraphImpl<Integer> rv = new DGraphImpl<>();
        for (int i = 0; i < links.length; i+= 2) {
            rv.add(links[i], links[i+1]);
        }
        return rv;
    }

    private int deriveIndependentSetWeigh(int[] weights, int[] links, int[] iset) {
        DGraphImpl<Integer> g = this.makeGraph(links);
        assert g.isAcyclic();
        Map<Integer, Integer> w = this.makeWeights(weights);
        IndependentSetTree<Integer> is = new IndependentSetTree<>(g, w);
        assert is.getRoot() == links[0];
        int weight = is.getCumulativeWeight();
        Set<Integer> st = is.getIndependentSet();
        assert st.size() == iset.length;
        for (int i : iset) {
            assert st.contains(i);
        }
        return weight;
    }
}
