package com.dog.cloud.system.controller;

import com.dog.cloud.core.base.controller.BaseController;
import com.dog.cloud.core.model.entity.system.SysOperLog;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.result.PageResult;
import com.dog.cloud.log.annotation.Log;
import com.dog.cloud.log.enums.BusinessType;
import com.dog.cloud.pre.authorize.annotation.PreAuthorize;
import com.dog.cloud.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author KING
 */
@RestController
@RequestMapping("/operlog")
public class SysOperlogController extends BaseController {
    @Autowired
    private ISysOperLogService operLogService;

    @PreAuthorize(hasPermi = "system:operlog:list")
    @GetMapping("/list")
    public BaseResult<?> list(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return PageResult.result(list);
    }

    @PreAuthorize(hasPermi = "system:operlog:remove")
    @DeleteMapping("/{operIds}")
    public BaseResult<?> remove(@PathVariable Long[] operIds) {
        return BaseResult.toBaseResult(operLogService.deleteOperLogByIds(operIds));
    }

    @PreAuthorize(hasPermi = "system:operlog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public BaseResult<?> clean() {
        operLogService.cleanOperLog();
        return BaseResult.success();
    }

    @PostMapping
    public BaseResult<?> add(@RequestBody SysOperLog operLog) {
        return BaseResult.toBaseResult(operLogService.insertOperlog(operLog));
    }
}
