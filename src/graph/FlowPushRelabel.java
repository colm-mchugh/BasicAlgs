package graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public class FlowPushRelabel<T> extends Flow<T> {

    static class Vertex {

        public int height;
        public int excess;

        public Vertex() {
            this.height = 0;
            this.excess = 0;
        }

        public void relabel() {
            this.height += 1;
        }

        @Override
        public String toString() {
            return "h=" + height + ", xs=" + excess;
        }

    }

    private final Map<T, Vertex> vertices;
    private final PriorityQueue<T> excess;
    private Max<T> theFlow;
    private final boolean logging;

    public FlowPushRelabel(boolean logging) {
        vertices = new HashMap<>();
        excess = new PriorityQueue<>(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return vertices.get(o2).height - vertices.get(o1).height;
            }
        });
        this.logging = logging;
    }

    public void clear() {
        super.clear();
        vertices.clear();
        assert(excess.isEmpty());
    }
    
    public void initialize(T s, T t) {
        for (T v : this.V()) {
            vertices.put(v, new Vertex());
        }
        vertices.get(s).height = this.numVertices();

        // Saturate all edges (s, v)
        for (Edge<T> e : this.edgesOf(s)) {
            T v = e.other(s);
            int cap = e.residualCapacity(v);
            if (cap == 0) {
                continue;
            }
            e.addResidualFlow(v, cap);
            vertices.get(v).excess = cap;
            if (!Objects.equals(v, t)) { // if s,t happen to be connected, the excess can stay at t
                excess.add(v);
            }
        }
    }

    private void Push(T u, T v, Edge<T> eP) {
        Vertex uV = vertices.get(u);
        Vertex vV = vertices.get(v);
        
        int delta = Integer.min(uV.excess, eP.residualCapacity(v));

        if (logging)
            System.out.println("PUSH(" + u + ", " + v + ") delta=" + delta);
        
        eP.addResidualFlow(v, delta);
        uV.excess -= delta;
        if (uV.excess == 0) {
            excess.remove(u);
        } else if (!excess.contains(u)) {
            excess.add(u);
        }
        vV.excess += delta;
        
        // Sink, Source do not get put in the excess queue
        if (Objects.equals(v, theFlow.sink)) {
            return;
        }     
        if (Objects.equals(v, theFlow.source)) {
            return;
        }
        
        if (excess.contains(v)) {
            excess.remove(v);
        }
        excess.add(v);
    }

    @Override
    Max<T> getMax(T s, T t) {     
        theFlow = new Max<>(s, t, this);       
        this.initialize(s, t);

        while (!excess.isEmpty()) {
            T u = excess.remove();
            Vertex uV = vertices.get(u);
            int hU = uV.height;
            if (hU == 0) {
                // No point in finding a lower edge if height already 0
                uV.relabel();
                excess.add(u);
                if (logging)
                    System.out.println("RELABEL: h(" + u +")= 1");
                continue;
            }
            
            boolean pushed = false;
            for (Edge<T> e : this.edgesOf(u)) {
                T v = e.other(u);
                int hV = vertices.get(v).height;
                if (e.residualCapacity(v) > 0 && hU == hV + 1) {
                    this.Push(u, v, e);
                    pushed = true;
                    break;
                }
            }
            
            if (!pushed) {
                uV.relabel();
                excess.add(u);
                if (logging)
                    System.out.println("RELABEL: h(" + u +")= " + uV.height);
            }
        }
        theFlow.value = vertices.get(theFlow.sink).excess;
        
        if (logging) {
            int excessCheck = Math.abs(this.excess(theFlow.sink));            
            if (excessCheck != theFlow.value)
                System.out.println("excessCheck (" + excessCheck + ") / Flow(" + theFlow.value +") mismatch");
            assert(excessCheck == theFlow.value);        
        }
        return theFlow;
    }

}
