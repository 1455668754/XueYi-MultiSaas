package com.xueyi.common.web.entity.manager;

import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.web.entity.domain.SqlField;

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
     * 查询数据对象列表 | 附加数据
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    List<D> selectListExtra(Q query);

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
     * 根据Id查询单条数据对象 | 附加数据
     *
     * @param id Id
     * @return 数据对象
     */
    D selectByIdExtra(Serializable id);

    /**
     * 新增数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    int insert(D d);

    /**
     * 新增数据对象（批量）
     *
     * @param entityList 数据对象集合
     * @return 结果
     */
    int insertBatch(Collection<D> entityList);

    /**
     * 修改数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    int update(D d);

    /**
     * 修改数据对象（批量）
     *
     * @param entityList 数据对象集合
     * @return 结果
     */
    int updateBatch(Collection<D> entityList);

    /**
     * 修改状态
     *
     * @param d 数据对象
     * @return 结果
     */
    int updateStatus(D d);

    /**
     * 根据Id删除数据对象
     *
     * @param id Id
     * @return 结果
     */
    int deleteById(Serializable id);

    /**
     * 根据Id集合批量删除数据对象
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
     * 根据动态SQL控制对象查询数据对象
     *
     * @param field 动态SQL控制对象
     * @return 数据对象
     */
    D selectByField(SqlField... field);

    /**
     * 根据动态SQL控制对象更新数据对象
     *
     * @param field 动态SQL控制对象
     * @return 结果
     */
    int updateByField(SqlField... field);

    /**
     * 校验名称是否唯一
     *
     * @param id   Id
     * @param name 名称
     * @return 数据对象
     */
    D checkNameUnique(Serializable id, String name);
}
