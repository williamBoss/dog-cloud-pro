package com.dog.cloud.core.utils;

/**
 * @author KING
 * @version V1.0
 * @Title: ArrayUtils
 * @Package com.base.pro.core.utils
 * @Description: 数组工具类
 * @date 2020/7/29 11:07
 */
public class ArrayUtils {

    /**
     * 数组是否为非空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为非空
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean isNotEmpty(final T... array) {
        return (array != null && array.length != 0);
    }

}
