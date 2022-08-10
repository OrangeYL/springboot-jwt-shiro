package com.orange.springbootjwtshiro.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Jwt工具类，用来签名和校验token
 *
 * @author:Li ZhiCheng
 * @create:2022-08-2022/8/10 14:48
 */

@Slf4j
public class JwtUtil {

    private static final long EXPIRE_TIME=30*60*1000;

    /**
     * 校验token是否正确
     *
     * @param token
     * @param username
     * @param secret
     * @return
     */
    public static boolean verify(String token,String username,String secret){
        try {
            //根据密码生成JWT校验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            //校验token
            DecodedJWT jwt = verifier.verify(token);
            log.info("登录验证成功");
            return true;
        } catch (Exception e) {
            log.info("JwtUtil登录验证失败");
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @param token
     * @return
     */
    public static  String getUsername(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (Exception e) {
           return null;
        }
    }

    /**
     * 生成token签名且在EXPIRE_TIME分钟后过期
     * @param username
     * @param secret
     * @return
     */
    public static String sign(String username,String secret){
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                // 附带username信息
                .withClaim("username",username)
                .withExpiresAt(date)
                .sign(algorithm);
    }
}
