package com.example.serve.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 * 
 * @author wu
 */
public class JwtUtil {

    // 有效期为
    public static final Long JWT_TTL = 24 * 60 * 60 * 1000L;// 60 * 60 *1000 一个小时
    // 设置秘钥明文
    public static final String JWT_KEY = "lLp91HF5gDeQiEZOSFzX4fGD5xQK6Gv8LTkY0BmHpAqC7NuWrVjM";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成jtw
     * 
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();

        // 确保密钥长度适合HS256算法
        if (secretKey.getEncoded().length < 32) {
            System.out.println("警告：密钥长度可能不足！当前长度: " + secretKey.getEncoded().length);
        }

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);

        try {
            // 打印关键信息，方便调试
            System.out.println("JWT构建开始 - 算法: " + signatureAlgorithm.name());
            System.out.println("JWT subject长度: " + (subject != null ? subject.length() : 0));

            return Jwts.builder()
                    .setId(uuid) // 唯一的ID
                    .setSubject(subject) // 主题 可以是JSON数据
                    .setIssuer("wu") // 签发者
                    .setIssuedAt(now) // 签发时间
                    .signWith(signatureAlgorithm, secretKey) // 使用HS256对称加密算法签名, 第二个参数为秘钥
                    .setExpiration(expDate);
        } catch (Exception e) {
            System.out.println("JWT构建过程中发生错误: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 生成jtw
     * 
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 创建token
     * 
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    // 添加测试方法，用于验证JWT生成
    public static void main(String[] args) {
        try {
            String testSubject = "{\"name\":\"test\",\"id\":1}";
            String token = createJWT(testSubject);
            System.out.println("测试生成的JWT token: " + token);

            // 验证token格式
            String[] parts = token.split("\\.");
            System.out.println("Token部分数量: " + parts.length);
            if (parts.length != 3) {
                System.out.println("警告: 生成的token不是标准的JWT格式!");
            } else {
                System.out.println("token格式正确");

                // 解析token
                Claims claims = parseJWT(token);
                System.out.println("解析的Subject: " + claims.getSubject());
                System.out.println("解析的Issuer: " + claims.getIssuer());
                System.out.println("解析的Id: " + claims.getId());
            }
        } catch (Exception e) {
            System.out.println("JWT测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 生成加密后的秘钥 secretKey
     * 
     * @return
     */
    public static SecretKey generalKey() {
        try {
            byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);

            // 确保使用正确的算法和密钥长度
            // 注意：HS256需要至少32字节（256位）密钥
            // 这里使用AES算法，但实际上JWT的签名使用的是HMAC算法
            SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA256");
            return key;
        } catch (Exception e) {
            System.out.println("密钥生成错误: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}