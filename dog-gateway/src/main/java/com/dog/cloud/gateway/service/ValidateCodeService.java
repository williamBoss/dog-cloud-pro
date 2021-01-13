package com.dog.cloud.gateway.service;

import com.dog.cloud.core.exception.CaptchaException;
import com.dog.cloud.core.result.BaseResult;

import java.io.IOException;

/**
 * 验证码处理
 *
 * @author KING
 */
public interface ValidateCodeService {

    /**
     * 生成验证码
     */
    public BaseResult createCapcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     */
    public void checkCapcha(String key, String value) throws CaptchaException;

}
