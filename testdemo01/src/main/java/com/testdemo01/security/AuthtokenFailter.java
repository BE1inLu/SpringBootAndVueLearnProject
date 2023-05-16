package com.testdemo01.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.testdemo01.config.jwtPropertiesConfig;
import com.testdemo01.service.SysUserService;
import com.testdemo01.service.impl.SysUserDetailServiceImpl;
import com.testdemo01.util.JwtUtil;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// jwt验证filter
public class AuthtokenFailter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserDetailServiceImpl sysUserDetailService;

    @Autowired
    jwtPropertiesConfig jwtPropertiesConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 获取jwt，并解析

        System.out.println("====进入 AuthtokenFailter ===");
        String jwt;
        jwt = request.getHeader(jwtPropertiesConfig.getHeader());

        System.out.println("jwt: "+jwt);

        // 异常判断
        // 没有token，继续执行后面的拦截器
        if (StrUtil.isBlankOrUndefined(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwt == null) {
            throw new JwtException("token 异常");
        }

        if (!jwtUtil.isTokenExpired(jwt)) {
            throw new JwtException("token 过期");
        }

        // 获取用户权限等信息
        String username = jwtUtil.getTokenPlayLoad(jwt);

        UserDetails userDetails = sysUserDetailService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        // System.out.println("UsernamePasswordAuthenticationToken token:");
        // System.out.println(token);

        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);

        // System.out.println("token:");
        // System.out.println(token);

        System.out.println("===离开 AuthtokenFailter ===");
        filterChain.doFilter(request, response);

    }

}
