package com.xueyi.tenant.source.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.IdUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;
import com.xueyi.tenant.source.domain.correlate.TeSourceCorrelate;
import com.xueyi.tenant.source.manager.ITeSourceManager;
import com.xueyi.tenant.source.service.ITeSourceService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Function;

/**
 * 租户服务 | 策略模块 | 数据源管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class TeSourceServiceImpl extends BaseServiceImpl<TeSourceQuery, TeSourceDto, TeSourceCorrelate, ITeSourceManager> implements ITeSourceService {

    /** 缓存主键命名定义 */
    @Override
    public CacheConstants.CacheType getCacheKey() {
        return CacheConstants.CacheType.TE_SOURCE_KEY;
    }

    /**
     * 校验数据源是否为默认数据源
     *
     * @param id 数据源id
     * @return 结果 | true/false 是/不是
     */
    @Override
    public boolean checkIsDefault(Long id) {
        TeSourceDto source = baseManager.selectById(id);
        return ObjectUtil.isNotNull(source) && StrUtil.equals(source.getIsDefault(), DictConstants.DicYesNo.YES.getCode());
    }

    /**
     * 新增数据源对象
     *
     * @param source 数据源对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(TeSourceDto source) {
        source.setSlave(IdUtil.simpleUUID());
        return super.insert(source);
    }

    /**
     * 新增数据源对象（批量）
     *
     * @param sourceList 数据源对象集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insertBatch(Collection<TeSourceDto> sourceList) {
        if (CollUtil.isNotEmpty(sourceList)) {
            sourceList.forEach(source -> source.setSlave(IdUtil.simpleUUID()));
        }
        return super.insertBatch(sourceList);
    }

    /**
     * 缓存更新
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
    @Override
    public void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, TeSourceDto dto, Collection<TeSourceDto> dtoList,
                             String cacheKey, Boolean isTenant, Function<? super TeSourceDto, String> cacheKeyFun, Function<? super TeSourceDto, Object> cacheValueFun) {
        super.refreshCache(operate, operateCache, dto, dtoList, cacheKey, isTenant, TeSourceDto::getSlave, Function.identity());
    }
}