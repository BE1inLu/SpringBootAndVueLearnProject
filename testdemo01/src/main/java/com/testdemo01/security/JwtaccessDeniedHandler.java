package com.testdemo01.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.testdemo01.result.result;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtaccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        // 状态码设置：403 权限不足
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // 创建流
        ServletOutputStream outputStream = response.getOutputStream();
        result varresult = result.fail(accessDeniedException.getMessage());
        outputStream.write(JSONUtil.toJsonStr(varresult).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();

    }

}
