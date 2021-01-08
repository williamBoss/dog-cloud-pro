/**
 * Copyright 2019 Yanzheng (https://github.com/micyo202). All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dog.cloud.core.utils.security;

import com.dog.cloud.core.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * HmacUtil
 * Hmac工具类
 *
 * @author Yanzheng (https://github.com/micyo202)
 * @date 2019/10/22
 */
@Slf4j
public class HmacUtil {

    /**
     * 加密、解密方式：SHA-1、SHA-224、SHA-256、SHA-384、SHA-512
     */
    private static final String HMAC_MD5 = "HmacMD5";
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String HMAC_SHA224 = "HmacSHA224";
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String HMAC_SHA384 = "HmacSHA384";
    private static final String HMAC_SHA512 = "HmacSHA512";

    /**
     * 默认秘钥，必须16位
     */
    private static final String DEFAULT_KEY = "MkI3I1YlFOFr57YL";

    /**
     * HMAC MD5 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encryptHmacMD5(String text) {
        return encryptHmac(text, DEFAULT_KEY, HMAC_MD5);
    }

    /**
     * HMAC SHA1 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encryptHmacSHA1(String text) {
        return encryptHmacSHA1(text, DEFAULT_KEY);
    }

    /**
     * HMAC SHA1 加密
     *
     * @param text 明文
     * @param key  秘钥
     * @return 密文
     */
    public static String encryptHmacSHA1(String text, String key) {
        return encryptHmac(text, key, HMAC_SHA1);
    }

    /**
     * HMAC SHA224 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encryptHmacSHA224(String text) {
        return encryptHmacSHA224(text, DEFAULT_KEY);
    }

    /**
     * HMAC SHA224 加密
     *
     * @param text 明文
     * @param key  秘钥
     * @return 密文
     */
    public static String encryptHmacSHA224(String text, String key) {
        return encryptHmac(text, key, HMAC_SHA224);
    }

    /**
     * HMAC SHA2564 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encryptHmacSHA256(String text) {
        return encryptHmacSHA256(text, DEFAULT_KEY);
    }

    /**
     * HMAC SHA256 加密
     *
     * @param text 明文
     * @param key  秘钥
     * @return 密文
     */
    public static String encryptHmacSHA256(String text, String key) {
        return encryptHmac(text, key, HMAC_SHA256);
    }

    /**
     * HMAC SHA384 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encryptHmacSHA384(String text) {
        return encryptHmacSHA384(text, DEFAULT_KEY);
    }

    /**
     * HMAC SHA384 加密
     *
     * @param text 明文
     * @param key  秘钥
     * @return 密文
     */
    public static String encryptHmacSHA384(String text, String key) {
        return encryptHmac(text, key, HMAC_SHA384);
    }

    /**
     * HMAC SHA521 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encryptHmacSHA512(String text) {
        return encryptHmacSHA512(text, DEFAULT_KEY);
    }

    /**
     * HMAC SHA521 加密
     *
     * @param text 明文
     * @param key  秘钥
     * @return 密文
     */
    public static String encryptHmacSHA512(String text, String key) {
        return encryptHmac(text, key, HMAC_SHA512);
    }

    /**
     * HMAC 加密基本算法
     *
     * @param text 明文
     * @param key  秘钥
     * @param type 类型
     * @return 密文
     */
    private static String encryptHmac(String text, String key, String type) {
        if (StringUtils.isAnyBlank(text, key, type)) {
            return null;
        }
        try {
            // byte[] key = getHmacKey(type);   // 随机生成秘钥
            byte[] keyBytes = key.getBytes(Constants.UTF8);
            byte[] dataBytes = text.getBytes(Constants.UTF8);

            // 1、还原密钥
            SecretKey secretKey = new SecretKeySpec(keyBytes, type);
            // 2、创建MAC对象
            Mac mac = Mac.getInstance(type);
            // 3、设置密钥
            mac.init(secretKey);
            // 4、数据加密
            byte[] bytes = mac.doFinal(dataBytes);
            // 5、生成数据
            String encodeHex = encodeHex(bytes, true);
            return encodeHex;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * HMAC Key 获取
     *
     * @param type 类型
     * @return 字节流key
     */
    private static byte[] getHmacKey(String type) {
        try {
            // 1、创建密钥生成器
            KeyGenerator keyGenerator = KeyGenerator.getInstance(type);
            // 2、产生密钥
            SecretKey secretKey = keyGenerator.generateKey();
            // 3、获取密钥
            byte[] encoded = secretKey.getEncoded();
            return encoded;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 数据转16进制编码
     *
     * @param data        数据流
     * @param toLowerCase 是否转小写
     * @return 16进制编码字符
     */
    private static String encodeHex(final byte[] data, final boolean toLowerCase) {
        final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return new String(out);
    }
}