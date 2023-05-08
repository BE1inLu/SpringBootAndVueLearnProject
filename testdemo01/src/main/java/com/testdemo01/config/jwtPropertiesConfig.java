package com.testdemo01.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "testdemo01.jwt")
public class jwtPropertiesConfig {
    
    // 过期时间
    private long expire;
    
    // 秘钥
    // werdftertwer23412ert34534t
    private String secret;

    // 名称
    private String header;

}
