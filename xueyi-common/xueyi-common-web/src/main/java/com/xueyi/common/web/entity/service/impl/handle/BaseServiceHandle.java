package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.common.web.entity.manager.IBaseManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 服务层 操作方法 基类实现通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <C>   Correlate
 * @param <IDG> DtoIManager
 * @author xueyi
 */
public class BaseServiceHandle<Q extends BaseEntity, D extends BaseEntity, C extends Enum<? extends Enum<?>> & CorrelateService, IDG extends IBaseManager<Q, D>> extends BaseCorrelateHandle<D, C> implements BaseCacheHandle<D> {

    @Autowired
    protected IDG baseManager;

    @Autowired
    protected RedisService redisService;

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
    public void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, D dto, Collection<D> dtoList,
                             String cacheKey, Boolean isTenant, Function<? super D, String> cacheKeyFun, Function<? super D, Object> cacheValueFun) {
        // 校验是否启动缓存管理
        if (StrUtil.isBlank(cacheKey)) {
            return;
        }
        String cacheKeyName = CacheConstants.CacheType.getCusCacheKey(cacheKey, isTenant);
        // 是否存在缓存字段二次组装
        if (operate.isSingle()) {
            subCorrelates(dto, getBasicCorrelate(CorrelateConstants.ServiceType.CACHE_REFRESH));
        } else if (operate.isBatch()) {
            subCorrelates(dtoList, getBasicCorrelate(CorrelateConstants.ServiceType.CACHE_REFRESH));
        }
        switch (operateCache) {
            case REFRESH_ALL -> {
                redisService.deleteObject(cacheKeyName);
                redisService.refreshMapCache(cacheKeyName, dtoList, cacheKeyFun, cacheValueFun);
            }
            case REFRESH -> {
                Collection<D> list = operate.isBatch() ? dtoList : new ArrayList<>() {{
                    add(dto);
                }};
                redisService.refreshMapCache(cacheKeyName, list, cacheKeyFun, cacheValueFun);
            }
            case REMOVE -> {
                Collection<D> list = operate.isBatch() ? dtoList : new ArrayList<>() {{
                    add(dto);
                }};
                redisService.refreshMapCache(cacheKeyName, list, cacheKeyFun);
            }
        }
    }

    /**
     * 单条操作 - 开始处理
     *
     * @param operate 服务层 - 操作类型
     * @param newDto  新数据对象（删除时不存在）
     * @param id      Id集合（非删除时不存在）
     */
    protected D startHandle(OperateConstants.ServiceType operate, D newDto, Serializable id) {
        return switch (operate) {
            case EDIT, EDIT_STATUS -> baseManager.selectById(newDto.getId());
            case DELETE -> baseManager.selectById(id);
            default -> null;
        };
    }

    /**
     * 单条操作 - 结束处理
     *
     * @param operate   服务层 - 操作类型
     * @param row       操作数据条数
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    protected void endHandle(OperateConstants.ServiceType operate, int row, D originDto, D newDto) {
        if (row <= 0) {
            return;
        }
        switch (operate) {
            case ADD -> {
                // insert correlate data
                addCorrelates(newDto, getBasicCorrelate(CorrelateConstants.ServiceType.ADD));
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newDto);
            }
            case EDIT -> {
                // update correlate data
                editCorrelates(originDto, newDto, getBasicCorrelate(CorrelateConstants.ServiceType.EDIT));
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newDto);
            }
            case EDIT_STATUS -> refreshCache(operate, RedisConstants.OperateType.REFRESH, newDto);
            case DELETE -> {
                // delete correlate data
                delCorrelates(originDto, getBasicCorrelate(CorrelateConstants.ServiceType.DELETE));
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REMOVE, originDto);
            }
        }
    }

    /**
     * 批量操作 - 开始处理
     *
     * @param operate 服务层 - 操作类型
     * @param newList 新数据对象集合（删除时不存在）
     * @param idList  Id集合（非删除时不存在）
     */
    protected List<D> startBatchHandle(OperateConstants.ServiceType operate, Collection<D> newList, Collection<? extends Serializable> idList) {
        return switch (operate) {
            case BATCH_EDIT -> baseManager.selectListByIds(newList.stream().map(D::getId).collect(Collectors.toList()));
            case BATCH_DELETE -> baseManager.selectListByIds(idList);
            default -> new ArrayList<>();
        };
    }

    /**
     * 批量操作 - 结束处理
     *
     * @param operate    服务层 - 操作类型
     * @param rows       操作数据条数
     * @param originList 源数据对象集合（新增时不存在）
     * @param newList    新数据对象集合（删除时不存在）
     */
    protected void endBatchHandle(OperateConstants.ServiceType operate, int rows, Collection<D> originList, Collection<D> newList) {
        if (rows <= 0) {
            return;
        }
        switch (operate) {
            case BATCH_ADD -> {
                // insert correlate data
                addCorrelates(newList, getBasicCorrelate(CorrelateConstants.ServiceType.BATCH_ADD));
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newList);
            }
            case BATCH_EDIT -> {
                // update correlate data
                editCorrelates(originList, newList, getBasicCorrelate(CorrelateConstants.ServiceType.BATCH_EDIT));
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newList);
            }
            case BATCH_DELETE -> {
                // delete correlate data
                delCorrelates(originList, getBasicCorrelate(CorrelateConstants.ServiceType.BATCH_DELETE));
                refreshCache(operate, RedisConstants.OperateType.REMOVE, originList);
            }
        }
    }

    /**
     * 获取操作类型默认关联控制
     *
     * @param serviceType 操作类型
     * @return 默认关联控制
     */
    @Override
    protected C getBasicCorrelate(CorrelateConstants.ServiceType serviceType) {
        String cacheKey = RedisConstants.CacheKey.SYS_CORRELATE_IMPL_KEY.getCacheName(getClass().getName());
        if (!redisService.hasJavaKey(cacheKey)) {
            Map<CorrelateConstants.ServiceType, C> correlateMap = defaultCorrelate();
            Map<String, C> cacheMap = correlateMap.keySet().stream().collect(Collectors.toMap(CorrelateConstants.ServiceType::getCode, correlateMap::get, (v1, v2) -> v1));
            redisService.setJavaCacheMap(cacheKey, cacheMap);
        }
        C correlate = redisService.getJavaCacheMapValue(cacheKey, serviceType.getCode());
        return switch (serviceType) {
            case SELECT, ADD, EDIT, DELETE, CACHE_REFRESH, EDIT_STATUS -> correlate;
            case SELECT_LIST, SELECT_ID_LIST, SELECT_ID_SINGLE ->
                    ObjectUtil.isNotNull(correlate) ? correlate : redisService.getJavaCacheMapValue(cacheKey, CorrelateConstants.ServiceType.SELECT.getCode());
            case BATCH_ADD ->
                    ObjectUtil.isNotNull(correlate) ? correlate : redisService.getJavaCacheMapValue(cacheKey, CorrelateConstants.ServiceType.ADD.getCode());
            case BATCH_EDIT ->
                    ObjectUtil.isNotNull(correlate) ? correlate : redisService.getJavaCacheMapValue(cacheKey, CorrelateConstants.ServiceType.EDIT.getCode());
            case BATCH_DELETE ->
                    ObjectUtil.isNotNull(correlate) ? correlate : redisService.getJavaCacheMapValue(cacheKey, CorrelateConstants.ServiceType.DELETE.getCode());
        };
    }
}
