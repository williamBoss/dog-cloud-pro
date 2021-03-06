package com.dog.cloud.auth.controller;

import com.dog.cloud.auth.form.LoginBody;
import com.dog.cloud.auth.service.SysLoginService;
import com.dog.cloud.core.model.entity.system.LoginUser;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.utils.StringUtils;
import com.dog.cloud.pre.authorize.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * token 控制
 *
 * @author KING
 */
@Slf4j
@RestController
@Api(tags = "token控制")
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    @ApiOperation(value = "登录")
    public BaseResult<?> login(@RequestBody LoginBody form) {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        Map<String, Object> token = tokenService.createToken(userInfo);
        return BaseResult.success().data(token);
    }

    @DeleteMapping("logout")
    @ApiOperation(value = "登出")
    public BaseResult<?> logout(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String username = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            sysLoginService.logout(username);
        }
        return BaseResult.success();
    }

    @PostMapping("refresh")
    @ApiOperation(value = "刷新token")
    public BaseResult<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return BaseResult.success();
        }
        return BaseResult.success();
    }
}
