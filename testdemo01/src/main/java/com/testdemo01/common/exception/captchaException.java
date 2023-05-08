package com.testdemo01.common.exception;

import org.springframework.security.core.AuthenticationException;

// 验证码异常返回类
public class captchaException extends AuthenticationException{
    
    public captchaException(String msg) {
        super(msg);
    }
    
}
