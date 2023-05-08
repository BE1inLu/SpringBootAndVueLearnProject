package com.testdemo01.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.testdemo01.config.jwtPropertiesConfig;
import com.testdemo01.result.result;
// import com.testdemo01.util.JwtUtil;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    // @Autowired
    // JwtUtil jwtUtil;

    @Autowired
    jwtPropertiesConfig jwtPropertiesConfig;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
                
                if(authentication!=null){
                    // authentication 存在则清空 authentication
                    new SecurityContextLogoutHandler().logout(request, response, authentication);
                }

                System.out.println("====进入 logoutfilter 执行 logout ====");
        response.setContentType("application/json;charset=UTF-8");
        // 创建流
        ServletOutputStream outputStream = response.getOutputStream();

        // 生成jwt
        response.setHeader(jwtPropertiesConfig.getHeader(), "");
        result varResule = result.succ("logout success");
        outputStream.write(JSONUtil.toJsonStr(varResule).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

}
