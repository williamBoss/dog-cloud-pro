package com.dog.cloud.system.utils;

import com.dog.cloud.cache.utils.RedisUtils;
import com.dog.cloud.core.constant.Constants;
import com.dog.cloud.core.utils.StringUtils;
import com.dog.cloud.core.utils.spring.SpringUtils;
import com.dog.cloud.system.domain.SysDictData;

import java.util.Collection;
import java.util.List;

/**
 * 字典工具类
 *
 * @author KING
 */
public class DictUtils {
    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas) {
        SpringUtils.getBean(RedisUtils.class).set(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysDictData> getDictCache(String key) {
        Object cacheObj = SpringUtils.getBean(RedisUtils.class).get(getCacheKey(key));
        if (StringUtils.isNotNull(cacheObj)) {
            List<SysDictData> dictDatas = StringUtils.cast(cacheObj);
            return dictDatas;
        }
        return null;
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        Collection<String> keys = SpringUtils.getBean(RedisUtils.class).keys(Constants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(RedisUtils.class).deleteObjects(keys);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return Constants.SYS_DICT_KEY + configKey;
    }
}
