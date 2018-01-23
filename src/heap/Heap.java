package heap;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for maintaining a heap of items according to some priority
 * e.g. the smallest item is always first, or the largest item is always first.
 * Items in a heap must be Comparable.
 *
 * @author colm_mchugh
 * @param <Key>
 */
public abstract class Heap<Key extends Comparable<Key>> {

    /**
     * The items are maintained in an array, where N is the number of items.
     * All heap operations must maintain the following invariants:
     * 1) items[0] is the highest priority item in the heap
     * 2) for all items i in the heap, priority of items[i/2 - 1] has higher priority than items[i]
     * 3) a map of item to item index is maintained to enable O(1) access of any item.
     *    Operations that mutate the heap (Insert, Delete, DeleteSpecificKey) check
     *    that the following invariant holds on exit: #items in heap == #items in item index
     */
    protected Key[] items; // the heap
    protected int N;    // #items in the heap
    protected Map<Key, Integer> itemIndex; // key to heap position

    /**
     * Create an empty heap
     */
    public Heap() {
        N = 0;
        items = (Key[]) new Comparable[2];
        itemIndex = new HashMap<>();
    }

    /**
     * Put the given item in the heap. 
     * 
     * The item is placed in the next available slot. It may violate the
     * heap order, so swim() is used to bubble it up to a position where
     * it satisfies the heap order. Running time: O(log n), because the
     * height of the heap is log n.
     *
     * @param k
     */
    public void Insert(Key k) {
        if (N >= this.items.length) {
            this.resize(this.items.length * 2);
        }
        this.itemIndex.put(k, N);
        this.items[N++] = k;
        this.swim(N);
        assert itemIndex.size() == N;
    }

    /**
     * Remove the item with highest priority from the heap and return it.
     * 
     * Retrieve the item at index 1 and swap the item at index N with the item
     * at index 1. Use sink() to restore the heap order. 
     * Running time: O(log n) because the height of the heap is log n, and this
     * is the maximum number of iterations that sink() can perform.
     *
     * @return the item with highest priority in the heap.
     */
    public Key Delete() {
        Key rv = this.items[0];
        this.Exch(1, N--);
        this.sink(1);
        this.items[N] = null;
        this.itemIndex.remove(rv);
        assert itemIndex.size() == N;
        return rv;
    }

    /**
     * Gives the item with highest priority in the heap.
     *
     * @return item with highest priority
     */
    public Key Peek() {
        return this.items[0];
    }

    /**
     * Say whether or not a given key is in the heap.
     *
     * @param k
     * @return item with highest priority
     */
    public Boolean Contains(Key k) {
        return this.itemIndex.containsKey(k);
    }
    
    /**
     * Gives the number of items in the heap
     *
     * @return
     */
    public int size() {
        return this.N;
    }

    /**
     * Remove a specific item from the heap. Running time: O(log n)
     * The itemIndex is used to enable constant time access to the item in the heap.
     * itemIndex must be updated as items are inserted into and deleted from the heap.
     *
     * @param k
     * @return
     */
    public Key DeleteSpecificKey(Key k) {
        int i = itemIndex.get(k);
        Key rv = this.items[i];
        this.Exch(i + 1, N--);
        this.sink(i + 1);
        this.items[N] = null;
        this.itemIndex.remove(k);
        assert itemIndex.size() == N;
        return rv;
    }

    /**
     * swim the item at i to its correct position in the items array.
     * 
     * While the item at index i has greater priority than its parent, swap
     * the item with its parent. 
     * 
     * @param i 
     */
    private void swim(int i) {
        while (!heapOrder(i)) {
            Exch(i, i / 2);
            i = i / 2;
        }
    }

    /**
     * 
     * sink the item at i to it's correct position in the items array.
     * 
     * While the item at index i does not have higher priority than both its
     * children, get j, the child with the highest priority, and swap the items
     * i and j. Rinse and repeat.
     * 
     * @param i 
     */
    private void sink(int i) {
        while (2 * i <= N && (!heapOrder(2 * i) || !heapOrder(2 * i + 1))) {
            int j = compare(2 * i, 2 * i + 1);
            Exch(i, j);
            i = j;
        }
    }

    /**
     * True if the item at index i satisfies the heap invariant, false otherwise.
     * 
     * The heap invariant is: The item at index i has lower priority than its 
     * parent item.
     *
     * @param i
     * @return
     */
    protected abstract boolean heapOrder(int i);

    /**
     * Compare items at index i and j of the heap, return the index with highest
     * priority
     *
     * @param i
     * @param j
     * @return
     */
    protected abstract int compare(int i, int j);

    /**
     * Swap the items at index i and index j.
     * @param i
     * @param j 
     */
    private void Exch(int i, int j) {
        Key tmp = items[i - 1];
        items[i - 1] = items[j - 1];
        itemIndex.put(items[j - 1], i - 1);
        items[j - 1] = tmp;
        itemIndex.put(tmp, j - 1);
    }

    /**
     * Return the parent of item i.
     * 
     * items are arranged so that the children of i are at 2*i and 2*i + 1.
     * The parent of i is therefore i/2.
     *
     * @param i
     * @return
     */
    protected Key parent(int i) {
        return this.items[i / 2 - 1];
    }

    private void resize(int capacity) {
        Key[] newItems = (Key[]) new Comparable[capacity];
        System.arraycopy(this.items, 0, newItems, 0, N);
        this.items = newItems;
    }

    public boolean isEmpty() {
        return this.N == 0;
    }
}
