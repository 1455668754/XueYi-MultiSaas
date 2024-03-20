package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.redis.constant.RedisConstants;

import java.util.Collection;
import java.util.function.Function;

/**
 * 服务层 操作方法 基类实现通用缓存处理
 *
 * @param <D> Dto
 * @author xueyi
 */
public interface BaseCacheHandle<D extends BaseEntity> {

    /** 缓存主键命名定义 */
    default CacheConstants.CacheType getCacheKey() {
        return null;
    }

    /**
     * 缓存更新
     *
     * @param operate      服务层 - 操作类型
     * @param operateCache 缓存操作类型
     * @param dto          数据对象
     */
    default void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, D dto) {
        refreshCache(operate, operateCache, dto, null);
    }

    /**
     * 缓存更新
     *
     * @param operate      服务层 - 操作类型
     * @param operateCache 缓存操作类型
     * @param dtoList      数据对象集合
     */
    default void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, Collection<D> dtoList) {
        refreshCache(operate, operateCache, null, dtoList);
    }

    /**
     * 缓存更新
     *
     * @param operate      服务层 - 操作类型
     * @param operateCache 缓存操作类型
     * @param dto          数据对象
     * @param dtoList      数据对象集合
     */
    default void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, D dto, Collection<D> dtoList) {
        refreshCache(operate, operateCache, dto, dtoList, getCacheKey().getCode(), getCacheKey().getIsTenant(), D::getIdStr, Function.identity());
    }

    /**
     * 缓存更新 | 自定义注入
     *
     * @param operate       服务层 - 操作类型
     * @param operateCache  缓存操作类型
     * @param dto           数据对象
     * @param dtoList       数据对象集合
     * @param cacheKey      缓存编码
     * @param isTenant      租户级缓存
     * @param cacheKeyFun   缓存键定义方法
     * @param cacheValueFun 缓存值定义方法
     */
    void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, D dto, Collection<D> dtoList,
                      String cacheKey, Boolean isTenant, Function<? super D, String> cacheKeyFun, Function<? super D, Object> cacheValueFun);
}
