package com.dog.cloud.core.utils.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * BCryptUtil
 * BCrypt工具类
 *
 * @author Yanzheng (https://github.com/micyo202)
 * @date 2019/10/21
 */
public class BCryptUtil {

    /**
     * BCrypt 加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encrypt(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        return BCrypt.hashpw(text, BCrypt.gensalt());
    }

    /**
     * BCrypt 校验
     *
     * @param text       明文
     * @param ciphertext 密文
     * @return 是否正确
     */
    public static boolean verify(String text, String ciphertext) {
        if (StringUtils.isAnyBlank(text, ciphertext)) {
            return false;
        }
        return BCrypt.checkpw(text, ciphertext);
    }
}
