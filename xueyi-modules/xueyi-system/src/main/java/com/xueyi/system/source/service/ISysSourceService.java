package com.xueyi.system.source.service;

/**
 * 策略组管理 服务层
 *
 * @author xueyi
 */
public interface ISysSourceService {

    /**
     * 加载策略组缓存数据
     */
    void loadingSourceCache();

    /**
     * 清空策略组缓存数据
     */
    void clearSourceCache();

    /**
     * 重置策略组缓存数据
     */
    void resetSourceCache();

    /**
     * 根据Id更新策略组缓存数据
     */
    void refreshSourceCache(Long id);
}
