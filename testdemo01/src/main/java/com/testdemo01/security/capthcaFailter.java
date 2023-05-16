package com.testdemo01.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.testdemo01.common.exception.captchaException;
import com.testdemo01.common.lang.Const;
import com.testdemo01.util.RedisUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 图片验证码校验过滤器
@Component
public class capthcaFailter extends OncePerRequestFilter {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    loginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // System.out.println("====进入图片验证码filter====");

        String url = request.getRequestURI();
        if ("/login".equals(url) && request.getMethod().equals("POST")) {
            try {
                // 验证码校验
                validate(request);
            } catch (captchaException e) {
                // 进入登录失败 filter
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }

        }
        filterChain.doFilter(request, response);
    }

    /**
     * <h2>验证码校验方法</h2>
     * @param request
     */
    private void validate(HttpServletRequest request) {

        String code = request.getParameter("code");
        String key = request.getParameter("token");

        System.out.println("code: " + code);
        System.out.println("key: " + key);

        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new captchaException("验证码不匹配");
        }

        // test
        System.out.println("redis->code: " + redisUtil.hashmapget(Const.CAPTCHA_KEY, key));

        if (!code.equals(redisUtil.hashmapget(Const.CAPTCHA_KEY, key))) {
            throw new captchaException("验证码错误");
        }
        redisUtil.hashmapdel(Const.CAPTCHA_KEY, key);
    }

}
