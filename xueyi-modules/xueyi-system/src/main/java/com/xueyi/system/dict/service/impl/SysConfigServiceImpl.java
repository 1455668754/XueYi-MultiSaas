package com.xueyi.system.dict.service.impl;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.basic.SecurityConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.query.SysConfigQuery;
import com.xueyi.system.dict.correlate.SysConfigCorrelate;
import com.xueyi.system.dict.manager.ISysConfigManager;
import com.xueyi.system.dict.service.ISysConfigService;
import org.springframework.stereotype.Service;

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

    /**
     * 缓存主键命名定义
     */
    @Override
    protected CacheConstants.CacheType getCacheKey() {
        return CacheConstants.CacheType.SYS_CONFIG_KEY;
    }

    /**
     * 缓存标识命名定义
     */
    protected CacheConstants.CacheType getCacheRouteKey() {
        return CacheConstants.CacheType.ROUTE_CONFIG_KEY;
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
        List<SysConfigDto> enterpriseTypeList = baseManager.selectListMerge(null);
        SecurityContextHolder.setEnterpriseId(SecurityConstants.COMMON_TENANT_ID.toString());
        List<SysConfigDto> commonTypeList = baseManager.selectListMerge(null);
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
            addConfigList.forEach(item -> item.setId(null));
            baseManager.insertBatch(addConfigList);
        }
        return Boolean.TRUE;
    }

    /**
     * 校验参数编码是否唯一
     *
     * @param Id         参数Id
     * @param configCode 参数编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkConfigCodeUnique(Long Id, String configCode) {
        return ObjectUtil.isNotNull(baseManager.checkConfigCodeUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, configCode));
    }

    /**
     * 校验是否为内置参数
     *
     * @param Id 参数Id
     * @return 结果 | true/false 是/否
     */
    @Override
    public boolean checkIsBuiltIn(Long Id) {
        return ObjectUtil.isNotNull(baseManager.checkIsBuiltIn(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id));
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
    protected void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, SysConfigDto dto, Collection<SysConfigDto> dtoList) {
        Long enterpriseId = SecurityUtils.getEnterpriseId();
        String cacheKey = StrUtil.format(getCacheKey().getCode(), enterpriseId);
        switch (operateCache) {
            case REFRESH_ALL -> {
                List<SysConfigDto> allList = baseManager.selectList(null);
                // 索引标识
                if (ObjectUtil.equals(SecurityConstants.COMMON_TENANT_ID, enterpriseId)) {
                    redisService.deleteObject(getCacheRouteKey().getCode());
                    redisService.refreshMapCache(getCacheRouteKey().getCode(), allList, SysConfigDto::getCode, Function.identity());
                }
                redisService.deleteObject(cacheKey);
                redisService.refreshMapCache(cacheKey, allList, SysConfigDto::getCode, SysConfigDto::getValue);
            }
            case REFRESH -> {
                if (operate.isSingle()) {
                    redisService.refreshMapValueCache(cacheKey, dto::getCode, dto::getValue);
                } else if (operate.isBatch()) {
                    dtoList.forEach(item -> redisService.refreshMapValueCache(cacheKey, item::getCode, item::getValue));
                }
            }
            case REMOVE -> {
                if (operate.isSingle()) {
                    redisService.removeMapValueCache(cacheKey, dto.getCode());
                } else if (operate.isBatch()) {
                    redisService.removeMapValueCache(cacheKey, dtoList.stream().map(SysConfigDto::getCode).toArray(String[]::new));
                }
            }
        }
    }
}