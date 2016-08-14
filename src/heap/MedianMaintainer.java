package heap;


import java.io.BufferedReader;
import java.io.FileReader;


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
    
    public static void main(String[] args) {
        String relPath = "resources/Median.txt";
        FileReader fr;
        try {
            fr = new FileReader( relPath );
            BufferedReader br = new BufferedReader( fr );
            String line;
            MedianMaintainer medianMaintainer = new MedianMaintainer();
            int medianSum = 0;
            while( ( line = br.readLine() ) != null ) {
                String[] split = line.trim().split( "(\\s)+" );
                Integer n = Integer.parseInt(split[0]);
                medianMaintainer.Add(n);
                assert medianMaintainer.Invariant();
                if (!medianMaintainer.Invariant()) {
                    throw new IllegalStateException("Invariant Violation");
                }
                medianSum += medianMaintainer.Median();
            }
            System.out.println(medianSum % 10000);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
    
}
