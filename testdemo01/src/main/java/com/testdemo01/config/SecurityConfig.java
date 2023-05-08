package com.testdemo01.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.testdemo01.security.AuthtokenFailter;
import com.testdemo01.security.JwtAuthenticationFilter;
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

    // @Autowired
    // @Lazy
    // JwtAuthenticationFilter jwtAuthenticationFilter;

    // 获取 authenticationManager (认证管理器)，登录认证用
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


//     @Bean
//     JwtAuthenticationFilter jwtAuthenticationFilter(){
//         return new JwtAuthenticationFilter();
//     }


//     @Bean
//     AuthtokenFailter authtokenFailter(){
//         return new AuthtokenFailter();
//     }


    // @Bean
    // public DaoAuthenticationProvider authenticationProvider(){
    //     DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
    //     authenticationProvider.setUserDetailsService(userDetailsService);
    //     return authenticationProvider;
    // }


    // @Bean
    // JwtAuthenticationFilter jwtAuthenticationFilter()throws Exception{
    //     JwtAuthenticationFilter filter=new JwtAuthenticationFilter(null);
    //     return filter;
    // }

    // 使用自定义用户登录信息
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception
    // {
    // auth.userDetailsService(userDetailsService);
    // }

    // TODO: jwtAuthenticationFilter
    // @Bean
    // JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    //     // JwtAuthenticationFilter filter=new
    //     JwtAuthenticationFilter(authenticationManager());
    //     return jwtAuthenticationFilter();
    // }

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
        // 进入混淆
        // System.out.println("返回混淆算法");
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
        // 登录方法
        http.formLogin(
                (authz) -> authz
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler));
        // 登出方法
        http.logout(
                (authz) -> authz
                        .logoutSuccessHandler(jwtLogoutSuccessHandler));
        // 域名请求方法
        http.authorizeHttpRequests(
                (authz) -> authz
                        .requestMatchers(URL_WHITELIST).permitAll()
                        .anyRequest().authenticated());
        // 异常处理方法
        http.exceptionHandling(
                (handling) -> handling
                        .authenticationEntryPoint(jwtauthenticationEntryPoint)
                        .accessDeniedHandler(JwtaccessDeniedHandler));
        // session管理方法
        http.sessionManagement(
                (management) -> management
                        // 删除自带的session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // http.addFilterBefore(jwtAuthenticationFilter(), capthcaFailter.class);
        // 这里的filter是图片验证码filter，在登录前判定
        // filter 设置 在 passwordfilter 前判定验证码有无错误
        // http.addFilter(jwtAuthenticationFilter);

        // http.authenticationProvider(authenticationProvider());

        // http.addFilterAfter(authtokenFailter(), LogoutFilter.class);
        http.addFilterBefore(capthcaFailter, UsernamePasswordAuthenticationFilter.class);


        // http.exceptionHandling(handling -> handling.authenticationEntryPoint(jwtauthenticationEntryPoint));
        // http.httpBasic(jwtAuthenticationFilter());
        // TODO:filter修改,
        // filter 添加，返回用户权限信息
        // http.addFilterat(jwtAuthenticationFilter());

        // 用户信息配置（新）
        http.userDetailsService(userDetailsService);

        // 构建 filter
        return http.build();
    }

}
