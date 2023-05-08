package com.testdemo01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testdemo01.config.jwtPropertiesConfig;
import com.testdemo01.mapping.SysUserMapper;
import com.testdemo01.result.result;
import com.testdemo01.service.SysUserService;
import com.testdemo01.service.impl.SysUserDetailServiceImpl;
import com.testdemo01.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class TestController {
    @Autowired
    SysUserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    jwtPropertiesConfig jwtPropertiesConfig;

    @Autowired
    SysUserDetailServiceImpl SysUserDetailServiceImpl;

    @GetMapping("/test/testlist")
    public Object test() {
        return userService.list();
    }

    @PreAuthorize("hasrole('admin')")
    @GetMapping("test01")
    public Object test01(){
        return userService.list();
    }


    @GetMapping("test/password")
    public String pass() {

        String password = passwordEncoder.encode("111111");
        String password2="$2a$10$R7zegeWzOXPw871CmNuJ6upC0v8D373GuLuTw8jn6NET4BkPRZfgK";
        // 加密后的密码
        System.out.println("加密后的密码1 :"+password);
        System.out.println("加密后的密码2 :"+password2);
        boolean matchs1 = passwordEncoder.matches("111111", password);
        boolean matchs2 = passwordEncoder.matches("111111", password2);
        System.out.println("结果1" + matchs1);
        System.out.println("结果2" + matchs2);
        System.out.println(result.succ(password));
        return "succ";
    }

    @GetMapping("test/jwt")
    result test02(){

        // 测试jwt调用
        // System.out.println(jwtUtil.getExpire());
        // System.out.println(jwtUtil.getHeader());
        // System.out.println(jwtUtil.getSecret());

        // token创建
        String token=jwtUtil.generateToken("admin");
        System.out.println("token :"+token);

        // token解析
        String local_string=jwtUtil.getTokenPlayLoad(token);
        System.out.println("解析结果 ："+local_string);

        return result.succ(token);
    }

    @GetMapping("test/test01")
    void test03(){
        System.out.println(sysUserMapper.listByMenuId((long) 1));
    }


    @GetMapping("/test/getjwt")
    result test04(){

        System.out.println(jwtPropertiesConfig.getExpire());
        System.out.println(jwtPropertiesConfig.getHeader());
        System.out.println(jwtPropertiesConfig.getSecret());

        return result.succ("succ");
    }


    @GetMapping("/test/test04")
    result test05(){

        log.info("SysUserDetailServiceImpl: "+SysUserDetailServiceImpl.loadUserByUsername("admin"));

        return result.succ("succ");
    }

}
