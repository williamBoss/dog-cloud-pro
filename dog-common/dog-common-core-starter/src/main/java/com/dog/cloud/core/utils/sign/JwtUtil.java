package com.dog.cloud.core.utils.sign;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Jwt工具类
 *
 * @author KING
 * @version V1.0
 * @date 2020/8/4 9:52
 */
@Slf4j
public class JwtUtil {

    /**
     * 生成token
     *
     * @param claims      自定义claims存储信息
     * @param secret      签名秘钥
     * @param issuedAt    签发时间
     * @param expiration  过期时间（seconds）
     * @param serviceName 服务名称
     * @return
     */
    public static String generateToken(Map<String, Object> claims, String secret, Date issuedAt, Long expiration,
        String serviceName) {
        SecretKey secretKey = createSecretKey(secret);
        return Jwts.builder()
            //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
            .setClaims(claims)
            //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
            .setId(UUID.randomUUID().toString())
            //iat: jwt的签发时间
            .setIssuedAt(issuedAt).setSubject(serviceName)
            //设置签名使用的签名算法和签名使用的秘钥
            .signWith(SignatureAlgorithm.HS256, secretKey)
            //设置过期时间
            .setExpiration(generateExpirationDate(expiration)).compact();
    }

    /**
     * Token的解析
     *
     * @param token
     * @param secret
     * @return
     */
    public static Claims getClaimFromToken(String token, String secret) throws ExpiredJwtException {
        SecretKey secretKey = createSecretKey(secret);
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    /**
     * 验证token是否失效
     * true:过期   false:没过期
     *
     * @param token
     * @param secret
     * @return
     */
    public static Boolean isTokenExpired(String token, String secret) {
        try {
            final Date expiration = Objects.requireNonNull(getClaimFromToken(token, secret)).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            log.error(expiredJwtException.getMessage(), expiredJwtException);
            return true;
        }
    }

    /**
     * 签名私钥
     *
     * @param signKey
     * @return
     */
    private static SecretKey createSecretKey(String signKey) {
        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(signKey);
        // 根据给定的字节数组使用AES加密算法构造一个密钥，使用encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。
        SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return secretKey;
    }

    /**
     * 过期时间计算
     *
     * @param expiration 过期时间（seconds）
     * @return
     */
    private static Date generateExpirationDate(Long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

}
