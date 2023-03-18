package com.xueyi.common.cache.service;

import cn.hutool.core.map.MapUtil;
import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.system.api.dict.feign.RemoteConfigService;
import com.xueyi.system.api.dict.feign.RemoteDictService;
import com.xueyi.tenant.api.source.feign.RemoteSourceService;
import com.xueyi.tenant.api.tenant.feign.RemoteStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
        T object = redisService.getCacheObject(cacheType.getCode());
        if (ObjectUtil.isNull(object)) {
            refreshCache(cacheType);
            return redisService.getCacheObject(cacheType.getCode());
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
        Set<T> cacheSet = redisService.getCacheSet(cacheType.getCode());
        if (ObjectUtil.isNull(cacheSet)) {
            refreshCache(cacheType);
            return redisService.getCacheSet(cacheType.getCode());
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
        List<T> cacheSet = redisService.getCacheList(cacheType.getCode());
        if (ObjectUtil.isNull(cacheSet)) {
            refreshCache(cacheType);
            return redisService.getCacheList(cacheType.getCode());
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
        Map<String, T> map = redisService.getCacheMap(cacheType.getCode());
        if (MapUtil.isEmpty(map)) {
            refreshCache(cacheType);
            return redisService.getCacheMap(cacheType.getCode());
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
        T object = redisService.getCacheMapValue(cacheType.getCode(), code);
        if (ObjectUtil.isNull(object)) {
            refreshCache(cacheType);
            return redisService.getCacheMapValue(cacheType.getCode(), code);
        }
        return object;
    }

    /**
     * 获取指定缓存数据对象
     *
     * @param cacheType 缓存枚举
     * @param code      缓存编码
     * @return 数据对象
     */
    public <T> List<T> getMultiCacheObject(CacheConstants.CacheType cacheType, Collection<Object> code) {
        List<T> objectList = redisService.getMultiCacheMapValue(cacheType.getCode(), code);
        if (CollUtil.isNotEmpty(objectList) && objectList.size() != code.size()) {
            refreshCache(cacheType);
            return redisService.getMultiCacheMapValue(cacheType.getCode(), code);
        }
        return objectList;
    }

    /**
     * 更新指定类型的缓存对象
     *
     * @param cacheType 缓存枚举
     */
    private void refreshCache(CacheConstants.CacheType cacheType) {
        switch (cacheType) {
            case SYS_DICT_KEY -> SpringUtil.getBean(RemoteDictService.class).refreshCacheInner();
            case SYS_CONFIG_KEY -> SpringUtil.getBean(RemoteConfigService.class).refreshCacheInner();
            case TE_STRATEGY_KEY ->
                    SpringUtil.getBean(RemoteStrategyService.class).refreshCache();
            case TE_SOURCE_KEY -> SpringUtil.getBean(RemoteSourceService.class).refreshCache();
            default -> throw new UtilException("缓存更新方法不存在，请先定义！");
        }
    }

}
