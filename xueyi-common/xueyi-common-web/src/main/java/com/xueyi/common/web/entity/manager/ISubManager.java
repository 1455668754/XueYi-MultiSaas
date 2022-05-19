package com.xueyi.common.web.entity.manager;

import com.xueyi.common.core.web.entity.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 数据封装层 主子型通用数据处理
 *
 * @param <Q>  Query
 * @param <D>  Dto
 * @param <SQ> SubQuery
 * @param <SD> SubDto
 * @author xueyi
 */
public interface ISubManager<Q extends BaseEntity, D extends BaseEntity, SQ extends BaseEntity, SD extends BaseEntity> {

    /**
     * 根据Id查询单条数据对象 | 包含子数据
     *
     * @param query 数据查询对象
     * @return 数据对象集合
     */
    // 待优化 联表更新后
    List<D> selectListExtra(Q query);

    /**
     * 根据Id查询单条数据对象 | 包含子数据
     *
     * @param id Id
     * @return 数据对象
     */
    D selectByIdExtra(Serializable id);

    /**
     * 根据外键查询子数据对象集合 | 子数据
     *
     * @param foreignKey 外键
     * @return 子数据对象集合
     */
    List<SD> selectSubByForeignKey(Serializable foreignKey);

    /**
     * 根据Id修改其归属数据的状态
     *
     * @param foreignKey 子表外键值
     * @param status     状态
     * @return 结果
     */
    int updateSubStatus(Serializable foreignKey, String status);

    /**
     * 根据Id删除其归属数据
     *
     * @param foreignKey 子表外键值
     * @return 结果
     */
    int deleteSub(Serializable foreignKey);

    /**
     * 校验是否存在子数据
     *
     * @param foreignKey 子表外键值
     * @return 子数据对象
     */
    SD checkExistSub(Serializable foreignKey);

    /**
     * 校验是否存在启用（正常状态）的子数据
     *
     * @param foreignKey 子表外键值
     * @return 子数据对象
     */
    SD checkExistNormalSub(Serializable foreignKey);
}
