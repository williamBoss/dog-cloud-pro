package com.dog.cloud.system.domain;

import lombok.Data;

/**
 * 用户和岗位关联 sys_user_post
 *
 * @author KING
 */
@Data
public class SysUserPost {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;

}
