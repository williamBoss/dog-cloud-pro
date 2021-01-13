package com.dog.cloud.auth.controller;

import com.dog.cloud.auth.form.LoginBody;
import com.dog.cloud.auth.service.SysLoginService;
import com.dog.cloud.core.model.entity.system.LoginUser;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.utils.StringUtils;
import com.dog.cloud.pre.authorize.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * token 控制
 *
 * @author ruoyi
 */
@RestController
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public BaseResult<?> login(@RequestBody LoginBody form) {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        // 获取登录token
        return BaseResult.success().data(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
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
