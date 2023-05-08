package com.testdemo01.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.testdemo01.entity.SysRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author enluba
 * @since 2023-04-09
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * @param userId
     * @return
     */
    List<SysRole> listRolesByUserId(Long userId);
}
