package com.dog.cloud.auth;

import com.dog.cloud.core.annotation.DogCloudApplication;
import com.dog.cloud.core.annotation.EnableDogFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 认证授权中心
 *
 * @author KING
 */
@DogCloudApplication
@EnableDogFeignClients
@ComponentScan(basePackages = {"com.dog.cloud.**"})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
