package com.dog.cloud.auth;

import com.dog.cloud.core.annotation.EnableDogFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 认证授权中心
 *
 * @author KING
 */
@SpringCloudApplication
@ComponentScan(basePackages = {"com.dog.cloud.**"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDogFeignClients
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
