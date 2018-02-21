package graph.shortestpath;

import java.util.List;
import java.util.Objects;

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

    public Path<T> set(T u, T v) {
        this.u = u;
        this.v = v;
        return this;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.u);
        hash = 83 * hash + Objects.hashCode(this.v);
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
        final Path<?> other = (Path<?>) obj;
        if (!Objects.equals(this.u, other.u)) {
            return false;
        }
        if (!Objects.equals(this.v, other.v)) {
            return false;
        }
        return true;
    }

    public final static Path INFINITY = new Path(null, null, Integer.MAX_VALUE);
    
}
