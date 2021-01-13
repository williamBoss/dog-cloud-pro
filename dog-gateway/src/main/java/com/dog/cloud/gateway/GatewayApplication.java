package com.dog.cloud.gateway;

import com.dog.cloud.core.annotation.DogCloudApplication;
import org.springframework.boot.SpringApplication;

/**
 * Gateway主启动类
 *
 * @author KING
 * @version V1.0
 * @date 2020/7/14 15:52
 */
@DogCloudApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
