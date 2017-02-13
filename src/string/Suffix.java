package string;

public class Suffix implements Comparable<Suffix> {
    public int index;
    public int length;
    private String text;

    public Suffix(int index, int length, String text) {
        this.index = index;
        this.length = length;
        this.text = text;
    }

    public Suffix(int index, String text) {
        this.index = index;
        this.text = text;
        this.length = Integer.MAX_VALUE;
    }
    
    @Override
    public int compareTo(Suffix o) {
        if (this == o) {
            return 0;
        }
        int len = Integer.min(o.length(), this.length());
        for (int i = 0; i < len; i++) {
            if (this.charAt(i) < o.charAt(i)) {
                return -1;
            }
            if (this.charAt(i) > o.charAt(i)) {
                return 1;
            }
        }
        return this.length() - o.length();
    }
    
    public String text() {
        return text;
    }
    
    public Character charAt(int i) {
        return text.charAt(index + i);
    }

    @Override
    public String toString() {
        if (this.length == Integer.MAX_VALUE) {
            return text.substring(index);
        }
        return text.substring(index, index + length);
    }
    
    public int length() {
        if (this.length == Integer.MAX_VALUE) {
            return text.length() - index;
        }
        return length;
    }
}
