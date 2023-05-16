package com.testdemo01.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.testdemo01.security.AuthtokenFailter;
import com.testdemo01.security.JwtLogoutSuccessHandler;
import com.testdemo01.security.JwtaccessDeniedHandler;
import com.testdemo01.security.JwtauthenticationEntryPoint;
import com.testdemo01.security.capthcaFailter;
import com.testdemo01.security.loginFailureHandler;
import com.testdemo01.security.loginSuccessHandler;
import com.testdemo01.service.impl.SysUserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // loginFailureHandler 实现
    @Autowired
    loginFailureHandler loginFailureHandler;

    // loginSuccessHandler 实现
    @Autowired
    loginSuccessHandler loginSuccessHandler;

    // capthcaFailter 实现
    @Autowired
    capthcaFailter capthcaFailter;

    // JwtauthenticationEntryPoint 实现
    @Autowired
    JwtauthenticationEntryPoint jwtauthenticationEntryPoint;

    // JwtaccessDeniedHandler 实现
    @Autowired
    JwtaccessDeniedHandler JwtaccessDeniedHandler;

    // userDetailsService 实现
    @Autowired
    SysUserDetailServiceImpl userDetailsService;

    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    // 获取 authenticationManager (认证管理器)，登录认证用
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    AuthtokenFailter authtokenFailter() {
        return new AuthtokenFailter();
    }

    // dao认证实现
    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html#page-title
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(BcryptPasswordEncoder());
        return authenticationProvider;
    }

    // 请求头白名单
    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "favicon.ico",
            "/test/**",
    };

    // 加密混淆
    @Bean
    public BCryptPasswordEncoder BcryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    };

    // 写法参考
    // https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 跨域请求方法
        http.cors(withDefaults());
        // csrf防护
        http.csrf(
                (authz) -> authz
                        .disable());
        // 登录链
        http.formLogin(
                (authz) -> authz
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler));
        // 登出链
        http.logout(
                (authz) -> authz
                        .logoutSuccessHandler(jwtLogoutSuccessHandler));
        // 域名请求
        http.authorizeHttpRequests(
                (authz) -> authz
                        .requestMatchers(URL_WHITELIST).permitAll()
                        .anyRequest().authenticated());
        // 异常处理
        http.exceptionHandling(
                (handling) -> handling
                        .authenticationEntryPoint(jwtauthenticationEntryPoint)
                        .accessDeniedHandler(JwtaccessDeniedHandler));
        // session管理
        http.sessionManagement(
                // 删除自带的session
                (management) -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 图片验证码filter
        http.addFilterBefore(capthcaFailter, UsernamePasswordAuthenticationFilter.class);
        // 认证方式链
        http.authenticationProvider(authenticationProvider());
        // jwt验证filter
        http.addFilterBefore(authtokenFailter(), UsernamePasswordAuthenticationFilter.class);
        // 构建 filter
        return http.build();
    }

}
