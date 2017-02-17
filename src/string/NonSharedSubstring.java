
package string;
import java.io.*;
import java.math.*;
import java.util.*;

public class NonSharedSubstring implements Runnable {
	String solve (String p, String q) {
		SuffixTree sfxTree = new SuffixTree();
                String result = sfxTree.shortestNoncommonSubstring(p, q);
                return result;
	}

	public void run () {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
			String p = in.readLine ();
			String q = in.readLine ();

			String ans = solve (p, q);

			System.out.println (ans);
		}
		catch (Throwable e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}

	public static void main (String [] args) {
		new Thread (new NonSharedSubstring ()).start ();
	}
}
