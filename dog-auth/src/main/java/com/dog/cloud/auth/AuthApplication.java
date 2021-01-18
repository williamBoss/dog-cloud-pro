package com.dog.cloud.auth;

import com.dog.cloud.core.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 认证授权中心
 *
 * @author KING
 */
@SpringCloudApplication
@EnableCustomConfig
@EnableFeignClients(basePackages = {"com.dog.cloud.**"})
@ComponentScan(basePackages = {"com.dog.cloud.**"})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
