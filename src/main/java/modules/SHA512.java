package modules;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512 {

    public static String hash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        System.out.println(md.getProvider() + "  " + md.getAlgorithm() + "  "
                + md.getDigestLength());

        md.update(password.getBytes());

        /*
         * The digest method can be called once for a given number of updates.
         * After digest has been called, the MessageDigest object is reset to
         * its initialized state.
         */
        byte byteData[] = md.digest();

        System.out.println("Password:" + password);
        System.out.println("Mesage Digest(Hex): " + bytesToHex(byteData));
        System.out.println("Digest Message Length = " + byteData.length * 8);
        return bytesToHex(byteData);
    }

    /**
     * Convert a binary byte array into readable hex form
     *
     * @param hash
     * @return
     */
    private static String bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
}
