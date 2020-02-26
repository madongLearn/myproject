package com.github.myproject.util;

import java.io.*;
import java.security.MessageDigest;

public class MD5Util {

    /**
     * 获取文件MD5摘要
     *
     * @param file 文件
     * @return  产生的MD5摘要（16进制)
     */
    public static String md5(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) != -1) {
                md.update(buf, 0, len);
            }
            return bytesToHexString(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            safeClose(fis);
        }
        return null;
    }

    public static void safeClose(Closeable closeable) {
        if (null == closeable) {
            return;
        }
        try {
            if(closeable instanceof Flushable){
                ((Flushable) closeable).flush();
            }
            closeable.close();
        } catch (IOException e) {
            // We made out best effort to release this resource. Nothing more we can do.
        }
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for(int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * sha256加密
     * @param message 待加密内容
     * @return 加密结果
     */
    public static String sha256(String message){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(message.getBytes("UTF-8"));
            return bytesToHexString(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}