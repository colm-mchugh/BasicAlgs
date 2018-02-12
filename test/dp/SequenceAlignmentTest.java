package dp;

import junit.framework.TestCase;
import org.junit.Test;

public class SequenceAlignmentTest extends TestCase {

    @Test
    public void testAlign() {
        System.out.println("align");
        String X = "GCATGCU";
        String Y = "GATTACA";
        //X = "GAATTCAGTTA";
        //Y = "GGATCGA";
        SequenceAlignment instance = new SequenceAlignment();
        SequenceAlignment.Alignment result = instance.align(X, Y);
        System.out.println(result.alignX);
        System.out.println(result.alignY);
        System.out.println("Edit-distance: " + result.edit_distance);
        System.out.println("Needlman_Wunsch: " + result.needleman_wunsch_score);
    }

    
}
