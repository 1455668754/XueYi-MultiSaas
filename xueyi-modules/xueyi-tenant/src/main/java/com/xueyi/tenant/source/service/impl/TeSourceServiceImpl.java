package com.xueyi.tenant.source.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.IdUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;
import com.xueyi.tenant.source.domain.correlate.TeSourceCorrelate;
import com.xueyi.tenant.source.manager.impl.TeSourceManagerImpl;
import com.xueyi.tenant.source.service.ITeSourceService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 租户服务 | 策略模块 | 数据源管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class TeSourceServiceImpl extends BaseServiceImpl<TeSourceQuery, TeSourceDto, TeSourceCorrelate, TeSourceManagerImpl> implements ITeSourceService {

    /** 缓存主键命名定义 */
    @Override
    public CacheConstants.CacheType getCacheKey() {
        return CacheConstants.CacheType.TE_SOURCE_KEY;
    }

    /** 缓存键取值逻辑定义 | Function */
    @Override
    public Function<? super TeSourceDto, String> cacheKeyFun() {
        return TeSourceDto::getSlave;
    }

    /** 缓存键取值逻辑定义 | Supplier */
    @Override
    public Supplier<Serializable> cacheKeySupplier(TeSourceDto dto) {
        return dto::getSlave;
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
}