package com.testdemo01.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.testdemo01.result.result;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// security 包 loginfail 登录失败处理类
@Component
public class loginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
                System.out.println("=====进入 onAuthenticationFailure =====");
        response.setContentType("application/json;charset=UTF-8");
        // 创建流
        ServletOutputStream outputStream = response.getOutputStream();
        result varresult = result
                .fail("Bad credentials".equals(exception.getMessage()) ? "用户名或密码不正确" : exception.getMessage());
        outputStream.write(JSONUtil.toJsonStr(varresult).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
                System.out.println("=====退出 onAuthenticationFailure =====");
    }

}
