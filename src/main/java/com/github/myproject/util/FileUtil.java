package com.github.myproject.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtil {
    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 将MultipartFile转成File
     *
     * @param file
     * @param filename
     * @param tokenId
     * @return
     */
    public static File turnMultiFileToFile(MultipartFile file, String filename, String tokenId) {
        File newFile = null;
        logger.info("saveFiletoLocal start |tokenId={},filename={}", tokenId, filename);
        if (file == null || StringUtils.isEmpty(filename)) {
            logger.info("file or filename is null |tokenId={},filename={}", tokenId, filename);
            return null;
        }
        try {
            newFile = new File(filename);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            file.transferTo(newFile);
            logger.info("saveFiletoLocal end |tokenId={},filename={}", tokenId, filename);
        } catch (Exception e) {
            logger.error("turnMultiFileToFile is Exception|tokenId={},filename={}|Exception=", tokenId, filename, e);
            if (newFile != null && newFile.exists()) {
                newFile.delete();
            }
            return null;
        }
        return newFile;
    }

    /**
     * 文件转byte[]
     *
     * @param tokenId
     * @param file
     * @return
     */
    public static byte[] fileToByte(String tokenId, File file) {
        byte[] buffer = null;
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            fileInput.read(buffer);
        } catch (Exception e) {
            logger.error("fileToByte | exception| tokenId={},filename={},Exception={}", tokenId, file.getName(), e);
        } finally {
            try {
                if (fileInput != null) {
                    fileInput.close();
                }
            } catch (Exception e) {
            }
        }
        return buffer;
    }

    /**
     * byte[]转文件
     *
     * @param fileByte
     * @param fileName
     * @return
     */
    public static File ByteToFile(byte[] fileByte, String fileName) {
        File file = null;
        FileOutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(fileName);
            fileOutput.write(fileByte);
            file = new File(fileName);
        } catch (Exception e) {
            logger.error("ByteToFile | exception| fileName={},Exception={}", fileName, e);
        } finally {
            try {
                if (fileOutput != null) {
                    fileOutput.close();
                }
            } catch (Exception e) {
            }
        }
        return file;
    }

    /**
     * 将文件转成base64字符串
     */
    public static String fileToBase64(String tokenId, File file) {
        String fileBase64 = null;
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fileInput.read(buffer);
            fileBase64 = Base64.encodeBase64String(buffer);
        } catch (Exception e) {
            logger.error("fileToBase64 | exception| tokenId={},filename={},Exception={}", tokenId, file.getName(), e);
        } finally {
            try {
                if (fileInput != null) {
                    fileInput.close();
                }
            } catch (Exception e) {
            }
        }
        return fileBase64;
    }

    /**
     * 将base64字符解码保存文件
     */
    public static File base64ToFile(String tokenId, String base64Code, String filePath) {
        File file = null;
        FileOutputStream fileOutput = null;
        try {
            byte[] buffer = Base64.decodeBase64(base64Code);
            fileOutput = new FileOutputStream(filePath);
            fileOutput.write(buffer);
            file = new File(filePath);
        } catch (Exception e) {
            logger.error("base64ToFile | exception| tokenId={},base64Code={},Exception={}", tokenId, base64Code, e);
        } finally {
            try {
                if (fileOutput != null) {
                    fileOutput.close();
                }
            } catch (Exception e) {
            }
        }
        return file;
    }

    //base64字符串转byte[]
    public static byte[] base64ToByte(String tokenId, String base64Str) {
        byte[] fileByte = null;
        try {
            fileByte = Base64.decodeBase64(base64Str);
        } catch (Exception e) {
            logger.error("base64ToByte | exception| tokenId={},base64Str={},Exception={}", tokenId, base64Str, e);
        }
        return fileByte;
    }

    //byte[]转base64
    public static String byteToBase64(String tokenId, byte[] fileByte) {
        String base64 = null;
        try {
            base64 = Base64.encodeBase64String(fileByte);
        } catch (Exception e) {
            logger.error("base64ToByte | exception| tokenId={},Exception={}", tokenId, e);
        }
        return base64;
    }

    public static File getFileByName(String filename, String tokenId) {
        File file;
        if (StringUtils.isEmpty(filename)) {
            logger.info("filename is null |tokenId={},filename={}", tokenId, filename);
            return null;
        }
        try {
            file = new File(filename);
        } catch (Exception e) {
            logger.error("getFileByName is Exception|tokenId={},filename={}", tokenId, filename, e);
            return null;
        }
        return file;
    }

    /**
     * @param srcDirName
     * @param destDirName
     */
    public static File fileChannelCopy(String tokenId, String srcDirName, String destDirName) {
        File file = null;
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(new File(srcDirName));
            fo = new FileOutputStream(new File(destDirName));
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道中
            file = new File(destDirName);
        } catch (Exception e) {
            logger.error("fileChannelCopy is Exception|tokenId={},srcDirName={},destDirName={}", tokenId, srcDirName, destDirName, e);
            return file;
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                logger.error("fileChannelCopy | close stream exception |tokenId={},srcDirName={},destDirName={}", tokenId, srcDirName, destDirName, e);
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 如果文件存在就删除
     *
     * @param file 文件
     * @return 是否删除
     */
    public static boolean deleteIfExists(File file) {
        return file != null && file.exists() && file.delete();
    }


}
