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
import com.xueyi.system.api.dict.domain.dto.SysImExDto;
import com.xueyi.system.api.dict.domain.query.SysImExQuery;
import com.xueyi.system.dict.domain.correlate.SysImExCorrelate;
import com.xueyi.system.dict.manager.ISysImExManager;
import com.xueyi.system.dict.service.ISysImExService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 导入导出配置管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysImExServiceImpl extends BaseServiceImpl<SysImExQuery, SysImExDto, SysImExCorrelate, ISysImExManager> implements ISysImExService {

    /** 缓存主键命名定义 */
    @Override
    public CacheConstants.CacheType getCacheKey() {
        return CacheConstants.CacheType.SYS_IM_EX_KEY;
    }

    /**
     * 查询数据对象列表 | 数据权限 | 附加数据
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<SysImExDto> selectListScope(SysImExQuery query) {
        return subCorrelates(selectList(query), SysImExCorrelate.EN_INFO_SELECT);
    }

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public SysImExDto selectById(Serializable id) {
        SysImExDto dto = baseManager.selectById(id);
        return subCorrelates(dto, SysImExCorrelate.EN_INFO_SELECT);
    }

    /**
     * 根据配置编码查询配置值
     *
     * @param code 配置编码
     * @return 配置对象
     */
    @Override
    public SysImExDto selectByCode(String code) {
        SysImExDto imEx = baseManager.selectByCode(code);
        if (ObjectUtil.isNull(imEx)) {
            syncCache();
            imEx = baseManager.selectByCode(code);
        }
        return imEx;
    }

    /**
     * 更新缓存数据
     */
    @Override
    public Boolean syncCache() {
        Long enterpriseId = SecurityUtils.getEnterpriseId();
        List<SysImExDto> enterpriseTypeList = baseManager.selectList(null);
        SecurityContextHolder.setEnterpriseId(SecurityConstants.COMMON_TENANT_ID.toString());
        List<SysImExDto> commonTypeList = baseManager.selectList(null);
        SecurityContextHolder.setEnterpriseId(enterpriseId.toString());
        Map<String, SysImExDto> enterpriseImExMap = enterpriseTypeList.stream().collect(Collectors.toMap(SysImExDto::getCode, Function.identity()));
        List<SysImExDto> addImExList = new ArrayList<>();
        commonTypeList.forEach(config -> {
            if (StrUtil.equals(DictConstants.DicCacheType.OVERALL.getCode(), config.getCacheType())) {
                return;
            }
            SysImExDto enterpriseType = enterpriseImExMap.get(config.getCode());
            if (ObjectUtil.isNull(enterpriseType)) {
                addImExList.add(config);
            }
        });
        if (CollUtil.isNotEmpty(addImExList)) {
            addImExList.forEach(SysImExDto::initId);
            baseManager.insertBatch(addImExList);
        }
        return Boolean.TRUE;
    }

    /**
     * 校验配置编码是否唯一
     *
     * @param id   配置Id
     * @param code 配置编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    @TenantIgnore
    public boolean checkCodeUnique(Long id, String code) {
        return ObjectUtil.isNotNull(baseManager.checkCodeUnique(ObjectUtil.isNull(id) ? BaseConstants.NONE_ID : id, code));
    }

    /**
     * 单条操作 - 开始处理
     *
     * @param operate 服务层 - 操作类型
     * @param newDto  新数据对象（删除时不存在）
     * @param id      Id集合（非删除时不存在）
     */
    @Override
    protected SysImExDto startHandle(OperateConstants.ServiceType operate, SysImExDto newDto, Serializable id) {
        SysImExDto originDto = SecurityContextHolder.setTenantIgnoreFun(() -> {
            SysImExDto info = super.startHandle(operate, newDto, id);
            return subCorrelates(info, SysImExCorrelate.EN_INFO_SELECT);
        });
        switch (operate) {
            case ADD -> {
                if (StrUtil.equals(DictConstants.DicCacheType.TENANT.getCode(), newDto.getCacheType()) || StrUtil.equals(DictConstants.DicCacheType.OVERALL.getCode(), newDto.getCacheType())) {
                    newDto.setTenantId(TenantConstants.COMMON_TENANT_ID);
                }
                if (ObjectUtil.notEqual(newDto.getTenantId(), SecurityUtils.getEnterpriseId())) {
                    if (SecurityUserUtils.isAdminTenant()) {
                        SecurityContextHolder.setEnterpriseId(newDto.getTenantId().toString());
                    } else {
                        throw new ServiceException("新增配置失败，无权限！");
                    }
                }
            }
            case EDIT, EDIT_STATUS -> {
                if (ObjectUtil.notEqual(originDto.getTenantId(), SecurityUtils.getEnterpriseId())) {
                    if (SecurityUserUtils.isAdminTenant()) {
                        SecurityContextHolder.setEnterpriseId(originDto.getTenantId().toString());
                    } else {
                        throw new ServiceException("修改配置失败，无权限！");
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
    protected void endHandle(OperateConstants.ServiceType operate, int row, SysImExDto originDto, SysImExDto newDto) {
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
    protected List<SysImExDto> startBatchHandle(OperateConstants.ServiceType operate, Collection<SysImExDto> newList, Collection<? extends Serializable> idList) {
        List<SysImExDto> originList = super.startBatchHandle(operate, newList, idList);
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
    protected void endBatchHandle(OperateConstants.ServiceType operate, int rows, Collection<SysImExDto> originList, Collection<SysImExDto> newList) {
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
        Collection<String> keys = redisService.keys(RedisConstants.CacheKey.IM_EX_KEY.getCacheName(StrUtil.ASTERISK));
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
    public void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, SysImExDto dto, Collection<SysImExDto> dtoList,
                             String cacheKey, Boolean isTenant, Function<? super SysImExDto, String> cacheKeyFun, Function<? super SysImExDto, Object> cacheValueFun) {
        // 默认缓存管理方法
        super.refreshCache(operate, operateCache, dto, dtoList, cacheKey, isTenant, SysImExDto::getCode, cacheValueFun);
        // 路由缓存管理方法
        if (SecurityUtils.isCommonTenant()) {
            CacheConstants.CacheType routeCacheKey = CacheConstants.CacheType.ROUTE_IM_EX_KEY;
            super.refreshCache(operate, operateCache, dto, dtoList, routeCacheKey.getCacheKey(), routeCacheKey.getIsTenant(), SysImExDto::getCode, Function.identity());
        }
    }
}