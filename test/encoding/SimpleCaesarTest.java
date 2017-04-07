package encoding;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCaesarTest {
    
    
    @Test
    public void testEncode() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SimpleCaesar[] caesars = { new SimpleCaesar(alphabet.toCharArray(), 0),
                                new SimpleCaesar(alphabet.toCharArray(), 2),
                                new SimpleCaesar(alphabet.toCharArray(), 7),
                                new SimpleCaesar(alphabet.toCharArray(), alphabet.length() * 3 + 17)};
        long[] datums = { 0, 12345, 583493, 938459025937948375l};
        validateEncodeDecode(caesars[0], datums[0]);
        validateEncodeDecode(caesars[0], datums[1]);
        validateEncodeDecode(caesars[0], datums[2]);
        validateEncodeDecode(caesars[0], datums[3]);
        
        validateEncodeDecode(caesars[1], datums[0]);
        validateEncodeDecode(caesars[1], datums[1]);
        validateEncodeDecode(caesars[1], datums[2]);
        validateEncodeDecode(caesars[1], datums[3]);
        
        validateEncodeDecode(caesars[2], datums[0]);
        validateEncodeDecode(caesars[2], datums[1]);
        validateEncodeDecode(caesars[2], datums[2]);
        validateEncodeDecode(caesars[2], datums[3]);
        
        validateEncodeDecode(caesars[3], datums[0]);
        validateEncodeDecode(caesars[3], datums[1]);
        validateEncodeDecode(caesars[3], datums[2]);
        validateEncodeDecode(caesars[3], datums[3]);
    }

    
    private void validateEncodeDecode(SimpleCaesar sc, long data) {
        assert data == sc.decode(sc.encode(data));
    }
    
}
