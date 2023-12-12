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
import java.util.function.Supplier;

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
        return getCusCacheObject(cacheType.getCode(), cacheType.getIsTenant(), cacheType.getConsumer());
    }

    /**
     * 获取指定缓存数据对象 | 自定义
     *
     * @param cacheCode 缓存编码
     * @param isTenant  租户级缓存
     * @param consumer  缓存更新方法
     * @return 数据对象
     */
    public <T> T getCusCacheObject(String cacheCode, Boolean isTenant, Supplier<Object> consumer) {
        String key = CacheConstants.CacheType.getCusCacheKey(cacheCode, isTenant);
        T object = redisService.getCacheObject(key);
        if (ObjectUtil.isNull(object)) {
            refreshCusCache(consumer);
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
        return getCusCacheSet(cacheType.getCode(), cacheType.getIsTenant(), cacheType.getConsumer());
    }

    /**
     * 获取指定缓存数据对象 | 自定义
     *
     * @param cacheCode 缓存编码
     * @param isTenant  租户级缓存
     * @param consumer  缓存更新方法
     * @return 数据对象
     */
    public <T> Set<T> getCusCacheSet(String cacheCode, Boolean isTenant, Supplier<Object> consumer) {
        String key = CacheConstants.CacheType.getCusCacheKey(cacheCode, isTenant);
        Set<T> cacheSet = redisService.getCacheSet(key);
        if (ObjectUtil.isNull(cacheSet)) {
            refreshCusCache(consumer);
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
        return getCusCacheList(cacheType.getCode(), cacheType.getIsTenant(), cacheType.getConsumer());
    }

    /**
     * 获取指定缓存数据对象 | 自定义
     *
     * @param cacheCode 缓存编码
     * @param isTenant  租户级缓存
     * @param consumer  缓存更新方法
     * @return 数据对象
     */
    public <T> List<T> getCusCacheList(String cacheCode, Boolean isTenant, Supplier<Object> consumer) {
        String key = CacheConstants.CacheType.getCusCacheKey(cacheCode, isTenant);
        List<T> cacheSet = redisService.getCacheList(key);
        if (ObjectUtil.isNull(cacheSet)) {
            refreshCusCache(consumer);
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
        return getCusCacheMap(cacheType.getCode(), cacheType.getIsTenant(), cacheType.getConsumer());
    }

    /**
     * 获取指定缓存数据对象 | 自定义
     *
     * @param cacheCode 缓存编码
     * @param isTenant  租户级缓存
     * @param consumer  缓存更新方法
     * @return 数据对象
     */
    public <T> Map<String, T> getCusCacheMap(String cacheCode, Boolean isTenant, Supplier<Object> consumer) {
        String key = CacheConstants.CacheType.getCusCacheKey(cacheCode, isTenant);
        Map<String, T> map = redisService.getCacheMap(key);
        if (MapUtil.isEmpty(map)) {
            refreshCusCache(consumer);
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
        return getCusCacheObject(cacheType.getCode(), cacheType.getIsTenant(), cacheType.getConsumer(), code);
    }

    /**
     * 获取指定缓存数据对象 | 自定义
     *
     * @param cacheCode 缓存编码
     * @param isTenant  租户级缓存
     * @param consumer  缓存更新方法
     * @param code      缓存编码
     * @return 数据对象
     */
    public <T> T getCusCacheObject(String cacheCode, Boolean isTenant, Supplier<Object> consumer, String code) {
        String key = CacheConstants.CacheType.getCusCacheKey(cacheCode, isTenant);
        T object = redisService.getCacheMapValue(key, code);
        if (ObjectUtil.isNull(object)) {
            refreshCusCache(consumer);
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
        refreshCusCache(cacheType.getConsumer());
    }

    /**
     * 更新指定类型的缓存对象 | 自定义
     *
     * @param consumer 缓存更新方法
     */
    public void refreshCusCache(Supplier<Object> consumer) {
        consumer.get();
    }
}
