package string;

import org.junit.Test;
import static org.junit.Assert.*;

public class RotationTest {
    
    @Test
    public void rotations() {
        String text = "hello";
        Rotation r0 = new Rotation(text, 0);
        Rotation r1 = new Rotation(text, 1);
        Rotation r3 = new Rotation(text, 3);
        
        assert r0.charAt(r0.length() - 1) == 'o';
        assert r1.charAt(r1.length() - 1) == 'l';
        assert r3.charAt(r3.length() - 1) == 'e';
        
        assert r0.toString().equals(text);
        assert r1.toString().equals("ohell");
        assert r3.toString().equals("llohe");
        
        assert r1.compareTo(r3) > 0;
        assert r0.compareTo(r1) < 0;
        
        assert r0.isRotationOf(text);
        assert r1.isRotationOf(text);
        assert r3.isRotationOf(text);
        
    }
    
}
