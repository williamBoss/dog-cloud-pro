package com.dog.cloud.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * jackson序列化，该字段null时不加入自定义序列化
 *
 * @author KING
 * @version V1.0
 * @date 2021/3/19 17:37
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullNotJoinToCustomizeJsonSerializer {
}
