package com.xueyi.tenant.source.service.impl;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.IdUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.api.source.domain.query.TeSourceQuery;
import com.xueyi.tenant.source.manager.impl.TeSourceManagerImpl;
import com.xueyi.tenant.source.service.ITeSourceService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 数据源管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class TeSourceServiceImpl extends BaseServiceImpl<TeSourceQuery, TeSourceDto, TeSourceManagerImpl> implements ITeSourceService {

    /**
     * 缓存主键命名定义
     */
    @Override
    protected String getCacheKey() {
        return CacheConstants.CacheType.TE_SOURCE_KEY.getCode();
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
     * 新增数据源对象（批量）
     *
     * @param sourceList 数据源对象集合
     * @return 结果
     */
    @Override
    public int insertBatch(Collection<TeSourceDto> sourceList) {
        if (CollUtil.isNotEmpty(sourceList))
            sourceList.forEach(source -> source.setSlave(IdUtil.simpleUUID()));
        return super.insertBatch(sourceList);
    }
}