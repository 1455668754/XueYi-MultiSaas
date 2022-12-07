package com.xueyi.common.web.entity.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.core.web.vo.TreeSelect;
import com.xueyi.common.web.entity.manager.ITreeManager;
import com.xueyi.common.web.entity.service.ITreeService;
import com.xueyi.common.web.entity.service.impl.handle.TreeHandleServiceImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务层 树型实现通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <IDG> DtoIManager
 * @author xueyi
 */
public class TreeServiceImpl<Q extends TreeEntity<D>, D extends TreeEntity<D>, IDG extends ITreeManager<Q, D>> extends TreeHandleServiceImpl<Q, D, IDG> implements ITreeService<Q, D> {

    /**
     * 根据Id查询本节点及其所有祖籍节点
     *
     * @param id Id
     * @return 本节点及其所有祖籍节点数据对象集合
     */
    @Override
    public List<D> selectAncestorsListById(Serializable id) {
        return baseManager.selectAncestorsListById(id);
    }

    /**
     * 根据Id查询本节点及其所有子节点
     *
     * @param id Id
     * @return 本节点及其所有子节点数据对象集合
     */
    @Override
    public List<D> selectChildListById(Serializable id) {
        return baseManager.selectChildListById(id);
    }

    /**
     * 新增修改数据对象
     *
     * @param d 数据对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(D d) {
        return super.insert(d);
    }

    /**
     * 修改数据对象 | 同步 修改子节点祖籍 || 停用子节点
     *
     * @param d 数据对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int update(D d) {
        return super.update(d);
    }

    /**
     * 修改数据对象状态 | 同步停用子节点
     *
     * @param d 数据对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int updateStatus(D d) {
        return super.updateStatus(d);
    }

    /**
     * 根据Id删除数据对象 | 同步删除 子节点
     *
     * @param id Id
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteById(Serializable id) {
        return super.deleteById(id);
    }

    /**
     * 根据Id集合删除数据对象 | 同步删除 子节点
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteByIds(Collection<? extends Serializable> idList) {
        return super.deleteByIds(idList);
    }

    /**
     * 校验是否为父级的子级
     *
     * @param id       Id
     * @param parentId 父级Id
     * @return 结果 | true/false 是/否
     */
    @Override
    public boolean checkIsChild(Serializable id, Serializable parentId) {
        return ObjectUtil.isNotNull(baseManager.checkIsChild(id, parentId));
    }

    /**
     * 校验是否存在子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    @Override
    public boolean checkHasChild(Serializable id) {
        return ObjectUtil.isNotNull(baseManager.checkHasChild(id));
    }

    /**
     * 校验是否有启用(正常状态)的子节点
     *
     * @param id Id
     * @return 结果 | true/false 有/无
     */
    @Override
    public boolean checkHasNormalChild(Serializable id) {
        return ObjectUtil.isNotNull(baseManager.checkHasNormalChild(id));
    }

    /**
     * 根据Id查询数据对象状态 | 顶级节点默认一直启动
     *
     * @param id Id
     * @return 结果 | NORMAL 正常 | DISABLE 停用 | EXCEPTION 异常（值不存在）
     */
    @Override
    public BaseConstants.Status checkStatus(Serializable id) {
        if (ObjectUtil.equals(BaseConstants.TOP_ID, id))
            return BaseConstants.Status.NORMAL;
        return super.checkStatus(id);
    }

    /**
     * 校验名称是否唯一
     *
     * @param id       Id
     * @param parentId 父级Id
     * @param name     名称
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkNameUnique(Serializable id, Serializable parentId, String name) {
        return ObjectUtil.isNotNull(baseManager.checkNameUnique(ObjectUtil.isNull(id) ? BaseConstants.NONE_ID : id,
                ObjectUtil.isNull(parentId) ? BaseConstants.NONE_ID : parentId, name));
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param list 数据集合
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect<D>> buildTreeSelect(List<D> list) {
        List<D> deptTrees = TreeUtil.buildTree(list);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

}
