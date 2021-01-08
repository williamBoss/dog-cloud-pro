package com.dog.cloud.core.utils.security;

import com.dog.cloud.core.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * Md5加密方法
 *
 * @author KING
 */
@Slf4j
public class Md5Utils {

    /**
     * 加密、解密方式
     */
    private static final String MD5 = "MD5";

    private static byte[] md5(String s) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance(MD5);
            algorithm.reset();
            algorithm.update(s.getBytes(Constants.UTF8));
            byte[] messageDigest = algorithm.digest();
            return messageDigest;
        } catch (Exception e) {
            log.error("MD5 Error...", e);
        }
        return null;
    }

    private static final String toHex(byte hash[]) {
        if (hash == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String hash(String s) {
        try {
            return new String(toHex(md5(s)).getBytes("UTF-8"), "UTF-8");
        } catch (Exception e) {
            log.error("not supported charset...{}", e);
            return s;
        }
    }
}
