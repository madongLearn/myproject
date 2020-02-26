package com.github.myproject.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by madong on 2016/5/12.
 * <p>
 * The helper class is used to encrypt and decrypt the confidential filed data in the database.
 * <p>
 * The key can be set via the environment variable "STORM_FIELD_ENCKEY", so that he env variable is deleted after
 * the application started in order to avoid the possible key leak.
 */
public class SecureFieldCipher {
    private final static Logger logger = LoggerFactory.getLogger(SecureFieldCipher.class);

    private static final Random RANDOM = new SecureRandom();
    private static final int IV_LENGTH = 16;
    // private String currentKey = "nj02";
    // private String value = "nj01:0b4042c754174e6792bb3172ea287d01#nj02:0b4042c754174ewq321dw3ewe2132112";

    private String currentKey = "1";
    private String value = "1:0b4042c754174e6792bb3172ea287d01#2:0b4042c754174ewq321dw3ewe2132112";


    private final static HashMap<String, String> ENC_KEY = new HashMap<String, String>();

    private static String keyType;

    private Cipher cipher;

    public SecureFieldCipher() throws Exception {
        String[] arrayValue = value.split("#");
        String keyValue = "";
        if (arrayValue.length > 0) {
            for (int i = 0; i < arrayValue.length; i++) {
                String[] keyValuePair = arrayValue[i].split(":");
                if (keyValuePair[0].equals(currentKey)) {
                    keyType = keyValuePair[0];
                    keyValue = keyValuePair[1];
                    break;
                }
            }
        }
        if (keyValue.length() < 16) {
            logger.error("bad encrypted key for secure file and must be greater than 16");
            throw new Exception("bad encrypted key for secure file and must be greater than 16");
        }

        ENC_KEY.put(keyType, keyValue);

        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    }

    public String encrypt(String contents) {
        try {
            byte[] ivBytes = getIV();
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(getAesCipherKey(ENC_KEY.get(keyType)), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(contents.getBytes("UTF-8"));
            return String.format("%s:%s:%s", keyType, byteToString(ivBytes), byteToString(encrypted));
        } catch (Exception e) {
            logger.error("failed to encrypt secure filed. error message: {}", e.getMessage());
            throw new RuntimeException("failed to encrypt the secure field.");
        }
    }

    public String decrypt(String contents) {
        String[] sections = contents.split(":");
        if (sections.length != 3) {
            logger.warn("failed to decrypt the secure field because of wrong contents");
            throw new RuntimeException("failed to decrypt the secure field");
        }
        keyType = sections[0];
        try {
            IvParameterSpec iv = new IvParameterSpec(stringToByte(sections[1]));
            SecretKeySpec secretKeySpec = new SecretKeySpec(getAesCipherKey(ENC_KEY.get(keyType)), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] decrypted = cipher.doFinal(stringToByte(sections[2]));
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            logger.error("failed to decrypt the secure field. error message: {}", e.getMessage());
            throw new RuntimeException("failed to decrypt the secure field");
        }
    }

    private byte[] getIV() {
        byte[] bytes = new byte[IV_LENGTH];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    private byte[] getAesCipherKey(String key) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(key.getBytes());
        byte[] hashedBytes = digest.digest();
        return Arrays.copyOf(hashedBytes, 16);

    }

    private static String byteToString(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    private static byte[] stringToByte(String input) {
        if (Base64.isBase64(input)) {
            return Base64.decodeBase64(input);
        } else {
            return Base64.encodeBase64(input.getBytes());
        }
    }
}
