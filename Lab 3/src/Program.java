import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        RSA crypto = new RSA(256);
        HashFunction hashF = new HashFunction(crypto.getN());
        crypto.showPublicKeys();
        System.out.println("Enter a message to digitally sign it");

        String hash = hashF.getHash(in.nextLine());
        System.out.printf("The hash of entered message:\n%s\n\nThe digital signature of entered message:\n%s\n\n",
                hash, crypto.encryptHash(hash));
        System.out.println("Enter the message, its digital signature and the public keys pair (n, b) " +
                "on separate lines");
        crypto.verifyMessage(in.nextLine(), in.nextLine(), hashF, in.nextLine(), in.nextLine());
    }
}
