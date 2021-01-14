package com.dog.cloud.system.controller;

import com.dog.cloud.core.base.controller.BaseController;
import com.dog.cloud.core.constant.UserConstants;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.result.PageResult;
import com.dog.cloud.core.utils.SecurityUtils;
import com.dog.cloud.log.annotation.Log;
import com.dog.cloud.log.enums.BusinessType;
import com.dog.cloud.pre.authorize.annotation.PreAuthorize;
import com.dog.cloud.system.domain.SysPost;
import com.dog.cloud.system.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author KING
 */
@RestController
@RequestMapping("/post")
public class SysPostController extends BaseController {
    @Autowired
    private ISysPostService postService;

    /**
     * 获取岗位列表
     */
    @PreAuthorize(hasPermi = "system:post:list")
    @GetMapping("/list")
    public BaseResult<?> list(SysPost post) {
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return PageResult.result(list);
    }

    /**
     * 根据岗位编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:post:query")
    @GetMapping(value = "/{postId}")
    public BaseResult<?> getInfo(@PathVariable Long postId) {
        return BaseResult.success().data(postService.selectPostById(postId));
    }

    /**
     * 新增岗位
     */
    @PreAuthorize(hasPermi = "system:post:add")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public BaseResult<?> add(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return BaseResult.failure().message("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return BaseResult.failure().message("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(postService.insertPost(post));
    }

    /**
     * 修改岗位
     */
    @PreAuthorize(hasPermi = "system:post:edit")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult<?> edit(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return BaseResult.failure().message("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return BaseResult.failure().message("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setUpdateBy(SecurityUtils.getUsername());
        return BaseResult.toBaseResult(postService.updatePost(post));
    }

    /**
     * 删除岗位
     */
    @PreAuthorize(hasPermi = "system:post:remove")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{postIds}")
    public BaseResult<?> remove(@PathVariable Long[] postIds) {
        return BaseResult.toBaseResult(postService.deletePostByIds(postIds));
    }

    /**
     * 获取岗位选择框列表
     */
    @GetMapping("/optionselect")
    public BaseResult<?> optionselect() {
        List<SysPost> posts = postService.selectPostAll();
        return BaseResult.success().data(posts);
    }
}
