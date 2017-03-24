package encoding;

import utils.math;

/**
 * Demonstration of RSA public/private key generation.
 *
 * Input: large positive integers X, Y
 *
 * 1) p = first prime number greater than X 
 * 2) q = first prime number greater than Y 
 * 3) N = p * q 
 * 4) tN = N - (p + q) - 1 
 * 5) e = any number < tN with property: gcd(tN, e) = 1 6) d = e^-1 % tN
 *
 * To encrypt data P: encrypt(P) = P^e % N 
 * To decrypt data P: decrypt(P) = P^d % N
 *
 */
public class RSADemo {

    public PPKPair makePPKPair(int X, int Y) {
        int p = nextPrime(X);
        int q = nextPrime(Y);
        int N = p * q;
        int tN = N - (p + q) - 1;
        int e = prevRelativePrime(tN);
        int d = modInverse(e, N);
        return new PPKPair(N, e, d);
    }

    private int nextPrime(int n) {
        while (!math.isPrime(n)) {
            n++;
        }
        return n;
    }

    private int prevRelativePrime(int n) {
        int rP = n - 1;
        while (rP > 0 && math.gcd(n, rP) != 1) {
            rP--;
        }
        return rP;
    }

    public int encode(PPKPair k, int datum) {
        // TODO: use modulo congruence
        return (int) (Math.pow(datum, k.e) % k.N);
    }

    public int decode(PPKPair k, int datum) {
        // TODO: use modulo congruence
        return (int) (Math.pow(datum, k.d) % k.N);
    }

    private int modInverse(int a, int m) {
        int m0 = m, t, q;
        int x0 = 0, x1 = 1;
        if (m == 1) {
            return 0;
        }
        while (a > 1) {
            // q is quotient
            q = a / m;
            t = m;
            m = a % m;
            a = t;
            t = x0;
            x0 = x1 - q * x0;
            x1 = t;
        }
        // Make x1 positive
        if (x1 < 0) {
            x1 += m0;
        }
        return x1;
    }

    public static class PPKPair {

        public int N;
        public int e;
        public int d;

        public PPKPair() {
        }

        private PPKPair(int N, int e, int d) {
            this.N = N;
            this.e = e;
            this.d = d;
        }
    }
}
