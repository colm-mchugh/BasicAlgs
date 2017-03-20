package encoding;

/**
 * Demonstration of RSA public/private key generation.
 * 
 * Input: large positive integers X, Y
 * 
 * 1) p = first prime number less than X
 * 2) q = first prime number less than Y
 * 3) N = p * q
 * 4) tN = N - (p + q) - 1
 * 5) e = any number < tN with property: gcd(tN, e) = 1
 * 6) d = e^-1 % tN
 * 
 * To encrypt data P: encrypt(P) = P^e % N
 * To decrypt data P: decrypt(P) = P^d % N
 * 
 */
public class RSADemo {
    // TODO
}
