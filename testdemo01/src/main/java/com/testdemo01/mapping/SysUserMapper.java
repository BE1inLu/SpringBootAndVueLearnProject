package com.testdemo01.mapping;

import com.testdemo01.entity.SysUser;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author enluba
 * @since 2023-04-09
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 
     * @param userId
     * @return
     */
    @Select("SELECT DISTINCT rm.menu_id FROM sys_user_role ur LEFT JOIN sys_role_menu rm ON ur.role_id = rm.role_id WHERE ur.user_id=#{ew.userId}")
    List<Long> getNavMenuIds(@Param(Constants.WRAPPER) Long userId);

    @Select("SELECT DISTINCT su.* FROM sys_user_role ur LEFT JOIN `sys_role_menu` rm ON rm.role_id = ur.role_id LEFT JOIN `sys_user` su ON su.id = ur.user_id WHERE rm.menu_id = #{menuId}")
    List<SysUser> listByMenuId(Long i);

}
