package utils;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class RandGen {
    private static Random random;    // pseudo-random number generator
    private static long seed;        // pseudo-random number generator seed

    // static initializer
    static {
        // this is how the seed was set in Java 1.4
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    /**
     * Sets the seed of the pseudorandom number generator.
     * This method enables you to produce the same sequence of "random"
     * number for each execution of the program.
     * Ordinarily, you should call this method at most once per program.
     *
     * @param s the seed
     */
    public static void setSeed(long s) {
        seed   = s;
        random = new Random(seed);
    }

    /**
     * Returns the seed of the pseudorandom number generator.
     *
     * @return the seed
     */
    public static long getSeed() {
        return seed;
    }

    /**
     * Returns a random real number uniformly in [0, 1).
     *
     * @return a random real number uniformly in [0, 1)
     */
    public static double uniform() {
        return random.nextDouble();
    }

    /**
     * Returns a random integer uniformly in [0, n).
     * 
     * @param n number of possible integers
     * @return a random integer uniformly between 0 (inclusive) and <tt>N</tt> (exclusive)
     * @throws IllegalArgumentException if <tt>n <= 0</tt>
     */
    public static int uniform(int n) {
        if (n <= 0) throw new IllegalArgumentException("Parameter N must be positive");
        return random.nextInt(n);
    }
    
    /**
     * Returns a random integer uniformly in [lo, hi].
     * 
     * @param lo the lower bound of the range
     * @param hi the upper bound of the range
     * @return a random integer uniformly between lo (inclusive) and hi (inclusive)
     * @throws IllegalArgumentException if <tt>lo <= 0  or hi <= 0 or hi <= lo </tt>
     */
    public static int uniform(int lo, int hi) {
        if ((lo <= 0) || (hi <= 0)) throw new IllegalArgumentException("Parameter must be positive");
        if (hi < lo) throw new IllegalArgumentException("Range must be >= 1");
        return random.nextInt((hi - lo) + 1) + lo;
    }
    
    /**
     * Returns a random boolean uniformly in [true, false]
     * 
     * @return a random boolean uniformly either true or false
     */
    public static boolean uniformBool() {
        int v = random.nextInt(2);
        // v is either 0 or 1
        return v != 0;
    }
    
    /**
     * Return a random sample of M numbers with no duplicates 
     * chosen from 1..N.
     * 
     * Robert Floyd's algorithm, runs in O(M).
     * https://dl.acm.org/citation.cfm?id=315746&dl=ACM&coll=DL
     * 
     * @param N
     * @param M
     * @return 
     */
    public static Set<Integer> uniformSample(int N, int M) {
        Set<Integer> sample = new HashSet<>(M);
        assert N >= M;
        for (int j = N - M + 1; j <= N; j++) {
            int s = uniform(1, j);
            if (!sample.contains(s)) {
                sample.add(s);
            } else {
                sample.add(j);
            }
        }
        return sample;
    }
    
    // don't instantiate
    private RandGen() { }
}
