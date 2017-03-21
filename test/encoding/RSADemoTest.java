package encoding;

import org.junit.Test;
import utils.RandGen;

public class RSADemoTest {
    
    @Test
    public void testMakePPKPair() {
        System.out.println("makePPKPair");
        int X = RandGen.uniform(101);
        int Y = RandGen.uniform(345);
        RSADemo instance = new RSADemo();
        RSADemo.PPKPair result = instance.makePPKPair(X, Y);
        
        int datum = 77;
        assert instance.decode(result, instance.encode(result, datum)) == datum;
        
    }
    
}
