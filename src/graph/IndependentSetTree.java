package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndependentSetTree<T> {
    private final DGraphImpl<T> graph;
    private final Map<T, Integer> weights;
    private final Map<T, weight> cumulativeWeights;
    private final Set<T> independentSet;
    private final T root; 
    private final int cumulativeWeight;

    private static class weight {
        int value;
        boolean fromChildren; // true if value is derived from children,
                              // false if derived from self + grandchildren
    }

    public IndependentSetTree(DGraphImpl<T> graph, Map<T, Integer> weights) {
        this.graph = graph;
        this.weights = weights;
        cumulativeWeights = new HashMap<>(graph.numVertices());
        TopologicalSort<T> sorter = new TopologicalSort<>();
        List<T> ordering = sorter.sort(graph);
        root = ordering.get(ordering.size() - 1);
        cumulativeWeight = makeWeights(root);
        independentSet = makeIndependentSet(new HashSet<T>(), root);
    } 
    
    private boolean isLeaf(T v) {
        return graph.connections(v).isEmpty();
    }
    
    private int makeWeights(T v) {
        if (v == null) {
            return 0;
        }
        if (!this.cumulativeWeights.containsKey(v)) {
            weight myWeight = new weight();
            if (isLeaf(v)) {
                myWeight.value = weights.get(v);
                myWeight.fromChildren = false;
            } else {
                int childWeight = 0;
                int grandChildWeight = weights.get(v);
                for (T u : graph.connections(v)) {
                    childWeight += makeWeights(u);
                    for (T w : graph.connections(u)) {
                        grandChildWeight += makeWeights(w);
                    }
                }
                myWeight.value = Integer.max(grandChildWeight, childWeight);
                myWeight.fromChildren = childWeight > grandChildWeight;
            }
            this.cumulativeWeights.put(v, myWeight);
        }
        return this.cumulativeWeights.get(v).value;
    }

    private Set<T> makeIndependentSet(Set<T> s, T v) {
        weight w = this.cumulativeWeights.get(v);
        if (!w.fromChildren) {
            s.add(v);
        }
        if (!isLeaf(v)) {
            for (T u : graph.connections(v)) {
                if (w.fromChildren) {
                    s = makeIndependentSet(s, u);
                } else {
                    for (T x : graph.connections(u)) {
                        s = makeIndependentSet(s, x);
                    }
                }
            }
        }
        return s;
    }
    
    
    public DGraphImpl<T> getGraph() {
        return graph;
    }

    public Map<T, Integer> getWeights() {
        return weights;
    }

    public T getRoot() {
        return root;
    }

    public int getCumulativeWeight() {
        return cumulativeWeight;
    }

    public Set<T> getIndependentSet() {
        return independentSet;
    }
    
}
