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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;

/**
 * AESUtil
 * AES工具类
 *
 * @author Yanzheng (https://github.com/micyo202)
 * @date 2019/10/16
 */
@Slf4j
public class AesUtil {

    /**
     * 加密、解密方式
     */
    private static final String AES = "AES";

    /**
     * 初始向量值，必须16位
     */
    private static final String IV_STRING = "16-Bytes--String";

    /**
     * 默认秘钥，必须16位
     */
    private static final String DEFAULT_KEY = "70pQxrWV7NWgGRXQ";

    /**
     * 指定加密的算法、工作模式和填充方式
     */
    private static final String CIPHER = "AES/CBC/PKCS5Padding";

    /**
     * AES 使用默认秘钥加密
     *
     * @param text 明文
     * @return 密文
     */
    public static String encrypt(String text) {
        return encrypt(text, DEFAULT_KEY);
    }

    /**
     * AES 自定义秘钥加密
     *
     * @param text 明文
     * @param key  秘钥（必须16位）
     * @return 密文
     */
    public static String encrypt(String text, String key) {
        if (StringUtils.isAnyBlank(text, key) || 16 != key.length()) {
            return null;
        }
        try {
            byte[] byteContent = text.getBytes(Constants.UTF8);
            byte[] enCodeFormat = key.getBytes();
            // 注意，为了能与 iOS 统一这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AES);
            byte[] initParam = IV_STRING.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(byteContent);
            return Base64.encodeBase64String(encryptedBytes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * AES 默认秘钥解密
     *
     * @param ciphertext 密文
     * @return 明文
     */
    public static String decrypt(String ciphertext) {
        return decrypt(ciphertext, DEFAULT_KEY);
    }

    /**
     * AES 自定义秘钥解密
     *
     * @param ciphertext 密文
     * @param key        秘钥（必须16位）
     * @return 明文
     */
    public static String decrypt(String ciphertext, String key) {
        if (StringUtils.isAnyBlank(ciphertext, key) || 16 != key.length()) {
            return null;
        }
        try {
            byte[] encryptedBytes = Base64.decodeBase64(ciphertext);
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, AES);
            byte[] initParam = IV_STRING.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal(encryptedBytes);
            return new String(result, Constants.UTF8);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    //----------------------------------- PKCS7Padding 加解密-----------------------------------
    static {
        //BouncyCastle是一个开源的加解密解决方案，主页在http://www.bouncycastle.org/
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * AES解密 PKCS7Padding
     *
     * @param data           密文，被加密的数据
     * @param key            秘钥
     * @param iv             偏移量
     * @param encodingFormat 解密后的结果需要进行的编码
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key, String iv, String encodingFormat) throws Exception {
        //被加密的数据
        byte[] dataByte = Base64.decodeBase64(data.getBytes());
        //加密秘钥
        byte[] keyByte = Base64.decodeBase64(key.getBytes());
        //偏移量
        byte[] ivByte = Base64.decodeBase64(iv.getBytes());
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return new String(resultByte, encodingFormat);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // ------------------------------------------end——----------------------------------

}
