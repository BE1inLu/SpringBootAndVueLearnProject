package com.testdemo01.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.testdemo01.entity.SysRole;
import com.testdemo01.mapping.SysRoleMapper;
import com.testdemo01.service.SysRoleService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author enluba
 * @since 2023-04-09
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<SysRole> listRolesByUserId(Long userId) {
        List<SysRole> sysRoles = this.list(
                new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id = " + userId));
        return sysRoles;
    }

}
