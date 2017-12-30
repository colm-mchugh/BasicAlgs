package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

public class CCKosarajuIterative<T> {

    static class Vertex<T> {

        boolean explored;
        boolean ordered;
        T data;

        public Vertex(T data) {
            this.data = data;
            this.explored = false;
            this.ordered = false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + Objects.hashCode(this.data);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Vertex<?> other = (Vertex<?>) obj;
            if (!Objects.equals(this.data, other.data)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "{" + data + ", " + explored + '}';
        }

    }

    static class Graph<T> {
        // rep is the graph representation; each vertex maps to its connected vertices

        final protected Map<Vertex<T>, Set<Vertex<T>>> rep;
        final protected Map<T, Vertex<T>> vertexMap;

        public Graph() {
            rep = new HashMap<>();
            vertexMap = new HashMap<>();
        }

        public void add(T u, T v) {
            Vertex<T> uV = fetch(u);
            Vertex<T> vV = fetch(v);
            rep.get(uV).add(vV);
        }

        private Vertex<T> fetch(T v) {
            if (!vertexMap.containsKey(v)) {
                vertexMap.put(v, new Vertex<>(v));
            }
            Vertex<T> vV = vertexMap.get(v);
            if (!rep.containsKey(vV)) {
                rep.put(vV, new HashSet<>());
            }
            return vV;
        }

        public Graph<T> reverse() {
            Graph<T> myReverse = new Graph<>();
            for (Vertex<T> v : rep.keySet()) {
                Set<Vertex<T>> us = rep.get(v);
                for (Vertex<T> u : us) {
                    myReverse.add(u.data, v.data);
                }
            }
            return myReverse;
        }

    }

    final public Map<T, Set<T>> components = new LinkedHashMap<>();
    protected final Map<T, T> index = new HashMap<>();

    private Graph<T> g;

    public Graph<T> makeGraph(List<T> elements) {
        g = new Graph<>();
        for (int i = 0; i < elements.size(); i += 2) {
            g.add(elements.get(i), elements.get(i + 1));
        }
        return g;
    }

    public Map<T, Set<T>> getComponents(List<T> elements) {
        g = makeGraph(elements);

        Graph<T> gRev = g.reverse();
        List<Vertex<T>> ordering = new ArrayList<>(elements.size());
        for (Vertex<T> v : gRev.rep.keySet()) {
            if (!v.explored) {
                this.topOrder(gRev, v, ordering);
            }
        }

        for (int i = ordering.size() - 1; i >= 0; i--) {
            Vertex<T> v = ordering.get(i);
            v = g.vertexMap.get(v.data);
            if (!v.explored) {
                components.put(v.data, new HashSet<>());
                this.populateConnectedComponents(g, v, v);
            }
        }
        return components;
    }

    public List<Integer> ccSizes() {
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
        return rv;
    }

    public boolean sameCC(T t1, T t2) {
        T l1 = this.index.get(t1);
        T l2 = this.index.get(t2);
        return l1 != null && l2 != null && l1 == l2;
    }

    public void topOrder(Graph<T> graph, Vertex<T> source, List<Vertex<T>> ordering) {
        Stack<Vertex<T>> stack = new Stack<>();
        stack.push(source);
        while (!stack.isEmpty()) {
            Vertex<T> v = stack.peek();
            if (!v.explored) {
                v.explored = true;
            }
            boolean doneWithV = true;
            for (Vertex<T> u : graph.rep.get(v)) {
                if (!u.explored) {
                    stack.push(u);
                    doneWithV = false;
                }
            }
            if (doneWithV) {
                if (!v.ordered) {
                    ordering.add(v);
                    v.ordered = true;
                }
                stack.pop();
            }
        }

    }

    public void populateConnectedComponents(Graph<T> graph, Vertex<T> leader, Vertex<T> source) {
        Stack<Vertex<T>> stack = new Stack<>();
        stack.push(source);
        while (!stack.isEmpty()) {
            Vertex<T> v = stack.pop();
            if (!v.explored) {
                v.explored = true;
                this.components.get(leader.data).add(v.data);
                this.index.put(v.data, leader.data);
            }
            for (Vertex<T> u : graph.rep.get(v)) {
                if (!u.explored) {
                    stack.push(u);
                }
            }
        }
    }

}
