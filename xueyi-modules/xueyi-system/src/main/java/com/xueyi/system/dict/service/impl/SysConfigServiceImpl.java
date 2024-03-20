package com.xueyi.system.dict.service.impl;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.constant.basic.TenantConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.annotation.TenantIgnore;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.query.SysConfigQuery;
import com.xueyi.system.dict.domain.correlate.SysConfigCorrelate;
import com.xueyi.system.dict.manager.ISysConfigManager;
import com.xueyi.system.dict.service.ISysConfigService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 系统服务 | 字典模块 | 参数管理 服务层实现
 *
 * @author xueyi
 */
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigQuery, SysConfigDto, SysConfigCorrelate, ISysConfigManager> implements ISysConfigService {

    /** 缓存主键命名定义 */
    @Override
    public CacheConstants.CacheType getCacheKey() {
        return CacheConstants.CacheType.SYS_CONFIG_KEY;
    }

    /**
     * 查询数据对象列表 | 数据权限 | 附加数据
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<SysConfigDto> selectListScope(SysConfigQuery query) {
        return subCorrelates(selectList(query), SysConfigCorrelate.EN_INFO_SELECT);
    }

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public SysConfigDto selectById(Serializable id) {
        SysConfigDto dto = baseManager.selectById(id);
        return subCorrelates(dto, SysConfigCorrelate.EN_INFO_SELECT);
    }

    /**
     * 根据参数编码查询参数值
     *
     * @param code 参数编码
     * @return 参数对象
     */
    @Override
    public SysConfigDto selectConfigByCode(String code) {
        SysConfigDto config = baseManager.selectConfigByCode(code);
        if (ObjectUtil.isNull(config)) {
            syncCache();
            config = baseManager.selectConfigByCode(code);
        }
        return config;
    }

    /**
     * 更新缓存数据
     */
    @Override
    public Boolean syncCache() {
        Long enterpriseId = SecurityUtils.getEnterpriseId();
        List<SysConfigDto> enterpriseTypeList = baseManager.selectList(null);
        SecurityContextHolder.setEnterpriseId(SecurityConstants.COMMON_TENANT_ID.toString());
        List<SysConfigDto> commonTypeList = baseManager.selectList(null);
        SecurityContextHolder.setEnterpriseId(enterpriseId.toString());
        Map<String, SysConfigDto> enterpriseConfigMap = enterpriseTypeList.stream().collect(Collectors.toMap(SysConfigDto::getCode, Function.identity()));
        List<SysConfigDto> addConfigList = new ArrayList<>();
        commonTypeList.forEach(config -> {
            if (StrUtil.equals(DictConstants.DicCacheType.OVERALL.getCode(), config.getCacheType())) {
                return;
            }
            SysConfigDto enterpriseType = enterpriseConfigMap.get(config.getCode());
            if (ObjectUtil.isNull(enterpriseType)) {
                addConfigList.add(config);
            }
        });
        if (CollUtil.isNotEmpty(addConfigList)) {
            addConfigList.forEach(SysConfigDto::initId);
            baseManager.insertBatch(addConfigList);
        }
        return Boolean.TRUE;
    }

    /**
     * 校验参数编码是否唯一
     *
     * @param id         参数Id
     * @param configCode 参数编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    @TenantIgnore
    public boolean checkConfigCodeUnique(Long id, String configCode) {
        return ObjectUtil.isNotNull(baseManager.checkConfigCodeUnique(ObjectUtil.isNull(id) ? BaseConstants.NONE_ID : id, configCode));
    }

    /**
     * 校验是否为内置参数
     *
     * @param id 参数Id
     * @return 结果 | true/false 是/否
     */
    @Override
    public boolean checkIsBuiltIn(Long id) {
        return ObjectUtil.isNotNull(baseManager.checkIsBuiltIn(ObjectUtil.isNull(id) ? BaseConstants.NONE_ID : id));
    }

    /**
     * 单条操作 - 开始处理
     *
     * @param operate 服务层 - 操作类型
     * @param newDto  新数据对象（删除时不存在）
     * @param id      Id集合（非删除时不存在）
     */
    @Override
    protected SysConfigDto startHandle(OperateConstants.ServiceType operate, SysConfigDto newDto, Serializable id) {
        SecurityContextHolder.setTenantIgnore();
        SysConfigDto originDto = super.startHandle(operate, newDto, id);
        subCorrelates(originDto, SysConfigCorrelate.EN_INFO_SELECT);
        SecurityContextHolder.clearTenantIgnore();
        switch (operate) {
            case ADD -> {
                if (StrUtil.equals(DictConstants.DicCacheType.TENANT.getCode(), newDto.getCacheType()) || StrUtil.equals(DictConstants.DicCacheType.OVERALL.getCode(), newDto.getCacheType())) {
                    newDto.setTenantId(TenantConstants.COMMON_TENANT_ID);
                }
                if (ObjectUtil.notEqual(newDto.getTenantId(), SecurityUtils.getEnterpriseId())) {
                    if (SecurityUserUtils.isAdminTenant()) {
                        SecurityContextHolder.setEnterpriseId(newDto.getTenantId().toString());
                    } else {
                        throw new ServiceException("新增参数失败，无权限！");
                    }
                }
            }
            case EDIT, EDIT_STATUS -> {
                if (ObjectUtil.notEqual(originDto.getTenantId(), SecurityUtils.getEnterpriseId())) {
                    if (SecurityUserUtils.isAdminTenant()) {
                        SecurityContextHolder.setEnterpriseId(originDto.getTenantId().toString());
                    } else {
                        throw new ServiceException("修改参数失败，无权限！");
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
    protected void endHandle(OperateConstants.ServiceType operate, int row, SysConfigDto originDto, SysConfigDto newDto) {
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
    protected List<SysConfigDto> startBatchHandle(OperateConstants.ServiceType operate, Collection<SysConfigDto> newList, Collection<? extends Serializable> idList) {
        List<SysConfigDto> originList = super.startBatchHandle(operate, newList, idList);
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
    protected void endBatchHandle(OperateConstants.ServiceType operate, int rows, Collection<SysConfigDto> originList, Collection<SysConfigDto> newList) {
        if (operate == OperateConstants.ServiceType.BATCH_DELETE) {
            if (SecurityUserUtils.isAdminTenant()) {
                SecurityContextHolder.clearTenantIgnore();
            }
        }
        super.endBatchHandle(operate, rows, originList, newList);
    }

    /**
     * 清空缓存数据
     */
    @Override
    public void clearCache() {
        Collection<String> keys = redisService.keys(RedisConstants.CacheKey.CONFIG_KEY.getCacheName(StrUtil.ASTERISK));
        if (CollUtil.isNotEmpty(keys)) {
            redisService.deleteObject(keys);
        }
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
    public void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, SysConfigDto dto, Collection<SysConfigDto> dtoList,
                             String cacheKey, Boolean isTenant, Function<? super SysConfigDto, String> cacheKeyFun, Function<? super SysConfigDto, Object> cacheValueFun) {
        // 默认缓存管理方法
        super.refreshCache(operate, operateCache, dto, dtoList, cacheKey, isTenant, SysConfigDto::getCode, SysConfigDto::getValue);
        // 路由缓存管理方法
        if (SecurityUtils.isCommonTenant()) {
            CacheConstants.CacheType routeCacheKey = CacheConstants.CacheType.ROUTE_CONFIG_KEY;
            super.refreshCache(operate, operateCache, dto, dtoList, routeCacheKey.getCacheKey(), routeCacheKey.getIsTenant(), SysConfigDto::getCode, Function.identity());
        }
    }
}