package graph.shortestpath;

import java.util.List;

public class Path<T> {

    public T u;
    public T v;
    public int d;
    public List<T> path;
    
    public Path(T u, T v, int d) {
        this.u = u;
        this.v = v;
        this.d = d;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(u);
        sb.append(", ");
        sb.append(v);
        sb.append(')');
        if (d == Integer.MAX_VALUE) {
            sb.append(" NO PATH");
        } else {
            sb.append(" d=");
            sb.append(d);
        }
        return sb.toString();
    }

    public final static Path INFINITY = new Path(null, null, Integer.MAX_VALUE);
    
}
