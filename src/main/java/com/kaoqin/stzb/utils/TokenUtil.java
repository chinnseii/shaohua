/*
 * @Date: 2021-07-27 16:34:23
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-09-22 17:33:45
 */
package com.kaoqin.stzb.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenUtil {

    /**
     * @description: 生成token
     * @param {String} mobile
     * @param {Long} expier
     * @param {String} secretKey
     * @return {*}
     */
    public static String createToken(String email, String secretKey) {
        String token = "";
        try {
            // 私钥加密
            Map<String, Object> header = new HashMap<String, Object>(2);
            header.put("type", "JWT");
            header.put("alg", "HS256");
            token = JWT.create().withHeader(header).withClaim("email", email).sign(Algorithm.HMAC256(secretKey));
            return token;
        } catch (Exception e) {
            log.error("身份令牌生成失败"+e.getMessage());
            return null;
        }
    }

    /**
     * @description: 验证token
     * @param {String} token
     * @param {String} secretKey
     * @param {String} mobile
     * @return {*}
     */
    public static boolean checkToken(String token, String secretKey, String email) {
        try {
            // 验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            Claim claimEmail = decodedJWT.getClaim("email");
            return claimEmail.asString().equals(email);
        } catch (Exception e) {
            log.info("用户"+email+" 密钥不正确");
            return false;
        }
    }
}
