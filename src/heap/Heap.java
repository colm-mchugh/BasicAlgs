package heap;

import java.util.Arrays;
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
     * The items are maintained in an array, where N is the number of items. All
     * heap operations must maintain the following invariants: 1) items[0] is
     * the highest priority item in the heap 2) for all items i in the heap,
     * priority of items[i/2 - 1] has higher priority than items[i] 3) a map of
     * item to item index is maintained to enable O(1) access of any item.
     * Operations that mutate the heap (Insert, Delete, DeleteSpecificKey) check
     * that the following invariant holds on exit: #items in heap == #items in
     * item index
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
     * The item is placed in the next available slot. It may violate the heap
     * order, so swim() is used to bubble it up to a position where it satisfies
     * the heap order. Running time: O(log n), because the height of the heap is
     * log n.
     *
     * @param k
     */
    public void Insert(Key k) {
        if (N >= this.items.length) {
            this.resize(this.items.length * 2);
        }
        this.items[N++] = k;
        this.itemIndex.put(k, N);
        this.swim(N);
        assert itemIndex.size() == N;
    }

    /**
     * Remove the item with highest priority from the heap and return it.
     *
     * Retrieve the item at index 1 and swap the item at index N with the item
     * at index 1. Use sink() to restore the heap order. Running time: O(log n)
     * because the height of the heap is log n, and this is the maximum number
     * of iterations that sink() can perform.
     *
     * @return the item with highest priority in the heap.
     */
    public Key Delete() {
        Key rv = value(1);
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
        return value(1);
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
     * Remove a specific item from the heap. Running time: O(log n) The
     * itemIndex is used to enable constant time access to the item in the heap.
     * itemIndex must be updated as items are inserted into and deleted from the
     * heap.
     *
     * @param k
     * @return
     */
    public Key DeleteSpecificKey(Key k) {
        assert itemIndex.size() == N;
        int i = itemIndex.get(k);
        Key rv = value(i);
        if (i == N) {
            N--;
            this.itemIndex.remove(k);
            this.items[N] = null;
            assert itemIndex.size() == N;
            return rv;
        }
        this.Exch(i, N--);
        this.sink(i);
        if (i > 1 && i <= N && outOfOrder(p(i), i)) {
            this.swim(i);
        }
        this.items[N] = null;
        this.itemIndex.remove(k);
        assert itemIndex.size() == N;
        return rv;
    }

    
    protected abstract boolean heapOrder(int i, int j);

    protected abstract boolean outOfOrder(int i, int j);
    
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
    protected abstract void sink(int i);
    
    /**
     * swim the item at i to its correct position in the items array.
     *
     * While the item at index i has greater priority than its parent, swap the
     * item with its parent.
     *
     * @param i
     */
    protected abstract void swim(int i);
    
    /**
     * Swap the items at index i and index j.
     *
     * @param i
     * @param j
     */
    protected void Exch(int i, int j) {
        if (i == j) {
            return;
        }
        Key tmp = value(i);
        items[i - 1] = items[j - 1];
        itemIndex.put(items[j - 1], i);
        items[j - 1] = tmp;
        itemIndex.put(tmp, j);
    }

    private void resize(int capacity) {
        Key[] newItems = (Key[]) new Comparable[capacity];
        System.arraycopy(this.items, 0, newItems, 0, N);
        this.items = newItems;
    }

    public boolean isEmpty() {
        return this.N == 0;
    }

    /**
     * Sort the given array in-place using sink and swim
     *
     * @param items
     */
    public void Sort(Key[] items) {
        this.items = items;
        this.N = items.length;
        // convert items to heap order by sifting up each element 
        for (int n = 2; n <= N; n++) {
            swim(n);
        }

        // items is now in heap order:
        assert (inHeapOrder());

        for (int i = N - 1; i >= 1; i--) {
            // Swap the biggest element in the heap with the first element
            // in the sorted array
            Key t = items[0];
            items[0] = items[i];
            items[i] = t;
            N--; // shrink the heap by 1 element
            
            // Restore the heap invariant by sifting first item to its correct place:
            this.sink(1);  
            
            // Check invariants:
            // (1) items[0:N] is in heap order:
            for (int h = N; h > 1; h--) {
                if (value(p(h)).compareTo(value(h)) < 0) {
                    System.out.println("Heap Order Check failed for array: " + Arrays.toString(items));
                    System.out.println("items[" + h / 2 + "] (" + items[h / 2] + ") < items[" + h + "] (" + items[h] + ")");
                }
                assert (value(p(h)).compareTo(value(h)) >= 0);
            }
            // (2) items[N+1:] is sorted:
            for (int s = i; s < items.length - 1; s++) {
                assert (items[s].compareTo(items[s + 1]) <= 0);
            }
        }
    }

    protected Key value(int i) {
        if (i <= N) {
            return items[i - 1];
        } else {
            assert false; // should never occur, is a bug if it does.
        }
        return null; 
    }

    /**
     * Return left child index of index i
     * @param i
     * @return 
     */
    protected int lchild(int i) {
        return 2 * i;
    }

    /**
     * Return right child index of index i
     * @param i
     * @return 
     */
    protected int rchild(int i) {
        return 2 * i + 1;
    }

    /**
     * Return parent index (p) of index i
     * @param i
     * @return 
     */
    protected int p(int i) {
        return i / 2;
    }

    /**
     * Check that the entire heap is maintaining all heap invariants.
     * 
     * @return true if all heap invariants hold. Otherwise, an assert fail happens.
     */
    public boolean inHeapOrder() {
       
       for (int i = 2; i <= N; i++) {
            if ((lchild(i)) <= N && !(heapOrder(i, lchild(i)) || value(i).equals(value(lchild(i))))) {
                System.out.println("Heap Order Violation: items[" + i + "] (" + value(i) + ") < items[" + lchild(i) + "] (" + value(lchild(i)) + ")");
                System.out.println("Heap Order Check failed for array: " + Arrays.toString(items));
            }
            if (rchild(i) <= N && !(heapOrder(i, rchild(i)) || value(i).equals(value(rchild(i))))) {
                System.out.println("Heap Order Violation: items[" + i + "] (" + value(i) + ") < items[" + rchild(i) + "] (" + value(rchild(i)) + ")");
                System.out.println("Heap Order Check failed for array: " + Arrays.toString(items));
            }
            if ((lchild(i)) <= N) 
                assert(heapOrder(i, lchild(i)) || value(i).equals(value(lchild(i))));
            if ((rchild(i)) <= N)
                assert(heapOrder(i, rchild(i)) || value(i).equals(value(rchild(i))));
        }

        for (int h = N; h > 1; h--) {
            if (!heapOrder(p(h), h)) {
                System.out.println("Heap Order Violation: items[" + p(h) + "] (" + value(p(h)) + ") < items[" + h + "] (" + value(h) + ")");
                System.out.println("Heap Order Check failed for array: " + Arrays.toString(items));
            }
            assert (heapOrder(p(h), h));
        }
        
        return true; 
    }
}
