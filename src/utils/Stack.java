package utils;

public class Stack<T> {

    T[] items;
    int i = 0;
    int numResizes = 0;

    public Stack() {
        items = (T[]) new Object[8];
        i = 0;
    }

    public void Push(T data) {
        if (i == items.length) {
            T[] newItems = (T[]) new Comparable[(items.length * 2)];
            System.arraycopy(this.items, 0, newItems, 0, i);
            this.items = newItems;
            this.numResizes++;
        }
        items[i++] = data;
    }

    public T Pop() {
        T data = items[--i];
        return data;
    }
    
    public T Top() {
        return items[i-1];
    }

}
