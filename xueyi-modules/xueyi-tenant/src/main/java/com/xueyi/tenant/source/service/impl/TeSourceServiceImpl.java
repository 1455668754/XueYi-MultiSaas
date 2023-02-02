package com.xueyi.tenant.source.service.impl;

import com.xueyi.common.core.constant.basic.CacheConstants;
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
import com.xueyi.tenant.source.manager.impl.TeSourceManagerImpl;
import com.xueyi.tenant.source.service.ITeSourceService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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

    /**
     * 缓存更新
     *
     * @param operate      服务层 - 操作类型
     * @param operateCache 缓存操作类型
     * @param dto          数据对象
     * @param dtoList      数据对象集合
     */
    @Override
    protected void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, TeSourceDto dto, Collection<TeSourceDto> dtoList) {
        switch (operateCache) {
            case REFRESH_ALL:
                List<TeSourceDto> allList = baseManager.selectList(null);
                redisService.deleteObject(getCacheKey());
                redisService.refreshMapCache(getCacheKey(), allList, TeSourceDto::getSlave, TeSourceDto -> TeSourceDto);
                break;
            case REFRESH:
                if (operate.isSingle())
                    redisService.refreshMapValueCache(getCacheKey(), dto::getSlave, () -> dto);
                else if (operate.isBatch())
                    dtoList.forEach(item -> redisService.refreshMapValueCache(getCacheKey(), item::getSlave, () -> item));
                break;
            case REMOVE:
                if (operate.isSingle())
                    redisService.removeMapValueCache(getCacheKey(), dto.getSlave());
                else if (operate.isBatch())
                    redisService.removeMapValueCache(getCacheKey(), dtoList.stream().map(TeSourceDto::getSlave).toArray());
                break;
        }
    }
}