package com.dog.cloud.system.api;

import com.dog.cloud.core.constant.ServiceNameConstants;
import com.dog.cloud.core.model.entity.system.LoginUser;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.system.api.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 结果
     */
    @GetMapping(value = "/user/info/{username}")
    public BaseResult<LoginUser> getUserInfo(@PathVariable("username") String username);
}
