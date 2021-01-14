package com.dog.cloud.system.controller;

import com.dog.cloud.core.base.controller.BaseController;
import com.dog.cloud.core.constant.Constants;
import com.dog.cloud.core.constant.UserConstants;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.utils.SecurityUtils;
import com.dog.cloud.core.utils.StringUtils;
import com.dog.cloud.log.annotation.Log;
import com.dog.cloud.log.enums.BusinessType;
import com.dog.cloud.pre.authorize.annotation.PreAuthorize;
import com.dog.cloud.system.domain.SysMenu;
import com.dog.cloud.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 *
 * @author KING
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController {
    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize(hasPermi = "system:menu:list")
    @GetMapping("/list")
    public BaseResult<?> list(SysMenu menu) {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return BaseResult.success().data(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:menu:query")
    @GetMapping(value = "/{menuId}")
    public BaseResult<?> getInfo(@PathVariable Long menuId) {
        return BaseResult.success().data(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public BaseResult<?> treeselect(SysMenu menu) {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return BaseResult.success().data(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public BaseResult<?> roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(userId);
        Map<String, Object> data = new HashMap<>(2);
        data.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        data.put("menus", menuService.buildMenuTreeSelect(menus));
        return BaseResult.success().data(data);
    }

    /**
     * 新增菜单
     */
    @PreAuthorize(hasPermi = "system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public BaseResult<?> add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return BaseResult.failure().message("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils
            .startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            return BaseResult.failure().message("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize(hasPermi = "system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult<?> edit(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return BaseResult.failure().message("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils
            .startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            return BaseResult.failure().message("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return BaseResult.failure().message("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize(hasPermi = "system:menu:remove")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public BaseResult<?> remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return BaseResult.failure().message("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return BaseResult.failure().message("菜单已分配,不允许删除");
        }
        return BaseResult.toBaseResult(menuService.deleteMenuById(menuId));
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public BaseResult<?> getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return BaseResult.success().data(menuService.buildMenus(menus));
    }
}