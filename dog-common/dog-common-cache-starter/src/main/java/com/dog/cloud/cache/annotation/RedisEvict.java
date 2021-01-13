package com.dog.cloud.cache.annotation;

import java.lang.annotation.*;

/**
 * redis删除注解
 *
 * @author KING
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisEvict {

    String key();

    String fieldKey();

}