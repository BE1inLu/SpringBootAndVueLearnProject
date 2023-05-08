package com.testdemo01.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.testdemo01.config.jwtPropertiesConfig;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;



@Component
public class JwtUtil {


    @Autowired
    jwtPropertiesConfig jwtPropertiesConfig;

    /**
     * JWT 生成方法
     * 
     * @param username
     * @return
     */
    public String generateToken(String username) {

        // 日期创建
        Date nowdate = new Date();
        Date expiredDate = new Date(nowdate.getTime() + 1000 * jwtPropertiesConfig.getExpire());
        String localtoken;

        localtoken = JWT.create()
                .setPayload("username", username)
                .setIssuedAt(nowdate)
                .setExpiresAt(expiredDate)
                .setSigner(JWTSignerUtil.hs512(jwtPropertiesConfig.getSecret().getBytes()))
                .sign();

        return localtoken;
    }

    /**
     * 
     * JWT 解析方法
     * 
     * @param token
     * @return string:
     */
    public String getTokenPlayLoad(String token) {
        try {
            JWT jwt = JWT.of(token);
            return (String) jwt.getPayload("username");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * JWT 验证
     * 
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        try{
            System.out.println("jwtPropertiesConfig.getSecret(): "+jwtPropertiesConfig.getSecret());
        }catch(Exception e){
            // e.printStackTrace();
        }
        // return JWTUtil.verify(token, JWTSignerUtil.hs512(jwtPropertiesConfig.getSecret().getBytes()));
        return JWTUtil.verify(token, JWTSignerUtil.hs512("werdftertwer23412ert34534t".getBytes()));

    }

}
