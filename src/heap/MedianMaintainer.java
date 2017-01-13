package heap;

/**
 * Maintain the median of a stream of values using a MinHeap for the upper half
 * of the values and a MaxHeap for the lower half of the follows. 
 * @author colm_mchugh
 */
public class MedianMaintainer {

    private final MaxHeap<Integer> lowerHalf;
    private final MinHeap<Integer> upperHalf;
    
    public MedianMaintainer() {
        lowerHalf = new MaxHeap<>();
        upperHalf = new MinHeap<>();
    }

    public int Median() {
        return this.lowerHalf.Peek();
    }

    public void Add(int x) {
        if (this.lowerHalf.size() == 0 || x < this.lowerHalf.Peek()) {
            this.lowerHalf.Insert(x);
        } else {
            this.upperHalf.Insert(x);
        }
        this.Balance(this.Cardinality() % 2 == 0 ? 0 : 1);
    }
    
    public boolean Invariant() {
        int oddFactor = (this.Cardinality() % 2 == 0 ? 0 : 1);
        return this.lowerHalf.size() == this.upperHalf.size() + oddFactor
                    && this.upperHalf.size() == this.Cardinality() / 2;
    }

    public int Cardinality() {
        return this.lowerHalf.size() + this.upperHalf.size();
    }
    
    private void Balance(int oddFactor) {
        int lsize = this.lowerHalf.size();
        int usize = this.upperHalf.size();
        while (lsize != usize + oddFactor) {
            if (lsize > usize + oddFactor) {
                this.swapOut(lowerHalf, upperHalf);
            } else {
                this.swapOut(upperHalf, lowerHalf);
            }
            lsize = this.lowerHalf.size();
            usize = this.upperHalf.size();
        }
    }

    private void swapOut(Heap<Integer> from, Heap<Integer> to) {
        Integer n = from.Delete();
        to.Insert(n);
    }
    
}
