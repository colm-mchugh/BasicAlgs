package utils;

import java.util.ArrayList;
import java.util.List;

public class CircularBuffer<T> {  
    private final int N;
    private final List<T> crclrBffr;
    private final boolean  noOverflow;
    private int r;
    private int w;
    private int nelems;

    public final static boolean NO_OVERFLOW = true;
    /**
     * Create a circular buffer that can hold N elements.
     * If noOverflow, appending will result in an exception
     * when there are N elements in the buffer. Otherwise,
     * appending will overwrite existing elements, and the
     * size will remain N.
     * 
     * @param N
     * @param noOverflow 
     */
    public CircularBuffer(int N, boolean noOverflow) {
        this.N = N;
        this.crclrBffr = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            crclrBffr.add(null);
        }
        this.r = -1;
        this.w = 0;
        this.nelems = 0;
        this.noOverflow = noOverflow;
    }
    
    /**
     * Append the given data to the buffer. 
     * 
     * If no overflow is in effect, throw an exception if the buffer is full.
     * Otherwise, increment the read index, effectively overwriting the current 
     * read item.
     * 
     * Once data is written, increment the write index and the number of elements.
     * 
     * @param data
     * @throws IllegalAccessException 
     */
    public void append(T data) throws IllegalAccessException {
        if (w == r) {
            if (noOverflow) {
                throw new IllegalAccessException("Write on full buffer");
            }
            r = (r + 1) % N;
        }
        if (r == -1) {
            r = w;
        }
        this.crclrBffr.set(w, data);
        w = (w + 1) % N;
        nelems = (nelems == N? nelems : nelems + 1);
    }
    
    /**
     * Return the item at the read index, increment the read index, and 
     * decrement element count. Throw exception if buffer is empty.
     * 
     * @return
     * @throws IllegalAccessException 
     */
    public T remove() throws IllegalAccessException {
        if (r == -1) {
            throw new IllegalAccessException("Read on empty buffer");
        }
        T data = this.crclrBffr.get(r);
        r = (r + 1) % N;
        nelems--;
        if (r == w) {
            r = -1;
        }
        return data;
    }
    
    /**
     * Return the item at the given index i. 
     * Exception thrown if buffer is empty, or i does not satisfy 
     * 0 <= i < size().
     * 
     * @param i
     * @return
     * @throws IllegalAccessException 
     */
    public T read(int i) throws IllegalAccessException {
        if (r == -1) {
            throw new IllegalAccessException("Read on empty buffer");
        }
        if (i >= nelems) {
            throw new IllegalAccessException("Not enough elements in buffer");
        }
        T data = this.crclrBffr.get((r + i)%N);
        return data;
    }
    
    /**
     * Overwrite an existing entry; put the given data in the buffer at the 
     * given index. Throw exception if index exceeds buffer size.
     * 
     * @param i where i < size()
     * @throws IllegalAccessException 
     */
    public void write(T data, int i) throws IllegalAccessException {
        if (i >= nelems) {
            throw new IllegalAccessException("Not enough elements in buffer");
        }
        this.crclrBffr.set((r + i)%N, data);
    }
    
    
    public int size() {
        return nelems;
    }
}
