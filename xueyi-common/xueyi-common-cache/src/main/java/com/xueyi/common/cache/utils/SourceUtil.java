package com.xueyi.common.cache.utils;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.cache.service.CacheService;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.web.model.SysSource;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.dto.TeStrategyDto;

/**
 * 源策略组缓存管理工具类
 *
 * @author xueyi
 */
public class SourceUtil {

    /**
     * 源策略组查询
     *
     * @param id 源策略组Id
     */
    public static SysSource getSourceCache(Long id) {
        TeStrategyDto strategy = getTeStrategyCache(id);
        if (ObjectUtil.isNull(strategy))
            return null;
        SysSource source = new SysSource();
        source.setId(strategy.getId());
        source.setSourceId(strategy.getSourceId());
        source.setMaster(strategy.getSourceSlave());
        return source;
    }

    /**
     * 获取源策略缓存
     *
     * @param id 源策略组Id
     * @return 源策略对象
     */
    public static TeStrategyDto getTeStrategyCache(Long id) {
        return SpringUtil.getBean(CacheService.class).getCacheObject(CacheConstants.CacheType.TE_STRATEGY_KEY, id.toString());
    }

    /**
     * 获取数据源缓存
     *
     * @param slave 数据源编码
     * @return 数据源对象
     */
    public static TeSourceDto getTeSourceCache(String slave) {
        return SpringUtil.getBean(CacheService.class).getCacheObject(CacheConstants.CacheType.TE_SOURCE_KEY, slave);
    }
}
