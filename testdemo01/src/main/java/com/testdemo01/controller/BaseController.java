package com.testdemo01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.testdemo01.config.jwtPropertiesConfig;
import com.testdemo01.service.SysMenuService;
import com.testdemo01.service.SysRoleMenuService;
import com.testdemo01.service.SysRoleService;
import com.testdemo01.service.SysUserRoleService;
import com.testdemo01.service.SysUserService;
import com.testdemo01.util.JwtUtil;
import com.testdemo01.util.RedisUtil;

import jakarta.servlet.http.HttpServletRequest;

public class BaseController {
	
    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtil redisUtil;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	jwtPropertiesConfig jwtPropertiesConfig;

    @Autowired
	SysUserService sysUserService;

	@Autowired
	SysRoleService sysRoleService;

	@Autowired
	SysMenuService sysMenuService;

	@Autowired
	SysUserRoleService sysUserRoleService;

	@Autowired
	SysRoleMenuService sysRoleMenuService;

	/**
	 * 获取页面
	 * @return
	 */
	public Page getPage() {
		int current = ServletRequestUtils.getIntParameter(req, "cuurent", 1);
		int size = ServletRequestUtils.getIntParameter(req, "size", 10);

		return new Page(current, size);
	}

}
