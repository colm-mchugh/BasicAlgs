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
     
    /**
     * True if the parent of item i is smaller than i, false otherwise.
     * Also true if i has no parent.
     * @param i
     * @return 
     */
    @Override
    protected boolean heapOrder(int i) {
        if ((i <= 1) || (i > N)) {
            return true;
        }
        return parent(i).compareTo(this.items[i - 1]) <= 0;
    }
    
    /**
     * Return the smallest of the two items i, j
     * @param i
     * @param j
     * @return 
     */
    @Override
    protected int compare(int i, int j) {
        if (j > N) {
            return i;
        }
        if (this.items[j - 1] == null) {
            return i;
        }
        if (this.items[i - 1] == null) {
            return j;
        }
        if (this.items[i - 1].compareTo(this.items[j - 1]) < 0) {
            return i;
        }
        return j;
    }

    
}
