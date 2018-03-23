package com.linkedin.replica.signing.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA512 {

    private static final Logger LOGGER = LogManager.getLogger(SHA512.class.getName());

    public static String hash(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.warn(ex.getMessage());
        }
        md.update(password.getBytes());
        byte byteData[] = md.digest();
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