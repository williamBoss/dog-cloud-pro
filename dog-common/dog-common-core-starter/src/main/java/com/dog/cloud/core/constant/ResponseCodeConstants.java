package com.dog.cloud.core.constant;

/**
 * @author KING
 * @version V1.0
 * @Title: ResponseCode
 * @Package com.cloud.bronze.common.result
 * @Description: Response请求响应码
 * @date 2020/7/15 11:52
 */
public interface ResponseCodeConstants {

    /**
     * Success
     * 成功
     */
    int SUCCESS = 200;

    /**
     * Erroe
     * 错误
     */
    int ERROR = 201;

    /**
     * Failure
     * 失败
     */
    int FAILURE = 500;

    /**
     * Bad Request
     * 请求错误
     */
    int BAD_REQUEST = 400;

    /**
     * Unauthorized
     * 未认证
     */
    int UNAUTHORIZED = 401;

    /**
     * Forbidden
     * 无权限
     */
    int FORBIDDEN = 403;

    /**
     * Not Found
     * 请求不存在
     */
    int NOT_FOUND = 404;

    /**
     * Method Not Allowed
     * 方法不允许
     */
    int METHOD_NOT_ALLOWED = 405;

    /**
     * Request Timeout
     * 请求超时
     */
    int REQUEST_TIMEOUT = 408;

    /**
     * Too Many Requests
     * 请求太多
     */
    int TOO_MANY_REQUESTS = 429;
}
