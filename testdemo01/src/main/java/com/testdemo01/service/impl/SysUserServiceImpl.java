package com.testdemo01.service.impl;

import com.testdemo01.entity.SysMenu;
import com.testdemo01.entity.SysRole;
import com.testdemo01.entity.SysUser;
import com.testdemo01.mapping.SysUserMapper;
import com.testdemo01.service.SysMenuService;
import com.testdemo01.service.SysRoleService;
import com.testdemo01.service.SysUserService;
import com.testdemo01.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author enluba
 * @since 2023-04-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    @Lazy
    SysMenuService sysMenuService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public SysUser getByUsername(String username) {
        // 获取用户是否存在
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {

        // 进入 getUserAuthorityInfo
        System.out.println("====进入 getUserAuthorityInfo ====");

        SysUser sysUser = this.getById(userId);

        String authority = "";

        if (redisUtil.hasKey("GrantedAuthority:" + sysUser.getUsername())) {
            // 从缓存获取用户权限
            System.out.println("缓存获取用户权限信息");
            authority = (String) redisUtil.get("GrantedAuthority:" + sysUser.getUsername());
            System.out.println("authotity: "+authority);

        } else {

            // 获取角色
            List<SysRole> roles = sysRoleService.list(
                    new QueryWrapper<SysRole>().inSql("id",
                            "select role_id from sys_user_role where user_id = " + userId));
            if (roles.size() > 0) {
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }
            // 获取菜单操作编码
            List<Long> menuIds = sysUserMapper.getNavMenuIds(userId);
            // 这个是通过用户 id 获取的 menuid
            // 下一步就要通过 menuid 来获取 menu 权限
            System.out.println("menuIds: " + menuIds);
            try {
                // 通过menuId来获取menu权限
                List<SysMenu> menus = sysMenuService.listByIds(menuIds);
                System.out.println("menus: " + menus);
                if (menuIds.size() > 0) {
                    String menuPerms = menus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
                    authority = authority.concat(menuPerms);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("用户ID - {" + userId + "} ---拥有的权限：{" + authority + "}");

            // 添加到缓存
            System.out.println("用户权限已添加到redis");
            redisUtil.set("GrantedAuthority:" + sysUser.getUsername(), authority, 60 * 60);

        }

        // 离开 getUserAuthorityInfo
        System.out.println("====离开 getUserAuthorityInfo ====");

        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority:" + username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>()
                .inSql("id", "select user_id from sys_user_role where role_id = " + roleId));
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<SysUser> sysUsers = sysUserMapper.listByMenuId(menuId);
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

}
