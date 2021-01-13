package com.dog.cloud.system.api.factory;

import com.dog.cloud.core.model.entity.system.LoginUser;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.system.api.RemoteLoginService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 登录服务
 *
 * @author KING
 */
@Slf4j
@Component
public class RemoteLoginFallbackFactory implements FallbackFactory<RemoteLoginService> {
    @Override
    public RemoteLoginService create(Throwable throwable) {
        log.error("登录服务调用失败:{}", throwable.getMessage());
        return new RemoteLoginService() {
            @Override
            public BaseResult<LoginUser> getLoginInfoByUserName(String username) {
                return null;
            }
        };
    }
}
