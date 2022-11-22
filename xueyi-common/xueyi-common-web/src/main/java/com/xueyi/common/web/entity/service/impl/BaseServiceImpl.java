package com.xueyi.common.web.entity.service.impl;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.exception.UtilException;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.redis.constant.RedisConstants;
import com.xueyi.common.web.entity.manager.IBaseManager;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.common.web.entity.service.impl.handle.BaseHandleServiceImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务层 基类实现通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <IDG> DtoIManager
 * @author xueyi
 */
public class BaseServiceImpl<Q extends BaseEntity, D extends BaseEntity, IDG extends IBaseManager<Q, D>> extends BaseHandleServiceImpl<Q, D, IDG> implements IBaseService<Q, D> {

    /**
     * 查询数据对象列表
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectList(Q query) {
        return baseManager.selectList(query);
    }

    /**
     * 查询数据对象列表 | 附加数据
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListExtra(Q query) {
        return baseManager.selectListExtra(query);
    }

    /**
     * 查询数据对象列表 | 数据权限 | 附加数据
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListScope(Q query) {
        return baseManager.selectListExtra(query);
    }

    /**
     * 根据Id集合查询数据对象列表
     *
     * @param idList Id集合
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListByIds(Collection<? extends Serializable> idList) {
        return baseManager.selectListByIds(idList);
    }

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public D selectById(Serializable id) {
        return baseManager.selectById(id);
    }

    /**
     * 根据Id查询单条数据对象 | 附加数据
     *
     * @param id Id
     * @return 数据对象
     */
    @Override
    public D selectByIdExtra(Serializable id) {
        return baseManager.selectByIdExtra(id);
    }

    /**
     * 新增数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    @Override
    public int insert(D d) {
        startHandle(OperateConstants.ServiceType.ADD, null, d);
        int row = baseManager.insert(d);
        endHandle(OperateConstants.ServiceType.ADD, row, d);
        return row;
    }

    /**
     * 新增数据对象（批量）
     *
     * @param entityList 数据对象集合
     */
    @Override
    public int insertBatch(Collection<D> entityList) {
        startBatchHandle(OperateConstants.ServiceType.BATCH_ADD, null, entityList);
        int rows = baseManager.insertBatch(entityList);
        endBatchHandle(OperateConstants.ServiceType.BATCH_ADD, rows, entityList);
        return rows;
    }

    /**
     * 修改数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    @Override
    public int update(D d) {
        D originDto = baseManager.selectById(d.getId());
        startHandle(OperateConstants.ServiceType.EDIT, originDto, d);
        int row = baseManager.update(d);
        endHandle(OperateConstants.ServiceType.EDIT, row, d);
        return row;
    }

    /**
     * 修改数据对象（批量）
     *
     * @param entityList 数据对象集合
     */
    @Override
    public int updateBatch(Collection<D> entityList) {
        List<D> originList = baseManager.selectListByIds(entityList.stream().map(D::getId).collect(Collectors.toList()));
        startBatchHandle(OperateConstants.ServiceType.BATCH_EDIT, originList, entityList);
        int rows = baseManager.updateBatch(entityList);
        endBatchHandle(OperateConstants.ServiceType.BATCH_EDIT, rows, entityList);
        return rows;
    }

    /**
     * 修改数据对象状态
     *
     * @param d 数据对象
     * @return 结果
     */
    @Override
    public int updateStatus(D d) {
        D originDto = baseManager.selectById(d.getId());
        startHandle(OperateConstants.ServiceType.EDIT_STATUS, originDto, d);
        int row = baseManager.updateStatus(d);
        endHandle(OperateConstants.ServiceType.EDIT_STATUS, row, d);
        return row;
    }

    /**
     * 根据Id删除数据对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    public int deleteById(Serializable id) {
        D originDto = baseManager.selectById(id);
        startHandle(OperateConstants.ServiceType.DELETE, originDto, null);
        int row = baseManager.deleteById(id);
        endHandle(OperateConstants.ServiceType.DELETE, row, originDto);
        return row;
    }

    /**
     * 根据Id集合删除数据对象
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    public int deleteByIds(Collection<? extends Serializable> idList) {
        List<D> originList = baseManager.selectListByIds(idList);
        startBatchHandle(OperateConstants.ServiceType.BATCH_DELETE, originList, null);
        int rows = baseManager.deleteByIds(idList);
        endBatchHandle(OperateConstants.ServiceType.BATCH_DELETE, rows, originList);
        return rows;
    }

    /**
     * 校验名称是否唯一
     *
     * @param id   Id
     * @param name 名称
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkNameUnique(Serializable id, String name) {
        return ObjectUtil.isNotNull(baseManager.checkNameUnique(ObjectUtil.isNull(id) ? BaseConstants.NONE_ID : id, name));
    }

    /**
     * 根据Id查询数据对象状态
     *
     * @param id Id
     * @return 结果 | NORMAL 正常 | DISABLE 停用 | EXCEPTION 异常（值不存在）
     */
    @Override
    public BaseConstants.Status checkStatus(Serializable id) {
        D info = ObjectUtil.isNotNull(id) ? baseManager.selectById(id) : null;
        return ObjectUtil.isNull(info)
                ? BaseConstants.Status.EXCEPTION
                : StrUtil.equals(BaseConstants.Status.NORMAL.getCode(), info.getStatus())
                ? BaseConstants.Status.NORMAL
                : BaseConstants.Status.DISABLE;
    }

    /**
     * 更新缓存数据
     */
    @Override
    public void refreshCache() {
        if (StrUtil.isEmpty(getCacheKey()))
            throw new UtilException("未正常配置缓存，无法使用!");
        refreshCache(null, RedisConstants.OperateType.REFRESH_ALL, null, null);
    }
}