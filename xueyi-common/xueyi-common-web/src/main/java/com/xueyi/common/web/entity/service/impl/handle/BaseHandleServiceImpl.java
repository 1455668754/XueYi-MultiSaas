package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.utils.core.TypeUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.web.entity.manager.IBaseManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**
 * 服务层 操作方法 基类实现通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <IDG> DtoIManager
 * @author xueyi
 */
public class BaseHandleServiceImpl<Q extends BaseEntity, D extends BaseEntity, IDG extends IBaseManager<Q, D>> {

    @Autowired
    protected IDG baseManager;

    @Autowired
    protected RedisService redisService;

    /** Query泛型的类型 */
    @Getter
    private final Class<Q> QClass = TypeUtil.getClazz(getClass().getGenericSuperclass(), NumberUtil.Zero);

    /** Dto泛型的类型 */
    @Getter
    private final Class<D> DClass = TypeUtil.getClazz(getClass().getGenericSuperclass(), NumberUtil.One);

    /**
     * 缓存主键命名定义
     */
    protected String getCacheKey() {
        return null;
    }

    /**
     * 缓存更新
     *
     * @param operate      服务层 - 操作类型
     * @param operateCache 缓存操作类型
     * @param dto          数据对象
     */
    protected void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, D dto) {
        refreshCache(operate, operateCache, dto, null);
    }

    /**
     * 缓存更新
     *
     * @param operate      服务层 - 操作类型
     * @param operateCache 缓存操作类型
     * @param dtoList      数据对象集合
     */
    protected void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, Collection<D> dtoList) {
        refreshCache(operate, operateCache, null, dtoList);
    }

    /**
     * 缓存更新
     *
     * @param operate      服务层 - 操作类型
     * @param operateCache 缓存操作类型
     * @param dto          数据对象
     * @param dtoList      数据对象集合
     */
    protected void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, D dto, Collection<D> dtoList) {
        // 校验是否启动缓存管理
        if (StrUtil.isEmpty(getCacheKey())) {
            return;
        }
        switch (operateCache) {
            case REFRESH_ALL -> {
                List<D> allList = baseManager.selectList(null);
                redisService.deleteObject(getCacheKey());
                redisService.refreshMapCache(getCacheKey(), allList, D::getIdStr, D -> D);
            }
            case REFRESH -> {
                if (operate.isSingle()) {
                    redisService.refreshMapValueCache(getCacheKey(), dto::getIdStr, () -> dto);
                } else if (operate.isBatch()) {
                    dtoList.forEach(item -> redisService.refreshMapValueCache(getCacheKey(), item::getIdStr, () -> item));
                }
            }
            case REMOVE -> {
                if (operate.isSingle()) {
                    redisService.removeMapValueCache(getCacheKey(), dto.getId());
                } else if (operate.isBatch()) {
                    redisService.removeMapValueCache(getCacheKey(), dtoList.stream().map(D::getIdStr).toArray(String[]::new));
                }
            }
        }
    }

    /**
     * 单条操作 - 开始处理
     *
     * @param operate   服务层 - 操作类型
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    protected void startHandle(OperateConstants.ServiceType operate, D originDto, D newDto) {
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
        if (row <= 0)
            return;
        switch (operate) {
            case ADD -> {
                // insert merge data
                baseManager.insertMerge(newDto);
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newDto);
            }
            case EDIT -> {
                // update merge data
                baseManager.updateMerge(originDto, newDto);
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newDto);
            }
            case EDIT_STATUS -> refreshCache(operate, RedisConstants.OperateType.REFRESH, newDto);
            case DELETE -> {
                // delete merge data
                baseManager.deleteMerge(originDto);
                // refresh cache
                refreshCache(operate, RedisConstants.OperateType.REMOVE, originDto);
            }
        }
    }

    /**
     * 批量操作 - 开始处理
     *
     * @param operate    服务层 - 操作类型
     * @param originList 源数据对象集合（新增时不存在）
     * @param newList    新数据对象集合（删除时不存在）
     */
    protected void startBatchHandle(OperateConstants.ServiceType operate, Collection<D> originList, Collection<D> newList) {
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
        if (rows <= 0)
            return;
        switch (operate) {
            case BATCH_ADD -> {
                baseManager.insertMerge(newList);
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newList);
            }
            case BATCH_EDIT -> {
                baseManager.updateMerge(originList, newList);
                refreshCache(operate, RedisConstants.OperateType.REFRESH, newList);
            }
            case BATCH_DELETE -> {
                baseManager.deleteMerge(originList);
                refreshCache(operate, RedisConstants.OperateType.REMOVE, originList);
            }
        }
    }

}
