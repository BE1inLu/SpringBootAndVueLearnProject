package com.testdemo01;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.testdemo01.mapping.SysUserMapper;

@SpringBootTest
public class MybaticplusTest {
    
    @Autowired
    SysUserMapper sysUserMapper;

    @Test
    void test01(){
        Long id = (long) 1;
        System.out.println( sysUserMapper.getNavMenuIds(id));
    }

}
