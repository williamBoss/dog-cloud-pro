package com.dog.cloud.system.api;

import com.dog.cloud.core.constant.ServiceNameConstants;
import com.dog.cloud.core.model.entity.system.LoginUser;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.system.api.factory.RemoteLoginFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author KING
 */
@FeignClient(contextId = "remoteLoginService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteLoginFallbackFactory.class)
public interface RemoteLoginService {

    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 结果
     */
    @GetMapping(value = "/login/info/{username}")
    public BaseResult<LoginUser> getLoginInfoByUserName(@PathVariable("username") String username);

}
