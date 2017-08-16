package utils;

import org.junit.Test;

public class CircularBufferTest {
    
    @Test
    public void testRW() throws IllegalAccessException {
        CircularBuffer<Integer> cb = new CircularBuffer<>(3, !CircularBuffer.NO_OVERFLOW);
        cb.enqueue(4);
        cb.enqueue(3);
        assert cb.size() == 2;
        assert cb.dequeue() == 4;
        assert cb.size() == 1;
        cb.enqueue(5);
        cb.enqueue(1);
        assert cb.size() == 3;
        cb.enqueue(7);
        assert cb.size() == 3;
        assert cb.dequeue() == 5;
    }
    
    @Test(expected = IllegalAccessException.class)
    public void testOFlow() throws IllegalAccessException {
        CircularBuffer<Integer> cb = new CircularBuffer<>(3, CircularBuffer.NO_OVERFLOW);
        cb.enqueue(4);
        cb.enqueue(3);
        cb.enqueue(5);
        assert cb.size() == 3;
        cb.enqueue(7);
    }
    
    @Test(expected = IllegalAccessException.class)
    public void testUFlow() throws IllegalAccessException {
        CircularBuffer<Integer> cb = new CircularBuffer<>(3, CircularBuffer.NO_OVERFLOW);
        cb.enqueue(4);
        cb.enqueue(3);
        cb.enqueue(5);
        assert cb.size() == 3;
        Integer head = cb.dequeue();
        assert head == 4;
        head = cb.dequeue();
        assert head == 3;
        head = cb.dequeue();
        assert head == 5;
        cb.dequeue(); // throws IllegalAccessException
    }
    
    @Test
    public void testIRead() throws IllegalAccessException {
        CircularBuffer<Integer> cb = new CircularBuffer<>(3, !CircularBuffer.NO_OVERFLOW);
        cb.enqueue(1);
        cb.enqueue(2);
        cb.enqueue(3);
        assert cb.read(0) == 1;
        assert cb.read(1) == 2;
        assert cb.read(2) == 3;
        cb.enqueue(4);
        assert cb.read(0) == 2;
        assert cb.read(1) == 3;
        assert cb.read(2) == 4;
    }
}
