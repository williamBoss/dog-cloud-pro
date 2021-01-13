package com.dog.cloud.core.annotation;

import com.dog.cloud.core.config.feign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自定义Feign加载注解
 *
 * @author KING
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
@Import({FeignAutoConfiguration.class})
public @interface EnableDogFeignClients {
    String[] value() default {};

    String[] basePackages() default {"com.dog.cloud.**"};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};
}
