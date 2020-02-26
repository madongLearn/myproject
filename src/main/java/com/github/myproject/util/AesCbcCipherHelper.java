package com.github.myproject.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class AesCbcCipherHelper {
    private static final Logger logger = LoggerFactory.getLogger(AesCbcCipherHelper.class);

    private String aesKey;

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public AesCbcCipherHelper() {



    }

    public AesCbcCipherHelper(String key) {
        this.aesKey = key;
    }

    public String encrypt(String content) {
        if (StringUtils.isBlank(content)) {
            logger.warn("encrypt: content is blank.");
            return null;
        } else {

            try {
                Cipher cipher = getCipher();
                byte[] aesKeys = getAesCipherKey(aesKey);
                init(cipher, Cipher.ENCRYPT_MODE, aesKeys);
                byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
                return Base64.getEncoder().encodeToString(encrypted);
            } catch (Exception var7) {
                logger.warn("encrypt: catch e.msg={}", var7.getMessage());
                throw new IllegalStateException("failed to encrypt the secure field.");
            }
        }
    }

    public String decrypt(String content) {
        if (StringUtils.isBlank(content)) {
            logger.warn("decrypt: content is blank.");
            return null;
        } else {

            try {
                Cipher cipher = getCipher();
                byte[] aesKeys = getAesCipherKey(aesKey);
                init(cipher, Cipher.DECRYPT_MODE, aesKeys);
                byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(content));
                return new String(decrypted, "UTF-8");
            } catch (Exception var8) {
                logger.warn("decrypt: catch e.msg={}", var8.getMessage());
                throw new IllegalStateException("failed to decrypt the secure field.");
            }
        }
    }

    private static void init(Cipher cipher, int mode, byte[] secretKeys) throws Exception {
        cipher.init(mode, new SecretKeySpec(secretKeys, "AES"), new IvParameterSpec(Arrays.copyOfRange(secretKeys, 0, 16)));
    }

    private static Cipher getCipher() {
        try {
            return Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (Exception var1) {
            throw new IllegalStateException(var1);
        }
    }

    private static byte[] getAesCipherKey(String key) {
        try {
            return Hex.encodeHexString(key.getBytes()).substring(0, 16).getBytes("UTF-8");
        } catch (Exception var2) {
            throw new IllegalStateException("NoSuchAlgorithmException,stop!");
        }
    }
}
