package com.dog.cloud.core.constant.enums;

import com.dog.cloud.core.constant.ResponseCodeConstants;

/**
 * @author KING
 * @version V1.0
 * @Title: ResponseStatus
 * @Package com.cloud.bronze.common.result
 * @Description: response响应状态常枚举常量
 * @date 2020/7/15 11:56
 */
public enum ResponseStatusEnum {

    /**
     * Success
     * 成功
     */
    SUCCESS(ResponseCodeConstants.SUCCESS, "00000", "Success", "成功"),

    /**
     * Failure
     * 失败
     */
    FAILURE(ResponseCodeConstants.FAILURE, "A0500", "Failure", "失败"),

    /**
     * Bad Request
     * 请求错误
     */
    BAD_REQUEST(ResponseCodeConstants.BAD_REQUEST, "A0400", "Bad Request", "请求错误"),

    /**
     * Unauthorized
     * 未认证
     */
    UNAUTHORIZED(ResponseCodeConstants.UNAUTHORIZED, "A0401", "Unauthorized", "访问权限异常"),

    /**
     * Forbidden
     * 无权限
     */
    FORBIDDEN(ResponseCodeConstants.FORBIDDEN, "A0403", "Forbidden", "访问未授权"),

    /**
     * Not Found
     * 请求不存在
     */
    NOT_FOUND(ResponseCodeConstants.NOT_FOUND, "A0400", "Not Found", "请求不存在"),

    /**
     * Method Not Allowed
     * 方法不允许
     */
    METHOD_NOT_ALLOWED(ResponseCodeConstants.METHOD_NOT_ALLOWED, "A0405", "Method Not Allowed", "方法不允许"),

    /**
     * Request Timeout
     * 请求超时
     */
    REQUEST_TIMEOUT(ResponseCodeConstants.REQUEST_TIMEOUT, "A0408", "Request Timeout", "请求超时"),

    /**
     * Too Many Requests
     * 请求太多
     */
    TOO_MANY_REQUESTS(ResponseCodeConstants.TOO_MANY_REQUESTS, "A0429", "Too Many Requests", "请求太多");

    /**
     * httpCode
     */
    private final int code;

    /**
     * 用户提示信息
     */
    private final String msg;

    /**
     * errorCode
     */
    private final String errorCode;

    /**
     * 简要描述后端出错原因
     */
    private final String errorMessage;

    ResponseStatusEnum(int code, String errorCode, String errorMessage, String msg) {
        this.code = code;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.msg = msg;
    }

    public int code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    public String errorCode() {
        return this.errorCode;
    }

    public String errorMessage() {
        return this.errorMessage;
    }

}
