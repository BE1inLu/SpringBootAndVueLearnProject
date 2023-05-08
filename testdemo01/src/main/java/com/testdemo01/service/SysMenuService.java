package com.testdemo01.service;

import com.testdemo01.common.dto.SysMenuDto;
import com.testdemo01.entity.SysMenu;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author enluba
 * @since 2023-04-09
 */
public interface SysMenuService extends IService<SysMenu> {


    /**
     * 获取当前用户菜单导航
     * @return
     */
    List<SysMenuDto> getcurrentUserNav();


    /**
     * <p>获取所有菜单信息</p>
     * <p>并且转成树状结构</p>
     * @return
     */
    List<SysMenu> tree();
}
