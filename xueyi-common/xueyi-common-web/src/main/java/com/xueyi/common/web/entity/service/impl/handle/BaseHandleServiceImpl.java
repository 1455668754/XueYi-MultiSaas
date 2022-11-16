package com.xueyi.common.web.entity.service.impl.handle;

import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.redis.service.DictService;
import com.xueyi.common.web.entity.manager.IBaseManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Arrays;
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
    protected DictService dictService;

    /**
     * 缓存主键命名定义
     */
    protected String getCacheKey() {
        return null;
    }

    /**
     * 缓存更新
     *
     * @param operateType 操作类型
     * @param ids         数据对象
     */
    protected void refreshCache(RedisConstants.OperateType operateType, Serializable... ids) {
        // 校验是否启动缓存管理
        if (StrUtil.isNotEmpty(getCacheKey()))
            return;
        List<D> dtoList;
        switch (operateType) {
            case REFRESH_ALL:
                dtoList = baseManager.selectList(null);
                dictService.refreshMapCache(getCacheKey(), dtoList, D::getIdStr, D -> D);
                break;
            case REFRESH:
                dtoList = baseManager.selectListByIds(Arrays.asList(ids));
                dtoList.forEach(item -> dictService.refreshMapValueCache(getCacheKey(), item::getIdStr, () -> item));
                break;
            case REMOVE:
                dictService.removeMapValueCache(getCacheKey(), ids);
                break;
        }
    }

    /**
     * 新增操作处理
     *
     * @param row        新增操作数据量
     * @param dto        数据对象
     * @param entityList 数据对象集合
     */
    protected void addHandle(int row, D dto, Collection<D> entityList) {
        if (row > 0) {
            if (ObjectUtil.isNotNull(dto))
                refreshCache(RedisConstants.OperateType.REFRESH, dto.getId());
            else
                refreshCache(RedisConstants.OperateType.REFRESH, entityList.stream().map(BasisEntity::getId).toArray());
        }
    }

    /**
     * 修改操作处理
     *
     * @param row        修改操作数据量
     * @param dto        数据对象
     * @param entityList 数据对象集合
     */
    protected void editHandle(int row, D dto, Collection<D> entityList) {
        if (row > 0) {
            if (ObjectUtil.isNotNull(dto))
                refreshCache(RedisConstants.OperateType.REFRESH, dto.getId());
            else
                refreshCache(RedisConstants.OperateType.REFRESH, entityList.stream().map(BasisEntity::getId).toArray());
        }
    }

    /**
     * 修改状态操作处理
     *
     * @param row 修改状态操作数据量
     * @param ids Id集合
     */
    protected void editStatusHandle(int row, Serializable... ids) {
        if (row > 0) {
            refreshCache(RedisConstants.OperateType.REFRESH, ids);
        }
    }

    /**
     * 删除操作处理
     *
     * @param row 删除操作数据量
     * @param ids Id集合
     */
    protected void deleteHandle(int row, Serializable... ids) {
        if (row > 0) {
            refreshCache(RedisConstants.OperateType.REMOVE, ids);
        }
    }
}
