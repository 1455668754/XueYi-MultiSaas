package com.xueyi.system.dict.service.impl;

import com.xueyi.common.cache.constants.CacheConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.po.SysDictDataPo;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;
import com.xueyi.system.dict.manager.ISysDictDataManager;
import com.xueyi.system.dict.service.ISysDictDataService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字典数据管理 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictDataQuery, SysDictDataDto, ISysDictDataManager> implements ISysDictDataService {

    /**
     * 缓存主键命名定义
     */
    @Override
    protected CacheConstants.CacheType getCacheKey() {
        return CacheConstants.CacheType.SYS_DICT_KEY;
    }

    /**
     * 查询字典数据对象列表
     *
     * @param code 字典编码
     * @return 字典数据对象集合
     */
    @Override
    public List<SysDictDataDto> selectListByCode(String code) {
        return baseManager.selectListByCode(code);
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
    protected void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, SysDictDataDto dto, Collection<SysDictDataDto> dtoList) {
        String cacheKey = StrUtil.format(getCacheKey().getCode(), SecurityUtils.getEnterpriseId());
        if (operate.isSingle()) {
            List<SysDictDataDto> dictList = baseManager.selectListByCode(dto.getCode());
            redisService.refreshMapValueCache(cacheKey, dto::getCode, () -> dictList);
        } else if (operate.isBatch()) {
            Set<String> codes = dtoList.stream().map(SysDictDataPo::getCode).collect(Collectors.toSet());
            if (CollUtil.isEmpty(codes)) {
                return;
            }
            List<SysDictDataDto> dictList = baseManager.selectListByCodes(codes);
            Map<String, List<SysDictDataDto>> dictMap = dictList.stream().collect(Collectors.groupingBy(SysDictDataDto::getCode));
            codes.forEach(item -> redisService.refreshMapValueCache(cacheKey, () -> item, () -> dictMap.get(item)));
        }
    }
}