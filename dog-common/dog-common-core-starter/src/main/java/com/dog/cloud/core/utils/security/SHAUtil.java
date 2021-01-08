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

import java.security.MessageDigest;

/**
 * SHAUtil
 * SHA哈希散列工具类
 *
 * @author Yanzheng (https://github.com/micyo202)
 * @date 2019/10/21
 */
@Slf4j
public class SHAUtil {

    /**
     * 加密、解密方式：SHA-1、SHA-224、SHA-256、SHA-384、SHA-512
     */
    private static final String SHA_1 = "SHA-1";
    private static final String SHA_224 = "SHA-224";
    private static final String SHA_256 = "SHA-256";
    private static final String SHA_384 = "SHA-384";
    private static final String SHA_512 = "SHA-512";

    /**
     * SHA1 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encrypt(String text) {
        return encrypt(text, SHA_1);
    }

    /**
     * SHA1 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encrypt224(String text) {
        return encrypt(text, SHA_224);
    }

    /**
     * SHA1 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encrypt256(String text) {
        return encrypt(text, SHA_256);
    }

    /**
     * SHA1 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encrypt384(String text) {
        return encrypt(text, SHA_384);
    }

    /**
     * SHA1 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encrypt512(String text) {
        return encrypt(text, SHA_512);
    }

    /**
     * SHA 通用加密算法
     *
     * @param text      明文
     * @param algorithm 加密类型
     * @return 密文
     */
    private static String encrypt(String text, String algorithm) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(text.getBytes(Constants.UTF8));
            byte[] bytes = messageDigest.digest();
            return bytes2Str(bytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 字节转字符串
     *
     * @param bytes 字节
     * @return 字符串
     */
    private static String bytes2Str(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String str;
        for (int i = 0; i < bytes.length; i++) {
            str = Integer.toHexString(bytes[i] & 0xFF);
            if (str.length() == 1) {
                // 1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }
}
