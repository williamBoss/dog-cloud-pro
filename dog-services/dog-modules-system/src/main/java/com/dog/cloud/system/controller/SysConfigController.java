package com.dog.cloud.system.controller;

import com.dog.cloud.core.base.controller.BaseController;
import com.dog.cloud.core.constant.UserConstants;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.result.PageResult;
import com.dog.cloud.core.utils.SecurityUtils;
import com.dog.cloud.log.annotation.Log;
import com.dog.cloud.log.enums.BusinessType;
import com.dog.cloud.pre.authorize.annotation.PreAuthorize;
import com.dog.cloud.system.domain.SysConfig;
import com.dog.cloud.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author KING
 */
@RestController
@RequestMapping("/config")
public class SysConfigController extends BaseController {
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize(hasPermi = "system:config:list")
    @GetMapping("/list")
    public BaseResult<?> list(SysConfig config) {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return PageResult.result(list);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping(value = "/{configId}")
    public BaseResult<?> getInfo(@PathVariable Long configId) {
        return BaseResult.success().data(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public BaseResult<?> getConfigKey(@PathVariable String configKey) {
        return BaseResult.success().data(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize(hasPermi = "system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public BaseResult<?> add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return BaseResult.failure().message("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getUserId().toString());
        return BaseResult.toBaseResult(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize(hasPermi = "system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult<?> edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return BaseResult.failure().message("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize(hasPermi = "system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public BaseResult<?> remove(@PathVariable Long[] configIds) {
        return BaseResult.toBaseResult(configService.deleteConfigByIds(configIds));
    }

    /**
     * 清空缓存
     */
    @PreAuthorize(hasPermi = "system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clearCache")
    public BaseResult<?> clearCache() {
        configService.clearCache();
        return BaseResult.success();
    }
}
