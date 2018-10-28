package encoding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class LZWTest {

    @Test
    public void testEncode1() {
        String[] inputs = {"TATAGATCTTAATATA", "ABRACADABRABRABRA", "ABABABA",
            "TATATAT", "BABAABAAA", "AABABACBAACBAADAAA"};
        char[][] alphabets = {
            {'A', 'C', 'G', 'T'},
            {'A', 'B', 'C', 'D', 'R'},
            {'A', 'B'},
            {'A', 'T'},
            {'A', 'B'},
            {'A', 'B', 'C', 'D'}
        };
        int[][] expectedEncodings = {
            {4, 1, 5, 3, 6, 2, 4, 5, 6, 13},
            {1, 2, 5, 1, 3, 1, 4, 6, 8, 7, 13, 1},
            {1, 2, 3, 5},
            {2, 1, 3, 5},
            {2, 1, 3, 4, 1, 7},
            {1, 1, 2, 6, 1, 3, 7, 9, 11, 4, 5, 1}
        };

        for (int i = 0; i < inputs.length; i++) {
            LZWEncoder encoder = new LZWEncoder(alphabets[i]);
            List<Integer> result = encoder.encode(inputs[i]);
            assert result.size() == expectedEncodings[i].length;
            for (int j = 0; j < expectedEncodings[i].length; j++) {
                assert expectedEncodings[i][j] == result.get(j);
            }
        }

        for (int i = 0; i < inputs.length; i++) {
            LZWDecoder decoder = new LZWDecoder(alphabets[i]);
            String decoding = decoder.decode(toList(expectedEncodings[i]));
            assert decoding.equals(inputs[i]);
        }

    }

    private List<Integer> toList(int[] expectedEncoding) {
        List<Integer> l = new ArrayList<>(expectedEncoding.length);
        for (int i : expectedEncoding) {
            l.add(i);
        }
        return l;
    }

    @Test
    public void encodeUlysses() {
        StringBuilder sb = new StringBuilder();
        Set<Character> chars = new HashSet<>();
        try {
            FileReader fr = new FileReader("resources/ulyss10.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            for (int j = 0; (line = br.readLine()) != null; j++) {
                sb.append(line);
                for (int i = 0; i < line.length(); i++) {
                    chars.add(line.charAt(i));
                }
            }
        } catch (IOException | NumberFormatException e) {
        }

        char[] alphabet = new char[chars.size()];
        int i = 0;
        for (char c : chars) {
            alphabet[i++] = c;
        }
        String text = sb.toString();

        int text_sz = text.length();

        LZWEncoder encoder = new LZWEncoder(alphabet);
        List<Integer> encoding = encoder.encode(text);

        int encoding_sz = encoding.size();

        System.out.println("Encoding ratio: " + (double) (encoding_sz * 2) / (text_sz));
        LZWDecoder decoder = new LZWDecoder(alphabet);
        String ulysses = decoder.decode(encoding);

        assert ulysses.equals(text);
    }

}
