package com.dog.cloud.core.exception.user;

import com.dog.cloud.core.exception.BaseException;

/**
 * 用户信息异常类
 *
 * @author KING
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}