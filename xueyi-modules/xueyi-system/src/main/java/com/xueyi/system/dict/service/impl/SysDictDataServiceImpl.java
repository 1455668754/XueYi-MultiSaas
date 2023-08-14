package com.xueyi.system.dict.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.context.SecurityContextHolder;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.SpringUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.security.utils.SecurityUserUtils;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.api.dict.domain.po.SysDictDataPo;
import com.xueyi.system.api.dict.domain.query.SysDictDataQuery;
import com.xueyi.system.dict.domain.correlate.SysDictDataCorrelate;
import com.xueyi.system.dict.manager.ISysDictDataManager;
import com.xueyi.system.dict.service.ISysDictDataService;
import com.xueyi.system.dict.service.ISysDictTypeService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
     * 修改数据对象
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int update(SysDictDataDto dto) {
        SecurityContextHolder.setTenantIgnore();
        SysDictDataDto originDto = selectById(dto.getId());
        SecurityContextHolder.clearTenantIgnore();
        startHandle(OperateConstants.ServiceType.EDIT, originDto, dto);
        int row = baseManager.update(dto);
        endHandle(OperateConstants.ServiceType.EDIT, row, originDto, dto);
        return row;
    }

    /**
     * 单条操作 - 开始处理
     *
     * @param operate   服务层 - 操作类型
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    protected void startHandle(OperateConstants.ServiceType operate, SysDictDataDto originDto, SysDictDataDto newDto) {
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
    protected void endHandle(OperateConstants.ServiceType operate, int row, SysDictDataDto originDto, SysDictDataDto newDto) {
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
    public void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, SysDictDataDto dto, Collection<SysDictDataDto> dtoList) {
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