import java.math.BigInteger;
import java.util.Random;

public class HashFunction {
    private final BigInteger n;
    private final BigInteger h0;

    public HashFunction(String nn) {
        Random random = new Random();
        n = new BigInteger(nn);
        h0 = BigInteger.probablePrime(n.bitLength() / 2, random);
    }

    public String getHash(String m) {
        BigInteger[] h = new BigInteger[m.length() + 1];
        h[0] = h0;
        for (int i = 0; i < m.length(); i++) {
            h[i + 1] = h[i].add(BigInteger.valueOf(m.charAt(i))).modPow(BigInteger.TWO, n);
        }
        return h[m.length()].toString(16);
    }
}
