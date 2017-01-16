/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;


public class TwoSumTest {
    
    @Test
    public void testTwoSum() {
        Long[] numbers = this.readNumbers("resources/2sum.txt");
        long x = TwoSum.twoSum(numbers, -10000l, 10000l);
        assert x == 427;
    }
    
    private Long[] readNumbers(String path) {
        Long[] numbers = new Long[1000000];
        try {
            BufferedReader br = new BufferedReader( new FileReader( path ));
            String line;
            for (int i = 0; ( line = br.readLine() ) != null; i++ ) {
                numbers[i] = (Long.parseLong(line.trim()));
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return numbers;
    }
}
