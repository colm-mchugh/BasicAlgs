package sort;

import java.util.HashSet;
import java.util.Set;

import string.Transform;

public class EditDistanceFilter {
	
	public static Set<String> Of(Set<String> input, double similarity_tolerance)
	{
		Set<String> filteredSet = new HashSet<String>();
		while (!input.isEmpty()) {
			String s = input.iterator().next();
			input.remove(s);
			Set<String> similarToS = new HashSet<String>();
			for (String t : input) {
				double dist_percent = (new Transform(s, t)).similarity();
				if (dist_percent < similarity_tolerance)
					similarToS.add(t);
			}
			input.removeAll(similarToS);
		}
		return filteredSet;
	}
}
