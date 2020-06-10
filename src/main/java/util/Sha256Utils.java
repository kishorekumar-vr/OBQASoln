package util;

import org.apache.commons.net.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Sha256Utils {

    public static SecureRandom secureRandom = new SecureRandom();

    public static void getRandom(byte[] value) {
        secureRandom.nextBytes(value);
    }

    public static String getSHA256UrlEncoded(String value) {
        return Base64.encodeBase64URLSafeString(Sha256Utils.getSHA256Digest(value));
    }

    public static byte[] getSHA256Digest(String value) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return md.digest(value.getBytes());
    }
}
