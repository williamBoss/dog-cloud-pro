package com.dog.cloud.system.controller;

import com.dog.cloud.core.base.controller.BaseController;
import com.dog.cloud.core.constant.UserConstants;
import com.dog.cloud.core.model.entity.system.SysRole;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.result.PageResult;
import com.dog.cloud.core.utils.SecurityUtils;
import com.dog.cloud.log.annotation.Log;
import com.dog.cloud.log.enums.BusinessType;
import com.dog.cloud.pre.authorize.annotation.PreAuthorize;
import com.dog.cloud.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息
 *
 * @author KING
 */
@RestController
@RequestMapping("/role")
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    @PreAuthorize(hasPermi = "system:role:list")
    @GetMapping("/list")
    public BaseResult<?> list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return PageResult.result(list);
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:role:query")
    @GetMapping(value = "/{roleId}")
    public BaseResult<?> getInfo(@PathVariable Long roleId) {
        return BaseResult.success().data(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize(hasPermi = "system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public BaseResult<?> add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return BaseResult.failure().message("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return BaseResult.failure().message("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @PreAuthorize(hasPermi = "system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult<?> edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return BaseResult.failure().message("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return BaseResult.failure().message("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(roleService.updateRole(role));
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize(hasPermi = "system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public BaseResult<?> dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        return BaseResult.toBaseResult(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public BaseResult<?> changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @PreAuthorize(hasPermi = "system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public BaseResult<?> remove(@PathVariable Long[] roleIds) {
        return BaseResult.toBaseResult(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @PreAuthorize(hasPermi = "system:role:query")
    @GetMapping("/optionselect")
    public BaseResult<?> optionselect() {
        return BaseResult.success().data(roleService.selectRoleAll());
    }
}