package com.testdemo01.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.testdemo01.config.jwtPropertiesConfig;
import com.testdemo01.result.result;
import com.testdemo01.util.JwtUtil;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class loginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtUtil jwtUtil;
    
    @Autowired
    jwtPropertiesConfig jwtPropertiesConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        System.out.println("====进入 loginsuccfilter ====");

        response.setContentType("application/json;charset=UTF-8");
        // 创建流
        ServletOutputStream outputStream = response.getOutputStream();

        // 生成jwt
        System.out.println("authentication.getName():"+authentication.getName());
        String jwt = jwtUtil.generateToken(authentication.getName());
        System.out.println("loginsucc生成的jwt: "+jwt);
        response.setHeader(jwtPropertiesConfig.getHeader(), jwt);
        result varResule = result.succ("login success");
        // // test
        // System.out.println(JSONUtil.toJsonStr(varResule).getBytes("UTF-8"));

        outputStream.write(JSONUtil.toJsonStr(varResule).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

}
