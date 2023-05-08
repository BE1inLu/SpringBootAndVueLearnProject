package com.testdemo01.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.testdemo01.config.jwtPropertiesConfig;
import com.testdemo01.entity.SysUser;
import com.testdemo01.service.SysUserService;
import com.testdemo01.service.impl.SysUserDetailServiceImpl;
import com.testdemo01.util.JwtUtil;
import com.testdemo01.util.RedisUtil;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// 认证管理器实现
@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    jwtPropertiesConfig jwtPropertiesConfig;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserDetailServiceImpl SysUserDetailService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        System.out.println("====进入JwtAuthenticationFilter===");        
        String jwtstr;
        try {
            String localstr = "Authorization";

            System.out.println("request.getHeader(localstr): " + request.getHeader(localstr));

            jwtstr = request.getHeader(localstr);
            System.out.println("===jwtUtil已获取===");
        } catch (Exception e) {
            System.out.println("=====JwtUtil异常=====");
            e.printStackTrace();
            jwtstr = null;
        }

        // 异常判断
        // 没有token，继续执行后面的拦截器
        if (StrUtil.isBlankOrUndefined(jwtstr)) {
            chain.doFilter(request, response);
            return;
        }

        // 判断 jwtstr：token 有无错误
        String jwt = jwtstr;
        System.out.println("jwt: " + jwt);

        // System.out.println("解密结果：" + JWTUtil.verify(jwt, "werdftertwer23412ert34534t".getBytes()));

        System.out.println("jwtutil: " + jwtUtil);

        if (jwt == null) {
            throw new JwtException("token 异常");
        }

        if (!jwtUtil.isTokenExpired(jwt)) {
            throw new JwtException("token 过期");
        }

        // 获取用户权限等信息
        String username = jwtUtil.getTokenPlayLoad(jwt);

        SysUser sysUser = sysUserService.getByUsername(username);

        System.out.println("sysuser: "+sysUser);
        System.out.println("sysUserService.getByUsername(username): "+sysUserService.getByUsername(username));

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null,SysUserDetailService.getUserAuthority(sysUser.getId()));

        System.out.println("token:");
        System.out.println(token);

        // SecurityContextHolder.getContext().setAuthentication(token);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);


        System.out.println("SecurityContext.getAuthentication(): ");
        System.out.println(context.getAuthentication());

        System.out.println("====离开JwtAuthenticationFilter===");
        chain.doFilter(request, response);
    }

}
