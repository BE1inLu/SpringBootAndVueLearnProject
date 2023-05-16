package com.testdemo01.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.testdemo01.common.lang.Const;
import com.testdemo01.entity.SysMenu;
import com.testdemo01.entity.SysRoleMenu;
import com.testdemo01.entity.SysUser;
import com.testdemo01.result.result;

import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author enluba
 * @since 2023-04-09
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

    /**
     * @param principal
     * @return
     */
    @GetMapping("/nav")
    public result nav(Principal principal) {
        String username = principal.getName();
        SysUser sysUser = sysUserService.getByUsername(username);
        String[] authoritys = StringUtils.tokenizeToStringArray(sysUserService.getUserAuthorityInfo(sysUser.getId()),
                ",");

        result succ = result.succ(
                MapUtil.builder()
                        .put("nav", sysMenuService.getcurrentUserNav())
                        .put("authoritys", authoritys)
                        .map());

        return succ;
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public result info(@PathVariable("id") Long id) {
        return result.succ(sysMenuService.getById(id));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public result list() {
        List<SysMenu> menus = sysMenuService.tree();
        return result.succ(menus);
    }

    /**
     * @param sysMenu
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public result save(@Validated @RequestBody SysMenu sysMenu) {
        sysMenu.setCreated(LocalDateTime.now());
        sysMenu.setStatu(Const.STATUS_ON);
        sysMenuService.save(sysMenu);
        return result.succ(sysMenu);
    }

    /**
     * @param sysMenu
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public result update(@Validated @RequestBody SysMenu sysMenu) {
        sysMenu.setUpdated(LocalDateTime.now());
        sysMenuService.updateById(sysMenu);
        // 清除所有与该菜单相关的权限缓存
        sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());
        return result.succ(sysMenu);
    }

    /**
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('sys:menu:delete')")
	public result delete(@PathVariable("id") Long id) {

		int count = (int) sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id", id));
		if (count > 0) {
			return result.fail("请先删除子菜单");
		}

		// 清除所有与该菜单相关的权限缓存
		sysUserService.clearUserAuthorityInfoByMenuId(id);

		sysMenuService.removeById(id);

		// 同步删除中间关联表
		sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id", id));
		return result.succ("");
	}

}
