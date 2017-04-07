package utils;

import org.junit.Test;

public class CircularBufferTest {
    
    @Test
    public void testRW() throws IllegalAccessException {
        CircularBuffer<Integer> cb = new CircularBuffer<>(3, false);
        cb.append(4);
        cb.append(3);
        assert cb.size() == 2;
        assert cb.remove() == 4;
        assert cb.size() == 1;
        cb.append(5);
        cb.append(1);
        assert cb.size() == 3;
        cb.append(7);
        assert cb.size() == 3;
        assert cb.remove() == 5;
    }
    
    @Test(expected = IllegalAccessException.class)
    public void testOFlow() throws IllegalAccessException {
        CircularBuffer<Integer> cb = new CircularBuffer<>(3, true);
        cb.append(4);
        cb.append(3);
        cb.append(5);
        assert cb.size() == 3;
        cb.append(7);
    }
    
    @Test(expected = IllegalAccessException.class)
    public void testUFlow() throws IllegalAccessException {
        CircularBuffer<Integer> cb = new CircularBuffer<>(3, true);
        cb.append(4);
        cb.append(3);
        cb.append(5);
        assert cb.size() == 3;
        assert cb.remove() == 4;
        assert cb.remove() == 3;
        assert cb.remove() == 5;
        cb.remove(); // throws IllegalAccessException
    }
    
    @Test
    public void testIRead() throws IllegalAccessException {
        CircularBuffer<Integer> cb = new CircularBuffer<>(3, false);
        cb.append(1);
        cb.append(2);
        cb.append(3);
        assert cb.read(0) == 1;
        assert cb.read(1) == 2;
        assert cb.read(2) == 3;
        cb.append(4);
        assert cb.read(0) == 2;
        assert cb.read(1) == 3;
        assert cb.read(2) == 4;
    }
}
