package encoding;

import java.util.HashMap;
import java.util.Map;

/**
 * Given an alphabet and a shift factor, a SimpleCaesar instance will encode a
 * long to a string, and decode that string to the original long.
 * - Based somewhat on Caesar cipher (https://en.wikipedia.org/wiki/Caesar_cipher)
 * - The alphabet can be anything the user wants it to be. Points to note are:
 *      - The length of the alphabet provides the numerical base for encoding and decoding.
 *      - No assumptions are made about the relationship of the alphabet characters
 *        to each other. A map is used for character offsets.
 */
public class SimpleCaesar {

    private final char[] alphabet;
    private final int shift;
    private final Map<Character, Integer> index;
    
    public SimpleCaesar(char[] alphabet, int shift) {
        this.alphabet = alphabet;
        this.shift = shift;
        this.index = new HashMap<>(alphabet.length);
        for (int i = 0; i < alphabet.length; i++) {
            this.index.put(alphabet[i], i);
        }
    }

    /**
     * Encode the given datum to an encoding in the alphabet.
     * 
     * @param datum
     * @return 
     */
    public String encode(long datum) {
        StringBuilder sb = new StringBuilder();
        int N = alphabet.length;
        while (datum != 0) {
            long i = ((datum % N) + shift) % N;
            sb.append(alphabet[(int)i]);
            datum = datum / N;
        }
        return sb.reverse().toString();
    }
    
    /**
     * Decode the given string from an encoding in the alphabet to 
     * the data it represents.
     * 
     * @param encoding
     * @return 
     */
    public long decode(String encoding) {
        long datum = 0;
        int N = alphabet.length;
        for (int i = 0; i < encoding.length(); i++) {
            int offset = (index.get(encoding.charAt(i)) - shift);
            if (offset < 0) {
                offset += N;
            }
            datum = offset % N + datum * N;
        }
        return datum;
    }
}
