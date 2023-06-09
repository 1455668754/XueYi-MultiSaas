package com.xueyi.common.web.entity.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
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
     * 查询数据对象列表 | 数据权限 | 附加数据
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    @Override
    public List<D> selectListScope(Q query) {
        return baseManager.selectList(query);
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
     * 新增数据对象
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(D dto) {
        startHandle(OperateConstants.ServiceType.ADD, null, dto);
        int row = baseManager.insert(dto);
        endHandle(OperateConstants.ServiceType.ADD, row, null, dto);
        return row;
    }

    /**
     * 新增数据对象（批量）
     *
     * @param dtoList 数据对象集合
     */
    @Override
    public int insertBatch(Collection<D> dtoList) {
        startBatchHandle(OperateConstants.ServiceType.BATCH_ADD, null, dtoList);
        int rows = baseManager.insertBatch(dtoList);
        endBatchHandle(OperateConstants.ServiceType.BATCH_ADD, rows, null, dtoList);
        return rows;
    }

    /**
     * 修改数据对象
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int update(D dto) {
        D originDto = baseManager.selectById(dto.getId());
        startHandle(OperateConstants.ServiceType.EDIT, originDto, dto);
        int row = baseManager.update(dto);
        endHandle(OperateConstants.ServiceType.EDIT, row, originDto, dto);
        return row;
    }

    /**
     * 修改数据对象（批量）
     *
     * @param dtoList 数据对象集合
     */
    @Override
    public int updateBatch(Collection<D> dtoList) {
        List<D> originList = baseManager.selectListByIds(dtoList.stream().map(D::getId).collect(Collectors.toList()));
        startBatchHandle(OperateConstants.ServiceType.BATCH_EDIT, originList, dtoList);
        int rows = baseManager.updateBatch(dtoList);
        endBatchHandle(OperateConstants.ServiceType.BATCH_EDIT, rows, originList, dtoList);
        return rows;
    }

    /**
     * 修改数据对象状态
     *
     * @param dto 数据对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int updateStatus(D dto) {
        D originDto = baseManager.selectById(dto.getId());
        startHandle(OperateConstants.ServiceType.EDIT_STATUS, originDto, dto);
        int row = baseManager.updateStatus(dto);
        endHandle(OperateConstants.ServiceType.EDIT_STATUS, row, originDto, dto);
        return row;
    }

    /**
     * 根据Id删除数据对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteById(Serializable id) {
        D originDto = baseManager.selectById(id);
        startHandle(OperateConstants.ServiceType.DELETE, originDto, null);
        int row = baseManager.deleteById(id);
        endHandle(OperateConstants.ServiceType.DELETE, row, originDto, null);
        return row;
    }

    /**
     * 根据Id删除数据对象（批量）
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteByIds(Collection<? extends Serializable> idList) {
        List<D> originList = baseManager.selectListByIds(idList);
        startBatchHandle(OperateConstants.ServiceType.BATCH_DELETE, originList, null);
        int rows = baseManager.deleteByIds(idList);
        endBatchHandle(OperateConstants.ServiceType.BATCH_DELETE, rows, originList, null);
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