package com.xueyi.system.dict.service.impl;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.api.dict.domain.query.SysConfigQuery;
import com.xueyi.system.dict.manager.ISysConfigManager;
import com.xueyi.system.dict.service.ISysConfigService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 参数配置管理 服务层实现
 *
 * @author xueyi
 */
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigQuery, SysConfigDto, ISysConfigManager> implements ISysConfigService {

    /**
     * 缓存主键命名定义
     */
    @Override
    protected String getCacheKey() {
        return CacheConstants.CacheType.SYS_CONFIG_KEY.getCode();
    }

    /**
     * 根据参数编码查询参数值
     *
     * @param configCode 参数编码
     * @return 参数值
     */
    @Override
    public String selectConfigByCode(String configCode) {
        SysConfigDto config = baseManager.selectConfigByCode(configCode);
        return ObjectUtil.isNotNull(config) ? config.getValue() : StrUtil.EMPTY;
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
        switch (operateCache) {
            case REFRESH_ALL:
                List<SysConfigDto> allList = baseManager.selectList(null);
                redisService.deleteObject(getCacheKey());
                redisService.refreshMapCache(getCacheKey(), allList, SysConfigDto::getCode, SysConfigDto::getValue);
                break;
            case REFRESH:
                if (operate.isSingle())
                    redisService.refreshMapValueCache(getCacheKey(), dto::getCode, dto::getValue);
                else if (operate.isBatch())
                    dtoList.forEach(item -> redisService.refreshMapValueCache(getCacheKey(), item::getCode, item::getValue));
                break;
            case REMOVE:
                if (operate.isSingle())
                    redisService.removeMapValueCache(getCacheKey(), dto.getCode());
                else if (operate.isBatch())
                    redisService.removeMapValueCache(getCacheKey(), dtoList.stream().map(SysConfigDto::getCode).toArray());
                break;
        }
    }
}
