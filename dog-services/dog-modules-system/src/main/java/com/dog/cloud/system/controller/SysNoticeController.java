package com.dog.cloud.system.controller;

import com.dog.cloud.core.base.controller.BaseController;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.result.PageResult;
import com.dog.cloud.core.utils.SecurityUtils;
import com.dog.cloud.log.annotation.Log;
import com.dog.cloud.log.enums.BusinessType;
import com.dog.cloud.pre.authorize.annotation.PreAuthorize;
import com.dog.cloud.system.domain.SysNotice;
import com.dog.cloud.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author KING
 */
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController {
    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize(hasPermi = "system:notice:list")
    @GetMapping("/list")
    public BaseResult<?> list(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return PageResult.result(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:notice:query")
    @GetMapping(value = "/{noticeId}")
    public BaseResult<?> getInfo(@PathVariable Long noticeId) {
        return BaseResult.success().data(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize(hasPermi = "system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping
    public BaseResult<?> add(@Validated @RequestBody SysNotice notice) {
        notice.setCreateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize(hasPermi = "system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult<?> edit(@Validated @RequestBody SysNotice notice) {
        notice.setUpdateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize(hasPermi = "system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public BaseResult<?> remove(@PathVariable Long[] noticeIds) {
        return BaseResult.toBaseResult(noticeService.deleteNoticeByIds(noticeIds));
    }
}
