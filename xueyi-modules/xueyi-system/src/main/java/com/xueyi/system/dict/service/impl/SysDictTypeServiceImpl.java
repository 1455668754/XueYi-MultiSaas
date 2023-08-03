package com.xueyi.system.dict.service.impl;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.po.SysDictTypePo;
import com.xueyi.system.api.dict.domain.query.SysDictTypeQuery;
import com.xueyi.system.dict.domain.correlate.SysDictTypeCorrelate;
import com.xueyi.system.dict.manager.ISysDictTypeManager;
import com.xueyi.system.dict.service.ISysDictDataService;
import com.xueyi.system.dict.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 系统服务 | 字典模块 | 字典类型管理 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysDictTypeServiceImpl extends BaseServiceImpl<SysDictTypeQuery, SysDictTypeDto, SysDictTypeCorrelate, ISysDictTypeManager> implements ISysDictTypeService {

    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询数据对象列表 | 数据权限 | 附加数据
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<SysDictTypeDto> selectListScope(SysDictTypeQuery query) {
        SysDictTypeCorrelate correlate = SysDictTypeCorrelate.EN_INFO_SELECT;
        return subCorrelates(selectList(query), correlate);
    }

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public SysDictTypeDto selectById(Serializable id) {
        SysDictTypeDto dto = baseManager.selectById(id);
        SysDictTypeCorrelate correlate = SysDictTypeCorrelate.EN_INFO_SELECT;
        return subCorrelates(dto, correlate);
    }

    /**
     * 默认方法关联配置定义
     */
    @Override
    protected Map<CorrelateConstants.ServiceType, SysDictTypeCorrelate> defaultCorrelate() {
        return new HashMap<>() {{
            put(CorrelateConstants.ServiceType.CACHE_REFRESH, SysDictTypeCorrelate.CACHE_REFRESH);
            put(CorrelateConstants.ServiceType.DELETE, SysDictTypeCorrelate.BASE_DEL);
        }};
    }

    /**
     * 缓存主键命名定义
     */
    @Override
    public CacheConstants.CacheType getCacheKey() {
        return CacheConstants.CacheType.SYS_DICT_KEY;
    }

    /**
     * 缓存标识命名定义
     */
    protected CacheConstants.CacheType getCacheRouteKey() {
        return CacheConstants.CacheType.ROUTE_DICT_KEY;
    }

    /**
     * 更新缓存数据
     */
    @Override
    public Boolean syncCache() {
        Long enterpriseId = SecurityUtils.getEnterpriseId();
        List<SysDictTypeDto> enterpriseTypeList = baseManager.selectList(null);
        subCorrelates(enterpriseTypeList, getBasicCorrelate(CorrelateConstants.ServiceType.CACHE_REFRESH));
        SecurityContextHolder.setEnterpriseId(SecurityConstants.COMMON_TENANT_ID.toString());
        List<SysDictTypeDto> commonTypeList = baseManager.selectList(null);
        subCorrelates(commonTypeList, getBasicCorrelate(CorrelateConstants.ServiceType.CACHE_REFRESH));
        SecurityContextHolder.setEnterpriseId(enterpriseId.toString());
        Map<String, SysDictTypeDto> enterpriseTypeMap = enterpriseTypeList.stream().collect(Collectors.toMap(SysDictTypePo::getCode, Function.identity()));
        List<SysDictTypeDto> addTypeList = new ArrayList<>();
        List<SysDictDataDto> addDataList = new ArrayList<>();
        Set<Long> delIdList = new HashSet<>();
        commonTypeList.forEach(type -> {
            if (StrUtil.equals(DictConstants.DicCacheType.OVERALL.getCode(), type.getCacheType())) {
                return;
            }
            SysDictTypeDto enterpriseType = enterpriseTypeMap.get(type.getCode());
            if (ObjectUtil.isNull(enterpriseType)) {
                addTypeList.add(type);
                addDataList.addAll(type.getSubList());
            } else {
                Set<String> enterpriseValues = enterpriseType.getSubList().stream().map(SysDictDataDto::getValue).collect(Collectors.toSet());
                Set<String> commonValues = type.getSubList().stream().map(SysDictDataDto::getValue).collect(Collectors.toSet());
                if (StrUtil.equals(DictConstants.DicDataType.READ.getCode(), type.getDataType()) || StrUtil.equals(DictConstants.DicDataType.INCREASE.getCode(), type.getDataType())) {
                    Set<String> increaseValues = new HashSet<>(commonValues);
                    increaseValues.removeAll(enterpriseValues);
                    Map<String, SysDictDataDto> commonDataMap = type.getSubList().stream().collect(Collectors.toMap(SysDictDataDto::getValue, Function.identity()));
                    increaseValues.forEach(item -> addDataList.add(commonDataMap.get(item)));
                }
                if (StrUtil.equals(DictConstants.DicDataType.READ.getCode(), type.getDataType()) || StrUtil.equals(DictConstants.DicDataType.SUBTRACT.getCode(), type.getDataType())) {
                    Map<String, Long> commonDataMap = enterpriseType.getSubList().stream().collect(Collectors.toMap(SysDictDataDto::getValue, SysDictDataDto::getId));
                    Set<String> subtractValues = new HashSet<>(enterpriseValues);
                    subtractValues.removeAll(commonValues);
                    subtractValues.forEach(item -> delIdList.add(commonDataMap.get(item)));
                }
            }
        });
        if (CollUtil.isNotEmpty(addTypeList)) {
            addTypeList.forEach(BasisEntity::initId);
            baseManager.insertBatch(addTypeList);
        }
        if (CollUtil.isNotEmpty(addDataList)) {
            addDataList.forEach(BasisEntity::initId);
            dictDataService.insertBatch(addDataList);
        }
        if (CollUtil.isNotEmpty(delIdList)) {
            dictDataService.deleteByIds(delIdList);
        }
        return Boolean.TRUE;
    }

    /**
     * 校验字典编码是否唯一
     *
     * @param Id       字典类型Id
     * @param dictCode 字典类型编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkDictCodeUnique(Long Id, String dictCode) {
        return ObjectUtil.isNotNull(baseManager.checkDictCodeUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, dictCode));
    }


    /**
     * 单条操作 - 开始处理
     *
     * @param operate   服务层 - 操作类型
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    protected void startHandle(OperateConstants.ServiceType operate, SysDictTypeDto originDto, SysDictTypeDto newDto) {
        switch (operate) {
            case ADD -> {
                if (ObjectUtil.notEqual(newDto.getTenantId(), SecurityUtils.getEnterpriseId())) {
                    if (SecurityUserUtils.isAdminTenant()) {
                        SecurityContextHolder.setEnterpriseId(newDto.getTenantId().toString());
                    } else {
                        throw new ServiceException("新增失败，无权限！");
                    }
                }
            }
            case EDIT, EDIT_STATUS -> {
                if (ObjectUtil.notEqual(originDto.getTenantId(), SecurityUtils.getEnterpriseId())) {
                    if (SecurityUserUtils.isAdminTenant()) {
                        SecurityContextHolder.setEnterpriseId(newDto.getTenantId().toString());
                    } else {
                        throw new ServiceException("新增失败，无权限！");
                    }
                }
            }
        }
    }

    /**
     * 单条操作 - 结束处理
     *
     * @param operate   服务层 - 操作类型
     * @param row       操作数据条数
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    protected void endHandle(OperateConstants.ServiceType operate, int row, SysDictTypeDto originDto, SysDictTypeDto newDto) {
        super.endHandle(operate, row, originDto, newDto);
        SecurityContextHolder.rollLastEnterpriseId();
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
    public void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, SysDictTypeDto dto, Collection<SysDictTypeDto> dtoList) {
        Long enterpriseId = SecurityUtils.getEnterpriseId();
        String cacheKey = StrUtil.format(getCacheKey().getCode(), enterpriseId);
        if (operate.isSingle()) {
            subCorrelates(dto, getBasicCorrelate(CorrelateConstants.ServiceType.CACHE_REFRESH));
        } else if (operate.isBatch()) {
            subCorrelates(dtoList, getBasicCorrelate(CorrelateConstants.ServiceType.CACHE_REFRESH));
        }
        switch (operateCache) {
            case REFRESH_ALL -> {
                redisService.deleteObject(cacheKey);
                redisService.refreshMapCache(cacheKey, dtoList, SysDictTypeDto::getCode, SysDictTypeDto::getSubList);
                // 索引标识
                if (ObjectUtil.equals(SecurityConstants.COMMON_TENANT_ID, enterpriseId)) {
                    redisService.deleteObject(getCacheRouteKey().getCode());
                    List<SysDictTypeDto> dictList = dtoList.stream().peek(item -> item.setSubList(null)).toList();
                    redisService.refreshMapCache(getCacheRouteKey().getCode(), dictList, SysDictTypeDto::getCode, Function.identity());
                }
            }
            case REFRESH -> {
                if (operate.isSingle()) {
                    redisService.refreshMapValueCache(cacheKey, dto::getCode, dto::getSubList);
                } else if (operate.isBatch()) {
                    dtoList.forEach(item -> redisService.refreshMapValueCache(cacheKey, item::getCode, item::getSubList));
                }
            }
            case REMOVE -> {
                if (operate.isSingle()) {
                    redisService.removeMapValueCache(cacheKey, dto.getCode());
                } else if (operate.isBatch()) {
                    redisService.removeMapValueCache(cacheKey, dtoList.stream().map(SysDictTypeDto::getCode).toArray(String[]::new));
                }
            }
        }
    }
}