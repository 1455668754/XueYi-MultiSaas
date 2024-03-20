package com.xueyi.system.dict.service.impl;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;
import com.xueyi.system.dict.domain.correlate.SysDictDataCorrelate;
import com.xueyi.system.dict.manager.ISysDictDataManager;
import com.xueyi.system.dict.service.ISysDictDataService;
import com.xueyi.system.dict.service.ISysDictTypeService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 系统服务 | 字典模块 | 字典数据管理 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictDataQuery, SysDictDataDto, SysDictDataCorrelate, ISysDictDataManager> implements ISysDictDataService {

    /**
     * 缓存主键命名定义
     */
    @Override
    public CacheConstants.CacheType getCacheKey() {
        return CacheConstants.CacheType.SYS_DICT_KEY;
    }

    /**
     * 单条操作 - 开始处理
     *
     * @param operate 服务层 - 操作类型
     * @param newDto  新数据对象（删除时不存在）
     * @param id      Id集合（非删除时不存在）
     */
    @Override
    protected SysDictDataDto startHandle(OperateConstants.ServiceType operate, SysDictDataDto newDto, Serializable id) {
        SecurityContextHolder.setTenantIgnore();
        SysDictDataDto originDto = super.startHandle(operate, newDto, id);
        SecurityContextHolder.clearTenantIgnore();
        switch (operate) {
            case ADD, EDIT, EDIT_STATUS -> {
                if (ObjectUtil.isNotNull(newDto.getDictTypeId())) {
                    SysDictTypeDto dictType = SpringUtil.getBean(ISysDictTypeService.class).selectByIdIgnore(newDto.getDictTypeId());
                    newDto.setDictTypeInfo(dictType);
                }
            }
        }

        switch (operate) {
            case EDIT, EDIT_STATUS -> {
                if (ObjectUtil.notEqual(originDto.getTenantId(), SecurityUtils.getEnterpriseId())) {
                    if (SecurityUserUtils.isAdminTenant()) {
                        SecurityContextHolder.setEnterpriseId(newDto.getTenantId().toString());
                    } else {
                        throw new ServiceException("修改失败，无权限！");
                    }
                }
            }
            case ADD -> {
                if (ObjectUtil.notEqual(newDto.getDictTypeInfo().getTenantId(), SecurityUtils.getEnterpriseId())) {
                    if (SecurityUserUtils.isAdminTenant()) {
                        SecurityContextHolder.setEnterpriseId(newDto.getDictTypeInfo().getTenantId().toString());
                    } else {
                        throw new ServiceException("新增失败，无权限！");
                    }
                }
            }
            case DELETE -> {
                if (SecurityUserUtils.isAdminTenant()) {
                    SecurityContextHolder.setTenantIgnore();
                }
            }
        }
        return originDto;
    }

    /**
     * 单条操作 - 结束处理
     *
     * @param operate   服务层 - 操作类型
     * @param row       操作数据条数
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    @Override
    protected void endHandle(OperateConstants.ServiceType operate, int row, SysDictDataDto originDto, SysDictDataDto newDto) {
        switch (operate) {
            case DELETE -> {
                if (SecurityUserUtils.isAdminTenant()) {
                    SecurityContextHolder.clearTenantIgnore();
                }
            }
            case ADD, EDIT, EDIT_STATUS -> SecurityContextHolder.rollLastEnterpriseId();
        }
        super.endHandle(operate, row, originDto, newDto);
    }

    /**
     * 批量操作 - 开始处理
     *
     * @param operate 服务层 - 操作类型
     * @param newList 新数据对象集合（删除时不存在）
     * @param idList  Id集合（非删除时不存在）
     */
    @Override
    protected List<SysDictDataDto> startBatchHandle(OperateConstants.ServiceType operate, Collection<SysDictDataDto> newList, Collection<? extends Serializable> idList) {
        List<SysDictDataDto> originList = super.startBatchHandle(operate, newList, idList);
        if (operate == OperateConstants.ServiceType.BATCH_DELETE) {
            if (SecurityUserUtils.isAdminTenant()) {
                SecurityContextHolder.setTenantIgnore();
            }
        }
        return originList;
    }

    /**
     * 批量操作 - 结束处理
     *
     * @param operate    服务层 - 操作类型
     * @param rows       操作数据条数
     * @param originList 源数据对象集合（新增时不存在）
     * @param newList    新数据对象集合（删除时不存在）
     */
    @Override
    protected void endBatchHandle(OperateConstants.ServiceType operate, int rows, Collection<SysDictDataDto> originList, Collection<SysDictDataDto> newList) {
        if (operate == OperateConstants.ServiceType.BATCH_DELETE) {
            if (SecurityUserUtils.isAdminTenant()) {
                SecurityContextHolder.clearTenantIgnore();
            }
        }
        super.endBatchHandle(operate, rows, originList, newList);
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
    public void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, SysDictDataDto dto, Collection<SysDictDataDto> dtoList,
                             String cacheKey, Boolean isTenant, Function<? super SysDictDataDto, String> cacheKeyFun, Function<? super SysDictDataDto, Object> cacheValueFun) {
        switch (operateCache) {
            case REFRESH, REMOVE -> {
                Collection<SysDictDataDto> list = operate.isBatch() ? dtoList : new ArrayList<>(){{
                    add(dto);
                }};
                List<String> enterpriseCaches = list.stream(). map(SysDictDataDto::getTenantId).collect(Collectors.toSet())
                        .stream().map(item -> CacheConstants.CacheType.getCusCacheKey(cacheKey, Boolean.TRUE, item)).toList();
                redisService.deleteObject(enterpriseCaches);
            }
        }
    }
}