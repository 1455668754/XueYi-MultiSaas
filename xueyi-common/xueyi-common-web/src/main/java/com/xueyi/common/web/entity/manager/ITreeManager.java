package com.xueyi.common.web.entity.manager;

import com.xueyi.common.core.web.entity.base.TreeEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 数据封装层 树型通用数据处理
 *
 * @param <Q> Query
 * @param <D> Dto
 * @author xueyi
 */
public interface ITreeManager<Q extends TreeEntity<D>, D extends TreeEntity<D>> extends IBaseManager<Q, D> {

    /**
     * 根据Id查询本节点及其所有祖籍节点
     *
     * @param id Id
     * @return 本节点及其所有祖籍节点数据对象集合
     */
    List<D> selectAncestorsListById(Serializable id);

    /**
     * 根据Id查询本节点及其所有子节点
     *
     * @param id Id
     * @return 本节点及其所有子节点数据对象集合
     */
    List<D> selectChildListById(Serializable id);

    /**
     * 根据Id修改其子节点的状态
     *
     * @param id     Id
     * @param status 状态
     * @return 结果
     */
    int updateChildrenStatus(Serializable id, String status);

    /**
     * 根据Id修改其子节点的祖籍
     *
     * @param id           Id
     * @param newAncestors 新祖籍
     * @param oldAncestors 旧祖籍
     * @return 结果
     */
    int updateChildrenAncestors(Serializable id, String newAncestors, String oldAncestors);

    /**
     * 根据Id修改其子节点的祖籍和状态
     *
     * @param id           Id
     * @param status       状态
     * @param newAncestors 新祖籍
     * @param oldAncestors 旧祖籍
     * @return 结果
     */
    int updateChildren(Serializable id, String status, String newAncestors, String oldAncestors);

    /**
     * 根据Id删除其子节点
     *
     * @param id Id
     * @return 结果
     */
    int deleteChildren(Serializable id);

    /**
     * 校验是否为父级的子级
     *
     * @param id       Id
     * @param parentId 父级Id
     * @return 结果 | true/false 是/否
     */
    D checkIsChild(Serializable id, Serializable parentId);

    /**
     * 校验是否存在子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    D checkHasChild(Serializable id);

    /**
     * 校验是否有启用(正常状态)的子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    D checkHasNormalChild(Serializable id);

    /**
     * 校验名称是否唯一
     *
     * @param id       Id
     * @param parentId 父级Id
     * @param name     名称
     * @return 数据对象
     */
    D checkNameUnique(Serializable id, Serializable parentId, String name);
}
