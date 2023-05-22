package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.cache.constant.CacheConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.TypeUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.web.correlate.utils.CorrelateUtil;
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
    protected CacheConstants.CacheType getCacheKey() {
        return null;
    }

    /**
     * 设置请求关联映射
     */
    protected void startCorrelates(Enum<? extends Enum<?>> correlateEnum) {
        CorrelateUtil.startCorrelates(correlateEnum);
    }

    /**
     * 清理关联映射的线程变量
     */
    protected void clearCorrelates() {
        CorrelateUtil.clearCorrelates();
    }

    /**
     * 数据映射关联 | 查询
     *
     * @param dto 数据对象
     * @return 数据对象
     */
    protected D subCorrelates(D dto) {
        return CorrelateUtil.subCorrelates(dto);
    }

    /**
     * 数据映射关联 | 查询（批量）
     *
     * @param dtoList 数据对象集合
     * @return 数据对象集合
     */
    protected List<D> subCorrelates(List<D> dtoList) {
        return CorrelateUtil.subCorrelates(dtoList);
    }

    /**
     * 数据映射关联 | 新增
     *
     * @param dto 数据对象
     * @return 结果
     */
    protected int addCorrelates(D dto) {
        return CorrelateUtil.addCorrelates(dto);
    }

    /**
     * 数据映射关联 | 新增（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    protected int addCorrelates(Collection<D> dtoList) {
        return CorrelateUtil.addCorrelates(dtoList);
    }

    /**
     * 数据映射关联 | 修改
     *
     * @param originDto 源数据对象
     * @param newDto    新数据对象
     * @return 结果
     */
    protected int editCorrelates(D originDto, D newDto) {
        return CorrelateUtil.editCorrelates(originDto, newDto);
    }

    /**
     * 数据映射关联 | 修改（批量）
     *
     * @param originList 源数据对象集合
     * @param newList    新数据对象集合
     * @return 结果
     */
    protected int editCorrelates(Collection<D> originList, Collection<D> newList) {
        return CorrelateUtil.editCorrelates(originList, newList);
    }

    /**
     * 数据映射关联 | 删除
     *
     * @param dto 数据对象
     * @return 结果
     */
    protected int delCorrelates(D dto) {
        return CorrelateUtil.delCorrelates(dto);
    }

    /**
     * 数据映射关联 | 删除（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    protected int delCorrelates(Collection<D> dtoList) {
        return CorrelateUtil.delCorrelates(dtoList);
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
        if (ObjectUtil.isNull(getCacheKey())) {
            return;
        }
        switch (operateCache) {
            case REFRESH_ALL -> {
                List<D> allList = baseManager.selectListMerge(null);
                redisService.deleteObject(getCacheKey().getCode());
                redisService.refreshMapCache(getCacheKey().getCode(), allList, D::getIdStr, D -> D);
            }
            case REFRESH -> {
                if (operate.isSingle()) {
                    redisService.refreshMapValueCache(getCacheKey().getCode(), dto::getIdStr, () -> dto);
                } else if (operate.isBatch()) {
                    dtoList.forEach(item -> redisService.refreshMapValueCache(getCacheKey().getCode(), item::getIdStr, () -> item));
                }
            }
            case REMOVE -> {
                if (operate.isSingle()) {
                    redisService.removeMapValueCache(getCacheKey().getCode(), dto.getId());
                } else if (operate.isBatch()) {
                    redisService.removeMapValueCache(getCacheKey().getCode(), dtoList.stream().map(D::getIdStr).toArray(String[]::new));
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
        if (row <= 0) {
            return;
        }
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
        if (rows <= 0) {
            return;
        }
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
