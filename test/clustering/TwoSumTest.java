/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;


public class TwoSumTest {
    
    @Test
    public void testTwoSum() {
        Long[] numbers = this.readNumbers("resources/2sum.txt");
        TwoSum twoSummer = new TwoSum(numbers);
        int t = 0;
        int lb = -10000, ub = 10000;
        int numTwoSums = 0;
        for (int target = lb; target <= ub; target++) {
            if (twoSummer.hasTwoSumFor(target)) {
                numTwoSums++;
            }
        }
        assert numTwoSums == 427;
    }
    
    private Long[] readNumbers(String path) {
        Long[] numbers = new Long[1000000];
        try {
            BufferedReader br = new BufferedReader( new FileReader( path ));
            String line;
            for (int i = 0; ( line = br.readLine() ) != null; i++ ) {
                numbers[i] = (Long.parseLong(line.trim()));
            }
        } catch ( IOException | NumberFormatException e ) {
            e.printStackTrace();
        }
        return numbers;
    }
}
