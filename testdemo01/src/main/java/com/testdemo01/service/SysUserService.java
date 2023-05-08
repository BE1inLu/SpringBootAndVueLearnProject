package com.testdemo01.service;

import com.testdemo01.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author enluba
 * @since 2023-04-09
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户信息获取
     * 
     * @param username
     * @return
     */
    SysUser getByUsername(String username);

    /**
     * 权限校验
     * 
     * @param userId
     * @return
     */
    String getUserAuthorityInfo(Long userId);

    /**
     * 删除某个用户权限信息
     * 
     * @param username
     */
    void clearUserAuthorityInfo(String username);

    /**
     * 删除所有与该角色关联的用户的权限信息
     * 
     * @param roleId
     */
    void clearUserAuthorityInfoByRoleId(Long roleId);

    /**
     * 删除所有与该菜单关联的所有用户的权限信息
     * 
     * @param menuId
     */
    void clearUserAuthorityInfoByMenuId(Long menuId);

}
