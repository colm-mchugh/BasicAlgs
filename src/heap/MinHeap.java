package heap;


public class MinHeap<Key extends Comparable<Key>> extends Heap<Key> {

    public MinHeap() {
        super();
    }
     
    @Override
    protected boolean heapOrder(int i) {
        if ((i <= 1) || (i > N)) {
            return true;
        }
        return parent(i).compareTo(this.items[i - 1]) <= 0;
    }
    
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
