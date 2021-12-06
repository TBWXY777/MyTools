package com.tubai.study_html.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;

/**
 * 注意，只要客户端的token不过期 即使咱服务器关停了 也仍然正常
 * 因为token是基于客户端的管理
 */
public class JwtUtils {

    /**
     * 生成token:header.payload.signature
     * 带有角色版的
     * @return
     */
    public static String createToken(String account
            , String secret,String role){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,1);//1天过期
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("account",account)
        .withClaim("role",role);
        String token = builder.withExpiresAt(instance.getTime())//指定令牌过期时间
                .sign(Algorithm.HMAC256(secret));//签名 秘钥不能泄露
        return token;
    }

    /**
     * 验证token 如果合法直接返回true
     * 带有角色版的
     */
    public static boolean verifyToken(String token,String account,String secret
    ,String role){
        //拿传来token和我们拿同样payload以及秘钥生成的token对比
        try{
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                    .withClaim("account",account)
                    .withClaim("role",role)
                    .build()
                    .verify(token);
            return true;
        }catch (Exception e){
            //异常说明token不相同
            return false;
        }
    }

    /**
     * 生成长久的token
     * @param account
     * @param secret
     * @return
     */
    public static String createTokenWithLongTime(String account
            , String secret){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH,1);//1月过期
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("account",account);
        String token = builder.withExpiresAt(instance.getTime())//指定令牌过期时间
                .sign(Algorithm.HMAC256(secret));//签名 秘钥不能泄露
        return token;
    }

    /**
     * 生成永久的token
     * @param account
     * @param secret
     * @param role
     * @return
     */
    public static String createPermanentToken(String account
            , String secret,String role){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR,2);//2年过期
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("account",account)
                .withClaim("role",role);
        String token = builder.withExpiresAt(instance.getTime())//指定令牌过期时间
                .sign(Algorithm.HMAC256(secret));//签名 秘钥不能泄露
        return token;
    }

    /**
     * 从token中解析出指定的key，例如username，password
     */
    public static String get(String token,String key) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取该token的过期时间
     * @param token
     * @return
     */
    public static String getExpireAt(String token){
        Date expiresAt = JWT.decode(token).getExpiresAt();
        return DateFormatUtils.getFormatDate("yyyy-MM-dd HH:mm:ss",expiresAt);
    }

    /**
     * 查看这个token是否过期
     * @param token
     * @return 过期则返回true
     */
    public static boolean isExpireAt(String token){
        Date expires = JWT.decode(token).getExpiresAt();
        //如果当前系统时间已经超过了签证时间
        if(new Date().getTime()>expires.getTime()){
            return true;
        }
        return false;
    }
}
