package com.xueyi.common.cache.service;

import cn.hutool.core.map.MapUtil;
import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存管理服务
 *
 * @author xueyi
 **/
@Component
public class CacheService {

    @Autowired
    private RedisService redisService;

    /**
     * 获取指定缓存数据对象
     *
     * @param cacheType 缓存枚举
     * @return 数据对象
     */
    public <T> T getCacheObject(CacheConstants.CacheType cacheType) {
        String key = cacheType.getCacheKey();
        T object = redisService.getCacheObject(key);
        if (ObjectUtil.isNull(object)) {
            refreshCache(cacheType);
            return redisService.getCacheObject(key);
        }
        return object;
    }

    /**
     * 获取指定缓存数据对象
     *
     * @param cacheType 缓存枚举
     * @return 数据对象
     */
    public <T> Set<T> getCacheSet(CacheConstants.CacheType cacheType) {
        String key = cacheType.getCacheKey();
        Set<T> cacheSet = redisService.getCacheSet(key);
        if (ObjectUtil.isNull(cacheSet)) {
            refreshCache(cacheType);
            return redisService.getCacheSet(key);
        }
        return cacheSet;
    }

    /**
     * 获取指定缓存数据对象
     *
     * @param cacheType 缓存枚举
     * @return 数据对象
     */
    public <T> List<T> getCacheList(CacheConstants.CacheType cacheType) {
        String key = cacheType.getCacheKey();
        List<T> cacheSet = redisService.getCacheList(key);
        if (ObjectUtil.isNull(cacheSet)) {
            refreshCache(cacheType);
            return redisService.getCacheList(key);
        }
        return cacheSet;
    }

    /**
     * 获取指定缓存数据对象
     *
     * @param cacheType 缓存枚举
     * @return 数据对象
     */
    public <T> Map<String, T> getCacheMap(CacheConstants.CacheType cacheType) {
        String key = cacheType.getCacheKey();
        Map<String, T> map = redisService.getCacheMap(key);
        if (MapUtil.isEmpty(map)) {
            refreshCache(cacheType);
            return redisService.getCacheMap(key);
        }
        return map;
    }

    /**
     * 获取指定缓存数据对象
     *
     * @param cacheType 缓存枚举
     * @param code      缓存编码
     * @return 数据对象
     */
    public <T> T getCacheObject(CacheConstants.CacheType cacheType, String code) {
        String key = cacheType.getCacheKey();
        T object = redisService.getCacheMapValue(key, code);
        if (ObjectUtil.isNull(object)) {
            refreshCache(cacheType);
            return redisService.getCacheMapValue(key, code);
        }
        return object;
    }

    /**
     * 更新指定类型的缓存对象
     *
     * @param cacheType 缓存枚举
     */
    public void refreshCache(CacheConstants.CacheType cacheType) {
        cacheType.getConsumer().get();
    }
}
