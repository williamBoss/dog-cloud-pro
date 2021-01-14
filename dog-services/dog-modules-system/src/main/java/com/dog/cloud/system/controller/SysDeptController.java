package com.dog.cloud.system.controller;

import com.dog.cloud.core.base.controller.BaseController;
import com.dog.cloud.core.constant.UserConstants;
import com.dog.cloud.core.model.entity.system.SysDept;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.utils.SecurityUtils;
import com.dog.cloud.core.utils.StringUtils;
import com.dog.cloud.log.annotation.Log;
import com.dog.cloud.log.enums.BusinessType;
import com.dog.cloud.pre.authorize.annotation.PreAuthorize;
import com.dog.cloud.system.service.ISysDeptService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 部门信息
 *
 * @author KING
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize(hasPermi = "system:dept:list")
    @GetMapping("/list")
    public BaseResult<?> list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return BaseResult.success().data(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize(hasPermi = "system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    public BaseResult<?> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept)it.next();
            if (d.getDeptId().intValue() == deptId || ArrayUtils
                .contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return BaseResult.success().data(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:dept:query")
    @GetMapping(value = "/{deptId}")
    public BaseResult<?> getInfo(@PathVariable Long deptId) {
        return BaseResult.success().data(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public BaseResult<?> treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return BaseResult.success().data(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public BaseResult<?> roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Map<String, Object> data = new HashMap<>();
        data.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        data.put("depts", deptService.buildDeptTreeSelect(depts));
        return BaseResult.success().data(data);
    }

    /**
     * 新增部门
     */
    @PreAuthorize(hasPermi = "system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public BaseResult<?> add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return BaseResult.failure().message("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize(hasPermi = "system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult<?> edit(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return BaseResult.failure().message("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return BaseResult.failure().message("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
            && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {
            return BaseResult.failure().message("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize(hasPermi = "system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public BaseResult<?> remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return BaseResult.failure().message("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return BaseResult.failure().message("部门存在用户,不允许删除");
        }
        return BaseResult.toBaseResult(deptService.deleteDeptById(deptId));
    }
}
