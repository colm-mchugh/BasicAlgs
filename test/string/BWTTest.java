package string;

import java.util.Arrays;
import java.util.Comparator;
import org.junit.Test;

public class BWTTest {

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

    @Test
    public void testBWT() {
        String bwt = BWT.transform("panamabananas$");
        assert bwt.equals("smnpbnnaaaaa$a");
    }

    @Test
    public void testReverseBWT() {
        String banana = BWT.reverse("annb$aa");
        assert banana.equals("banana$");
        assert BWT.reverse(BWT.transform("panamabananas$")).equals("panamabananas$");
        assert BWT.reverse("AC$A").equals("ACA$");
        assert BWT.reverse("AGGGAA$").equals("GAGAGA$");
    }

    @Test
    public void testMatch() {
        String bananaBwt = BWT.transform("banana$");
        int f = BWT.match(bananaBwt, "ana");
        assert f == 2;

        assert BWT.match("AGGGAA$", "GA") == 3;
        assert BWT.match("ATT$AA", "ATA") == 2;
        assert BWT.match("ATT$AA", "A") == 3;
        assert BWT.match("AT$TCTATG", "TCT") == 0;
        assert BWT.match("AT$TCTATG", "TATG") == 0;
    }

    @Test
    public void testTextSort() {
        String bwt = "annb$aa";
        Integer[] s_bwt = new Integer[bwt.length()];
        for (int i = 0; i < s_bwt.length; i++) {
            s_bwt[i] = i;
        }
        Arrays.sort(s_bwt, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                char c1 = bwt.charAt(o1);
                char c2 = bwt.charAt(o2);
                if (c1 < c2) {
                    return -1;
                };
                return (c1 == c2 ? 0 : 1);
            }
        });
        int[] expected = {4, 0, 5, 6, 3, 1, 2};
        for (int i = 0; i < s_bwt.length; i++) {
            assert expected[i] == s_bwt[i];
        }

    }

}
