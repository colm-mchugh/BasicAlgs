package heap;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class PairingHeap<Key extends Comparable<Key>> {

    private Key item = null;
    private final LinkedList<PairingHeap<Key>> children;

    public static int nheaps = 0;
    public static int nmerges = 0;
    public static int npairings = 0;
    public static int nchildren = 0;
    public static int max_children = 0;

    public PairingHeap() {
        this.item = null;
        this.children = new LinkedList<>();
        nheaps++;
    }

    public Key Min() {
        return this.item;
    }

    public PairingHeap<Key> Merge(PairingHeap<Key> h1, PairingHeap<Key> h2) {
        if (h1 == null) {
            return h2;
        }
        if (h2 == null) {
            return h1;
        }

        nmerges++;
        if (h1.item.compareTo(h2.item) < 0) {
            h1.children.push(h2);
            max_children = Integer.max(max_children, h1.children.size());
            return h1;
        } else {
            h2.children.push(h1);
            max_children = Integer.max(max_children, h2.children.size());
            return h2;
        }
    }

    PairingHeap<Key> Delete() {
        return merge_pairs(this.children);
    }

    private PairingHeap<Key> merge_pairs(LinkedList<PairingHeap<Key>> l) {
        if (l.isEmpty()) {
            return null;
        }
        if (l.size() == 1) {
            return l.element();
        }
        PairingHeap<Key> l0 = l.pop();
        PairingHeap<Key> l1 = l.pop();
        npairings++;
        return Merge(Merge(l0, l1), merge_pairs(l));
    }

    public PairingHeap<Key> Insert(Key k) {
        if (this.item == null) {
            this.item = k;
            return this;
        }
        PairingHeap<Key> ph = new PairingHeap<>();    
        return Merge(ph.Insert(k), this);
    }

    public void Print() {
        Queue<PairingHeap<Key>> queue = new ArrayDeque<>();
        int n = 1;
        queue.add(this);
        while (!queue.isEmpty()) {
            PairingHeap<Key> next = queue.remove();
            System.out.print(next.item);
            System.out.print(' ');
            n--;
            if (n == 0) {
                System.out.print('*');
                System.out.println();
            }
            for (PairingHeap<Key> v : next.children) {
                queue.add(v);
                n++;
            }
        }

    }
}
