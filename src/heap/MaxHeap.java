package heap;


public class MaxHeap<Key extends Comparable<Key>> extends Heap<Key> {

    public MaxHeap() {
        super();
    }
     
    /**
     * True if the parent of item i is greater than i, false otherwise.
     * Also true if i has no parent.
     * @param i
     * @return 
     */
    @Override
    protected boolean heapOrder(int i) {
        if ((i <= 1) || (i > N)) {
            return true;
        }
        return parent(i).compareTo(this.items[i - 1]) > 0;
    }
    
    /**
     * Given two item i, j return i if items[i] >= items[j], 
     * otherwise return j.
     * 
     * If items[i] is NULL return j, and vice versa.
     * 
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
        if (this.items[i - 1].compareTo(this.items[j - 1]) >= 0) {
            return i;
        }
        return j;
    }

}
