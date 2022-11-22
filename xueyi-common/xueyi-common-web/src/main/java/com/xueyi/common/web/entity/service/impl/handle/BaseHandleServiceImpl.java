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
     * @param dtoList      数据对象集合
     */
    protected void refreshCache(OperateConstants.ServiceType operate, RedisConstants.OperateType operateCache, D dto, Collection<D> dtoList) {
        // 校验是否启动缓存管理
        if (StrUtil.isEmpty(getCacheKey()))
            return;
        switch (operateCache) {
            case REFRESH_ALL:
                List<D> allList = baseManager.selectList(null);
                redisService.deleteObject(getCacheKey());
                redisService.refreshMapCache(getCacheKey(), allList, D::getIdStr, D -> D);
                break;
            case REFRESH:
                if (operate.isSingle())
                    redisService.refreshMapValueCache(getCacheKey(), dto::getIdStr, () -> dto);
                else if (operate.isBatch())
                    dtoList.forEach(item -> redisService.refreshMapValueCache(getCacheKey(), item::getIdStr, () -> item));
                break;
            case REMOVE:
                if (operate.isSingle())
                    redisService.removeMapValueCache(getCacheKey(), dto.getId());
                else if (operate.isBatch())
                    redisService.removeMapValueCache(getCacheKey(), dtoList.stream().map(D::getIdStr).toArray());
                break;
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
     * @param operate 服务层 - 操作类型
     * @param row     操作数据条数
     * @param dto     数据对象
     */
    protected void endHandle(OperateConstants.ServiceType operate, int row, D dto) {
        if (row > 0) {
            switch (operate) {
                case ADD:
                case EDIT:
                case EDIT_STATUS:
                    refreshCache(operate, RedisConstants.OperateType.REFRESH, dto, null);
                    break;
                case DELETE:
                    refreshCache(operate, RedisConstants.OperateType.REMOVE, dto, null);
                    break;
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
     * @param entityList 数据对象集合
     */
    protected void endBatchHandle(OperateConstants.ServiceType operate, int rows, Collection<D> entityList) {
        if (rows > 0) {
            switch (operate) {
                case BATCH_ADD:
                case BATCH_EDIT:
                    refreshCache(operate, RedisConstants.OperateType.REFRESH, null, entityList);
                    break;
                case BATCH_DELETE:
                    refreshCache(operate, RedisConstants.OperateType.REMOVE, null, entityList);
                    break;
            }
        }
    }
}
