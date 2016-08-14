package graph;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class StrongCC<T> {

    private final DGraphImpl<T> g;
    private final Map<T, Set<T>> components;
    private final Set<T> visited;
    private final List<T> ordering;
    private T leader;

    private static final boolean FIRST_PASS = true;
    private static final boolean SECOND_PASS = false;

    public StrongCC(DGraphImpl<T> rep) {
        this.g = rep;
        this.components = new HashMap<>();
        visited = new HashSet<T>();
        ordering = new ArrayList<>(g.numVertices());
        Graph<T> rev = g.reverse();
        for (T v : rev.V()) {
            if (!visited.contains(v)) {
                this.doDFS(rev, v, FIRST_PASS);
            }
        }
        visited.clear();
        for (int i = ordering.size() - 1; i >= 0; i--) {
            T v = ordering.get(i);
            if (!visited.contains(v)) {
                leader = v;
                this.components.put(leader, new HashSet<T>());
                this.doDFS(g, v, SECOND_PASS);
            }
        }
    }

    private void doDFS(Graph<T> graph, T source, boolean whichPass) {
        visited.add(source);
        if (whichPass == SECOND_PASS) {
            this.components.get(this.leader).add(source);
        }
        for (T v : graph.connections(source)) {
            if (!visited.contains(v)) {
                doDFS(graph, v, whichPass);
            }
        }
        if (whichPass == FIRST_PASS) {
            ordering.add(source);
        }
    }

    public Map<T, Set<T>> ccs() {
        return this.components;
    }

    public void ccSizes() {
        List<Integer> rv = new ArrayList<>(this.components.keySet().size());
        for (T v : this.components.keySet()) {
            rv.add(this.components.get(v).size());
        }
        rv.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println("#components=" + this.components.keySet().size());
        int brake = 5;
        for (Integer i : rv) {
            System.out.print(i);
            if (--brake == 0) {
                break;
            }
            System.out.print(',');
        }
        System.out.println();
    }

    public Iterable<T> finishingTimes() {
        return this.ordering;
    }

    public void print() {
        for (T foo : this.components.keySet()) {
            Set<T> comps = this.components.get(foo);
            for (T c : comps) {
                System.out.print(c);
                System.out.print(' ');
            }
            System.out.println();
        }
    }
}
