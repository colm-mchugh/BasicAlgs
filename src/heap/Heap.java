package heap;


public abstract class Heap<Key extends Comparable<Key>> {
    protected Key[] items;
    protected int N;

    public Heap() {
        N = 0;
        items = (Key[]) new Comparable[2];
    }
    
    // Running time: O(log n)
    public void Insert(Key k) {
        if (N >= this.items.length) {
            this.resize(this.items.length * 2);
        }
        this.items[N++] = k;
        this.swim(N);
    }
    
    // Running time: O(log n)
    public Key Delete() {
        Key rv = this.items[0];
        this.Exch(1, N--);
        this.sink(1);
        this.items[N] = null;
        return rv;
    }
    
    public Key Peek() {
        return this.items[0];
    }
    
    public int size() {
        return this.N;
    }
    
    // Running time: O(n log n)
    public Key DeleteSpecificKey(Key k) {
        int i = 0;
        while (!this.items[i].equals(k)) {
            i++;
        }
        Key rv = this.items[i];
        this.Exch(i+1, N--);
        this.sink(i+1);
        this.items[N] = null;
        return rv;
    }
    
    private void swim(int i) {
        while (!heapOrder(i)) {
            Exch(i, i/2);
            i = i/2;
        }
    }
    
    private void sink(int i) {
        while(2*i <= N && (!heapOrder(2*i) || !heapOrder(2*i + 1))) {
            int j = compare(2*i, 2*i + 1);
            Exch(i, j);
            i = j;
        }
    }
    
    protected abstract boolean heapOrder(int i);
    
    protected abstract int compare(int i, int j);
       
    private void Exch(int i, int j) {
        Key tmp = items[i - 1];
        items[i - 1] = items[j - 1];
        items[j - 1] = tmp;
    }
    
    protected Key parent(int i) {
        return this.items[i/2 -1];
    }
    
    private void resize(int capacity) {
        Key[] newItems = (Key[]) new Comparable[capacity];
        System.arraycopy(this.items, 0, newItems, 0, N);
        this.items = newItems;
    }
}
