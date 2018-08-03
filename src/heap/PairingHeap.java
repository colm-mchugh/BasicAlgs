package heap;

import java.util.LinkedList;

public class PairingHeap<Key extends Comparable<Key>>  {
    
    private final Key item;
    private final LinkedList<PairingHeap<Key>> children;

    public PairingHeap(Key item) {
        this.item = item;
        this.children = new LinkedList<>();
    }   
    
    public Key Peek() {
        return this.item;
    }
    
    public PairingHeap<Key> Merge(PairingHeap<Key> h1, PairingHeap<Key> h2) {
        if (h1 == null) {
            return h2;
        }
        if (h2 == null) {
            return h1;
        }
        if (h1.item.compareTo(h2.item) < 0) {
            h1.children.push(h2);
            return h1;
        } else {
            h2.children.push(h1);
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
        return Merge(Merge(l0, l1), merge_pairs(l));
    }
    
    public PairingHeap<Key> Insert(Key k) {
        return Merge(new PairingHeap<>(k), this);
    }
}
