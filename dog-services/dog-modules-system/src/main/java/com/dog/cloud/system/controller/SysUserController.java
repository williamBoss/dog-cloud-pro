package com.dog.cloud.system.controller;

import com.dog.cloud.core.base.controller.BaseController;
import com.dog.cloud.core.constant.UserConstants;
import com.dog.cloud.core.model.entity.system.LoginUser;
import com.dog.cloud.core.model.entity.system.SysRole;
import com.dog.cloud.core.model.entity.system.SysUser;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.result.PageResult;
import com.dog.cloud.core.utils.SecurityUtils;
import com.dog.cloud.core.utils.StringUtils;
import com.dog.cloud.log.annotation.Log;
import com.dog.cloud.log.enums.BusinessType;
import com.dog.cloud.pre.authorize.annotation.PreAuthorize;
import com.dog.cloud.system.service.ISysPermissionService;
import com.dog.cloud.system.service.ISysPostService;
import com.dog.cloud.system.service.ISysRoleService;
import com.dog.cloud.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author KING
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ISysPermissionService permissionService;

    /**
     * 获取用户列表
     */
    @PreAuthorize(hasPermi = "system:user:list")
    @GetMapping("/list")
    public BaseResult<?> list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return PageResult.result(list);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info/{username}")
    public BaseResult<?> info(@PathVariable("username") String username) {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            return BaseResult.failure().message("用户名或密码错误");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser.getUserId());
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser.getUserId());
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return BaseResult.<LoginUser>success().data(sysUserVo);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public BaseResult<?> getInfo() {
        Long userId = SecurityUtils.getUserId();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(userId);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(userId);
        Map<String, Object> data = new HashMap<>(3);
        data.put("user", userService.selectUserById(userId));
        data.put("roles", roles);
        data.put("permissions", permissions);
        return BaseResult.success().data(data);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping(value = {"/", "/{userId}"})
    public BaseResult<?> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        Map<String, Object> data = new HashMap<>();
        List<SysRole> roles = roleService.selectRoleAll();
        data.put("roles",
            SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        data.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            data.put("data", userService.selectUserById(userId));
            data.put("postIds", postService.selectPostListByUserId(userId));
            data.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return BaseResult.success().data(data);
    }

    /**
     * 新增用户
     */
    @PreAuthorize(hasPermi = "system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public BaseResult<?> add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return BaseResult.failure().message("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && UserConstants.NOT_UNIQUE
            .equals(userService.checkPhoneUnique(user))) {
            return BaseResult.failure().message("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && UserConstants.NOT_UNIQUE
            .equals(userService.checkEmailUnique(user))) {
            return BaseResult.failure().message("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return BaseResult.toBaseResult(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult<?> edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && UserConstants.NOT_UNIQUE
            .equals(userService.checkPhoneUnique(user))) {
            return BaseResult.failure().message("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && UserConstants.NOT_UNIQUE
            .equals(userService.checkEmailUnique(user))) {
            return BaseResult.failure().message("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize(hasPermi = "system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public BaseResult<?> remove(@PathVariable Long[] userIds) {
        return BaseResult.toBaseResult(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public BaseResult<?> resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public BaseResult<?> changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(userService.updateUserStatus(user));
    }
}
