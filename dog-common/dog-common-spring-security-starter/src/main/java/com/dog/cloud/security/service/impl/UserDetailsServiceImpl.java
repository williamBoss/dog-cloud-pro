package com.dog.cloud.security.service.impl;

import com.dog.cloud.core.constant.Constants;
import com.dog.cloud.core.constant.UserConstants;
import com.dog.cloud.core.constant.enums.UserStatus;
import com.dog.cloud.core.exception.BaseException;
import com.dog.cloud.core.model.entity.system.LoginUser;
import com.dog.cloud.core.model.entity.system.SysUser;
import com.dog.cloud.core.result.BaseResult;
import com.dog.cloud.core.utils.StringUtils;
import com.dog.cloud.system.api.RemoteLogService;
import com.dog.cloud.system.api.RemoteLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 身份认证类
 *
 * @author KING
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Autowired
    private RemoteLoginService remoteLoginService;

    @Autowired
    private RemoteLogService remoteLogService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        // 用户名为空 错误
        if (StringUtils.isBlank(username)) {
            remoteLogService.saveLogininfor(username, Constants.LOGIN_FAIL, "用户名必须填写");
            throw new BaseException("用户名必须填写");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
            || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            remoteLogService.saveLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new BaseException("用户名不在指定范围");
        }
        // 查询用户信息
        final BaseResult<LoginUser> userResult = remoteLoginService.getLoginInfoByUserName(username);
        if (Constants.FAIL.equals(userResult.getCode())) {
            throw new BaseException(userResult.getMsg());
        }
        LoginUser loginUser = userResult.getData();
        SysUser user = userResult.getData().getSysUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            remoteLogService.saveLogininfor(username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            remoteLogService.saveLogininfor(username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new LockedException("对不起，您的账号：" + username + " 已停用");
        }
        return Mono.justOrEmpty(loginUser).switchIfEmpty(Mono.error(() -> {
            remoteLogService.saveLogininfor(username, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        })).doOnNext(u -> log.info(String.format("查询账号成功  user:%s", u.getUsername()))).cast(UserDetails.class);
    }
}
