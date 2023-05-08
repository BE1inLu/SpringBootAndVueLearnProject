package com.testdemo01.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.testdemo01.common.dto.PassDto;
import com.testdemo01.common.lang.Const;
import com.testdemo01.entity.SysRole;
import com.testdemo01.entity.SysUser;
import com.testdemo01.entity.SysUserRole;
import com.testdemo01.result.result;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author enluba
 * @since 2023-04-09
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public result info(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        Assert.notNull(user, "找不到该管理员！");
        List<SysRole> roles = sysRoleService.listRolesByUserId(user.getId());
        user.setSysRoles(roles);
        return result.succ(user);
    }

    /**
     * 用户自己修改密码
     *
     */
    @PostMapping("/updataPass")
    public result updataPass(@Validated @RequestBody PassDto passDto, Principal principal) {
        SysUser sysUser = sysUserService.getByUsername(principal.getName());
        boolean matches = passwordEncoder.matches(passDto.getCurrentPass(), sysUser.getPassword());
        if (!matches) {
            return result.fail("旧密码不正确！");
        }
        sysUser.setPassword(passwordEncoder.encode(passDto.getPassword()));
        sysUser.setUpdated(LocalDateTime.now());
        sysUserService.updateById(sysUser);
        return result.succ(null);
    }

    /**
    * 超级管理员重置密码
    */
   @PostMapping("/repass")
   @PreAuthorize("hasAuthority('sys:user:repass')")
   public result repass(@RequestBody Long userId) {
      SysUser sysUser = sysUserService.getById(userId);
      sysUser.setPassword(passwordEncoder.encode(Const.DEFAULT_PASSWORD));
      sysUser.setUpdated(LocalDateTime.now());
      sysUserService.updateById(sysUser);
      return result.succ(null);
   }
   
   @GetMapping("/list")
   @PreAuthorize("hasAuthority('sys:user:list')")
   public result page(String username) {
      Page<SysUser> users = sysUserService.page(getPage(),
            new QueryWrapper<SysUser>()
                  .like(StrUtil.isNotBlank(username), "username", username)
      );
      users.getRecords().forEach(u -> {
         u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
      });
      return result.succ(users);
   }
   
   @PostMapping("/save")
   @PreAuthorize("hasAuthority('sys:user:save')")
   public result save(@Validated @RequestBody SysUser sysUser) {
      sysUser.setCreated(LocalDateTime.now());
      sysUser.setStatu(Const.STATUS_ON);
      // 初始默认密码
      sysUser.setPassword(Const.DEFAULT_PASSWORD);
      if (StrUtil.isBlank(sysUser.getPassword())) {
         return result.fail("密码不能为空");
      }
      String password = passwordEncoder.encode(sysUser.getPassword());
      sysUser.setPassword(password);
      // 默认头像
      sysUser.setAvatar(Const.DEFAULT_AVATAR);
      sysUserService.save(sysUser);
      return result.succ(sysUser);
   }
   
   @PostMapping("/update")
   @PreAuthorize("hasAuthority('sys:user:update')")
   public result update(@Validated @RequestBody SysUser sysUser) {
      sysUser.setUpdated(LocalDateTime.now());
      if (StrUtil.isNotBlank(sysUser.getPassword())) {
         String password = passwordEncoder.encode(sysUser.getPassword());
         sysUser.setPassword(password);
      }
      sysUserService.updateById(sysUser);
      return result.succ(sysUser);
   }
   
   @PostMapping("/delete")
   @PreAuthorize("hasAuthority('sys:user:delete')")
   public result delete(@RequestBody Long[] ids){
      sysUserService.removeByIds(Arrays.asList(ids));
      sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));
      return result.succ("");
   }
   
   /**
    * 分配角色
    * @return
    */
   @Transactional
   @PostMapping("/role/{userId}")
   @PreAuthorize("hasAuthority('sys:user:role')")
   public result perm(@PathVariable Long userId, @RequestBody Long[] roleIds) {
      System.out.println(roleIds);
      List<SysUserRole> userRoles = new ArrayList<>();
      Arrays.stream(roleIds).forEach(roleId -> {
         SysUserRole userRole = new SysUserRole();
         userRole.setRoleId(roleId);
         userRole.setUserId(userId);
         userRoles.add(userRole);
      });
      sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
      sysUserRoleService.saveBatch(userRoles);
      // 清除权限信息
      SysUser sysUser = sysUserService.getById(userId);
      sysUserService.clearUserAuthorityInfo(sysUser.getUsername());
      return result.succ(roleIds);
   }
}


