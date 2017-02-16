package string;

/**
 * A rotation of a string S by N is a rotation of S such that its characters are shifted
 * right by N places, and its first N characters are the last N characters of S 
 * Rotate("hello", 0) = "hello" 
 * Rotate("hello", 1) = "ohell" 
 * Rotate("hello", 3) = "llohe"
 *
 */
public class Rotation implements Comparable<Rotation> {

    String text;
    int index;

    public Rotation(String text, int index) {
        this.text = text;
        this.index = index;
    }

    Character charAt(int i) {
        return text.charAt((i + text.length() - index) % text.length());
    }

    @Override
    public int compareTo(Rotation o) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            sb.append(this.charAt(i));
        }
        return sb.toString();
        // could also be implemented as : text.substring(index) + text.substring(0, index);
    }

    public boolean isRotationOf(String s) {
        if (s.length() != text.length()) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (this.charAt(i) != s.charAt((i + text.length() - index) % text.length())) {
                return false;
            } 
        }
        return true;
    }
    
    public int length() {
        return text.length();
    }

}
