package com.dog.cloud.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.dog.cloud.core.constant.CacheConstants;
import com.dog.cloud.core.utils.ServletUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * mybatis自动填充
 *
 * @author KING
 */
@Component
public class MyBatisMetaHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.fillStrategy(metaObject, "delFlag", false);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        String userId = Objects.requireNonNull(ServletUtils.getRequest()).getHeader(CacheConstants.DETAILS_USER_ID);
        this.fillStrategy(metaObject, "createBy", userId);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        String userId = Objects.requireNonNull(ServletUtils.getRequest()).getHeader(CacheConstants.DETAILS_USER_ID);
        this.fillStrategy(metaObject, "updateBy", userId);
    }

}
