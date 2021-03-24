package com.dog.cloud.core.annotation;

import com.dog.cloud.core.config.feign.FeignAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.*;

/**
 * EnableAspectJAutoProxy(exposeProxy=true) 表示通过aop框架暴露该代理对象,AopContext能够访问
 * EnableAsync 开启线程异步执行
 * Import({FeignAutoConfiguration.class}) 自动加载类
 *
 * @author KING
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableAsync
@Import({FeignAutoConfiguration.class})
public @interface EnableCustomConfig {

}
