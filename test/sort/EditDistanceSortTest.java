package sort;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class EditDistanceSortTest {

	@Test
	public void test() {
		Set<String> input = makeSet("/home/cmchugh/scontrol_files/small.sql", 53238);
		Set<String> output = EditDistanceFilter.Of(input, 0.8);
		writeSet(output, "/home/cmchugh/scontrol_files/small_editDistance80pc.sql");
	}

	private Set<String> makeSet(String filename, int sz) {
		Set<String> strings = new HashSet<>(sz);
		FileReader fr;
        try {
            fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
            	strings.add(line.trim());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return strings;
	}
	
	private void writeSet(Set<String> output, String filename) {
		FileWriter fw;
        try {
            fw = new FileWriter(filename);
            BufferedWriter br = new BufferedWriter(fw);
            for (String s : output) {
            	br.write(s);
            	br.newLine();
            }
            br.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

	}
	
}
