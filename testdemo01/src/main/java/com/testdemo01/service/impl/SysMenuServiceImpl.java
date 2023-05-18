package com.testdemo01.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.testdemo01.common.dto.SysMenuDto;
import com.testdemo01.entity.SysMenu;
import com.testdemo01.entity.SysUser;
import com.testdemo01.mapping.SysMenuMapper;
import com.testdemo01.mapping.SysUserMapper;
import com.testdemo01.service.SysMenuService;
import com.testdemo01.service.SysUserService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author enluba
 * @since 2023-04-09
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysUserService sysUserService;

    @Override
    public List<SysMenuDto> getcurrentUserNav() {
        // 通过 security 获取用户名
        // String username =
        // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        SysUser sysUser = sysUserService.getByUsername(username);
        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        List<SysMenu> menus = buildTreeMenu(this.listByIds(menuIds));
        return convert(menus);

    }

    /**
     * <p>
     * list转树形结构
     * </p>
     * <p>
     * 例：系统管理 - 菜单管理 - 添加菜单
     * </p>
     * 
     * @param listByIds
     * @return
     */
    private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {
        List<SysMenu> finalmenus = new ArrayList<>();
        for (SysMenu menu : menus) {
            // 先遍历主title下的子title
            for (SysMenu e : menus) {
                if (e.getParentId() == menu.getId()) {
                    menu.getChildren().add(e);
                }
            }
            // 提取父节点 一级菜单为0
            if (menu.getParentId() == 0L) {
                finalmenus.add(menu);
            }
        }

        return finalmenus;
    }

    /**
     * menu 转 menudto
     * 
     * @param menus
     * @return
     */
    private List<SysMenuDto> convert(List<SysMenu> menus) {
        List<SysMenuDto> menuDtos = new ArrayList<>();

        menus.forEach(
                m -> {
                    SysMenuDto dto = new SysMenuDto();
                    dto.setId(m.getId());
                    dto.setName(m.getPerms());
                    dto.setTitle(m.getName());
                    dto.setIcon(m.getIcon());
                    dto.setPath(m.getPath());
                    dto.setComponent(m.getComponent());
                    // 如果 child 不为 0 ，则继续执行 convert 读取子 child 下的标题信息
                    if (m.getChildren().size() > 0) {
                        dto.setChildren(convert(m.getChildren()));
                    }

                    menuDtos.add(dto);
                });
        return menuDtos;
    }

    @Override
    public List<SysMenu> tree() {
        // 获取所有菜单信息
        List<SysMenu> sysMenus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));

        // 转成树状结构
        return buildTreeMenu(sysMenus);
    }

}
