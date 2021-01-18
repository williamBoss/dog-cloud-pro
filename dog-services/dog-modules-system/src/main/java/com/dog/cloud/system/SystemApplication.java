package com.dog.cloud.system;

import com.dog.cloud.core.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 系统模块
 *
 * @author KING
 */
@SpringCloudApplication
@EnableCustomConfig
@EnableFeignClients(basePackages = {"com.dog.cloud.**"})
@ComponentScan(basePackages = {"com.dog.cloud.**"})
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
