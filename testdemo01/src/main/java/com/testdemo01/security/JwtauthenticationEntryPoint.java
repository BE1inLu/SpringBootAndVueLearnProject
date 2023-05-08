package com.testdemo01.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.testdemo01.result.result;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
@Component
public class JwtauthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
                response.setContentType("application/json;charset=UTF-8");
                // 状态码设置：401 未认证权限
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // 创建流
                ServletOutputStream outputStream = response.getOutputStream();
                result varresult = result.fail("请先登录");
                outputStream.write(JSONUtil.toJsonStr(varresult).getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
        
            }
    
}
