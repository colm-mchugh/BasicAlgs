package heap;

/**
 * Heap implementation that maintains the smallest item in the first position;
 * Delete() always returns the smallest item in the heap.
 *
 * @author colm_mchugh
 * @param <Key>
 */
public class MinHeap<Key extends Comparable<Key>> extends Heap<Key> {

    public MinHeap() {
        super();
    }

    @Override
    protected boolean heapOrder(int i, int j) {
        return value(i).compareTo(value(j)) <= 0;
    }

    @Override
    protected boolean outOfOrder(int i, int j) {
        return value(i).compareTo(value(j)) > 0;
    }

    @Override
    protected void sink(int i) {
        for (int k = i, j = lchild(k); lchild(k) <= N; j = lchild(k)) {
            if (rchild(k) <= N && value(rchild(k)).compareTo(value(lchild(k))) < 0) {
                j++;
            }
            if (value(k).compareTo(value(j)) < 0) {
                break;
            }
            this.Exch(k, j);
            k = j;
        }
    }

    @Override
    protected void swim(int i) {
        while (true) {
            if (i == 1) {
                break;
            }
            if (value(p(i)).compareTo(value(i)) <= 0) {
                break;
            }
            this.Exch(p(i), i);
            i = p(i);
        }
    }

}
