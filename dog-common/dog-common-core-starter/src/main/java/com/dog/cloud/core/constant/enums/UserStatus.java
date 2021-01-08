package com.dog.cloud.core.constant.enums;

/**
 * 用户状态
 *
 * @author KING
 */
public enum UserStatus {

    /**
     * 用户正常
     */
    OK("0" , "正常"),

    /**
     * 用户停用
     */
    DISABLE("1" , "停用"),

    /**
     * 用户删除
     */
    DELETED("2" , "删除");

    /**
     * 状态码
     */
    private final String code;

    /**
     * 状态信息
     */
    private final String info;

    UserStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
