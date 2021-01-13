package com.dog.cloud.core.annotation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * 自定义SpringCloudApplication加载注解
 *
 * @author KING
 * @version V1.0
 * @date 2021/1/12 21:08
 */
@SpringCloudApplication
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public @interface DogCloudApplication {
    String[] basePackages() default {"com.dog.cloud.**"};
}
