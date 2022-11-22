package com.xueyi.system.dict.service.impl;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.web.entity.service.impl.SubBaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;
import com.xueyi.system.api.dict.domain.query.SysDictTypeQuery;
import com.xueyi.system.dict.manager.ISysDictTypeManager;
import com.xueyi.system.dict.service.ISysDictDataService;
import com.xueyi.system.dict.service.ISysDictTypeService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 字典类型管理 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysDictTypeServiceImpl extends SubBaseServiceImpl<SysDictTypeQuery, SysDictTypeDto, ISysDictTypeManager, SysDictDataQuery, SysDictDataDto, ISysDictDataService> implements ISysDictTypeService {

    /**
     * 缓存主键命名定义
     */
    @Override
    protected String getCacheKey() {
        return CacheConstants.CacheType.SYS_DICT_KEY.getCode();
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
     * 设置子数据的外键值
     */
    @Override
    protected void setForeignKey(Collection<SysDictDataDto> dictDataList, SysDictDataDto dictData, SysDictTypeDto dictType, Serializable key) {
        String code = ObjectUtil.isNotNull(dictType) ? dictType.getCode() : (String) key;
        if (ObjectUtil.isNotNull(dictData))
            dictData.setCode(code);
        else
            dictDataList.forEach(sub -> sub.setCode(code));
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
    protected void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, SysDictTypeDto dto, Collection<SysDictTypeDto> dtoList) {
        switch (operateCache) {
            case REFRESH_ALL:
                List<SysDictTypeDto> allList = baseManager.selectList(null);
                redisService.deleteObject(getCacheKey());
                redisService.refreshMapCache(getCacheKey(), allList, SysDictTypeDto::getCode, SysDictTypeDto::getSubList);
                break;
            case REFRESH:
                if (operate.isSingle())
                    redisService.refreshMapValueCache(getCacheKey(), dto::getCode, dto::getSubList);
                else if (operate.isBatch())
                    dtoList.forEach(item -> redisService.refreshMapValueCache(getCacheKey(), item::getCode, item::getSubList));
                break;
            case REMOVE:
                if (operate.isSingle())
                    redisService.removeMapValueCache(getCacheKey(), dto.getCode());
                else if (operate.isBatch())
                    redisService.removeMapValueCache(getCacheKey(), dtoList.stream().map(SysDictTypeDto::getCode).toArray());
                break;
        }
    }
}