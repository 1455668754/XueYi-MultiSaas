package com.xueyi.common.web.entity.manager;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.correlate.domain.SqlField;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 数据封装层 基类通用数据处理
 *
 * @param <Q> Query
 * @param <D> Dto
 * @author xueyi
 */
public interface IBaseManager<Q extends BaseEntity, D extends BaseEntity> {

    /**
     * 查询数据对象列表
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    List<D> selectList(Q query);

    /**
     * 根据Id集合查询数据对象列表
     *
     * @param idList Id集合
     * @return 数据对象集合
     */
    List<D> selectListByIds(Collection<? extends Serializable> idList);

    /**
     * 根据Id查询单条数据对象
     *
     * @param id Id
     * @return 数据对象
     */
    D selectById(Serializable id);

    /**
     * 新增数据对象
     *
     * @param dto 数据对象
     * @return 结果
     */
    int insert(D dto);

    /**
     * 新增数据对象（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    int insertBatch(Collection<D> dtoList);

    /**
     * 修改数据对象
     *
     * @param dto 数据对象
     * @return 结果
     */
    int update(D dto);

    /**
     * 修改数据对象（批量）
     *
     * @param dtoList 数据对象集合
     * @return 结果
     */
    int updateBatch(Collection<D> dtoList);

    /**
     * 修改状态
     *
     * @param dto 数据对象
     * @return 结果
     */
    int updateStatus(D dto);

    /**
     * 根据Id删除数据对象
     *
     * @param id Id
     * @return 结果
     */
    int deleteById(Serializable id);

    /**
     * 根据Id集合删除数据对象（批量）
     *
     * @param idList Id集合
     * @return 结果
     */
    int deleteByIds(Collection<? extends Serializable> idList);

    /**
     * 根据动态SQL控制对象查询数据对象集合
     *
     * @param field 动态SQL控制对象
     * @return 数据对象集合
     */
    List<D> selectListByField(SqlField... field);

    /**
     * 校验名称是否唯一
     *
     * @param id   Id
     * @param name 名称
     * @return 数据对象
     */
    D checkNameUnique(Serializable id, String name);
}
