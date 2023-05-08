package com.testdemo01;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.testdemo01.util.JwtUtil;

// import com.testdemo01.service.SysUserService;

@SpringBootTest
class Testdemo01ApplicationTests {

	@Autowired
	JwtUtil jwtUtil;

	@Test
	void contextLoads() {
		// System.out.println("Testdemo01ApplicationTests.contextLoads()");
		// System.out.println(jwtUtil.getHeader());
	}

}
