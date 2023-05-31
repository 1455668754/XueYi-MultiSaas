package com.xueyi.common.web.entity.service.impl;

import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.utils.core.ObjectUtil;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.core.web.vo.TreeSelect;
import com.xueyi.common.web.correlate.service.CorrelateService;
import com.xueyi.common.web.entity.manager.ITreeManager;
import com.xueyi.common.web.entity.service.ITreeService;
import com.xueyi.common.web.entity.service.impl.handle.TreeServiceHandle;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务层 树型实现通用数据处理
 *
 * @param <Q>   Query
 * @param <D>   Dto
 * @param <C>   Correlate
 * @param <IDG> DtoIManager
 * @author xueyi
 */
public class TreeServiceImpl<Q extends TreeEntity<D>, D extends TreeEntity<D>, C extends Enum<? extends Enum<?>> & CorrelateService, IDG extends ITreeManager<Q, D>> extends TreeServiceHandle<Q, D, C, IDG> implements ITreeService<Q, D> {

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
     * 根据Id集合查询节点及子节点（批量）
     *
     * @param idList Id集合
     * @return 节点及子节点数据对象集合
     */
    @Override
    public List<D> selectChildListByIds(Collection<? extends Serializable> idList) {
        return baseManager.selectChildListByIds(idList);
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
