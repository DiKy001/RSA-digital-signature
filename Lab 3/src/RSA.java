import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger e;
    private BigInteger d;
    private final Random random = new Random();
    private final int length;

    public String getN() { return n.toString();}

    private void generateKey() {
        p = BigInteger.probablePrime(length, random);
        BigInteger q1 = BigInteger.probablePrime(length, random);
        while (p.equals(q1)) {
            q1 = BigInteger.probablePrime(length, random);
        }
        q = q1;
        n = p.multiply(q);
        BigInteger fi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = fi.subtract(BigInteger.ONE);
        d = getSecretNum(fi);
    }

    private BigInteger getSecretNum(BigInteger fi) {
        BigInteger d0 = fi;
        BigInteger d1 = e;
        BigInteger x0 = BigInteger.ONE;
        BigInteger x1 = BigInteger.ZERO;
        BigInteger y0 = BigInteger.ZERO;
        BigInteger y1 = BigInteger.ONE;
        while (d1.compareTo(BigInteger.ONE) > 0) {
            BigInteger q0 = d0.divide(d1);
            BigInteger d2 = d0.mod(d1);
            BigInteger x2 = x0.subtract(q0.multiply(x1));
            BigInteger y2 = y0.subtract(q0.multiply(y1));
            d0 = d1;
            d1 = d2;
            x0 = x1;
            x1 = x2;
            y0 = y1;
            y1 = y2;
        }
        return y1;
    }

    public RSA() {
        length = 256;
        generateKey();
    }

    public RSA(int l) {
        length = l;
        generateKey();
    }

    public String encrypt(String m, String ee, String nn) {
        byte[] m0 = m.getBytes(StandardCharsets.UTF_8);
        StringBuilder encrypted = new StringBuilder();
        BigInteger e0 = new BigInteger(ee,16);
        BigInteger n0 = new BigInteger(nn,16);
        for (byte el : m0) {
            String ch = BigInteger.valueOf(el).modPow(e0, n0).toString(16);
            while (ch.length() < length / 2) { ch = "0".concat(ch); }
            encrypted.append(ch);
        }
        return encrypted.toString();
    }

    public String encryptHash(String m) {
        BigInteger m0 = new BigInteger(m,16);
        return m0.modPow(d, n).toString(16);
    }

    public String encryptPub(String m) {
        return encrypt(m, e.toString(), n.toString());
    }

    public String decrypt(String c) {
        String c0 = c;
        StringBuilder m = new StringBuilder();
        int halfLength = length / 2;
        for (int i = 0; i < c.length() / halfLength; i++) {
            m.append((char)(new BigInteger(c0.substring(0, halfLength), 16)).modPow(d, n).intValue());
            c0 = c0.substring(halfLength);
        }
        return m.toString();
    }

    public String decryptHash(String c, String ee, String nn) {
        BigInteger e0 = new BigInteger(ee, 16);
        BigInteger n0 = new BigInteger(nn, 16);
        BigInteger c0 = new BigInteger(c,16);
        return c0.modPow(e0, n0).toString(16);
    }

    public void verifyMessage(String m, String ds, HashFunction hF, String ee, String nn) {
        System.out.println();
        String hash = hF.getHash(m);
        String hash1 = decryptHash(ds, ee, nn);
        if (hash.equals(hash1)) {
            System.out.println("Digital signature is valid");
        } else {
            System.out.println("Digital signature is invalid. The hash of entered message:\n" + hash);
            System.out.println("Valid hash of message:\n" + hash1);
        }
    }

    public void showPublicKeys() {
        System.out.println("A pair of generated public keys:");
        System.out.println("e = " + e.toString(16));
        System.out.println("n = " + n.toString(16));
    }

}
