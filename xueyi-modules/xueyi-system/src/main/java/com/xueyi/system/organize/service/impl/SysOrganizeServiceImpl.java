package com.xueyi.system.organize.service.impl;

import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.common.core.utils.TreeUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import com.xueyi.system.organize.manager.ISysOrganizeManager;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 系统服务 | 组织模块 | 组织管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysOrganizeServiceImpl implements ISysOrganizeService {

    @Autowired
    private ISysOrganizeManager organizeManager;

    /**
     * 登录校验 | 根据角色Ids获取关联部门
     *
     * @param roleIds 角色Ids
     * @return 部门Id集合
     */
    @Override
    public Set<Long> selectRoleDeptSetByRoleIds(Collection<Long> roleIds) {
        return organizeManager.selectRoleDeptSetByRoleIds(roleIds);
    }

    /**
     * 登录校验 | 根据角色Ids获取关联岗位
     *
     * @param roleIds 角色Ids
     * @return 岗位Id集合
     */
    @Override
    public Set<Long> selectRolePostSetByRoleIds(Collection<Long> roleIds) {
        return organizeManager.selectRolePostSetByRoleIds(roleIds);
    }

    /**
     * 登录校验 | 根据岗位Ids获取归属用户Ids集合
     *
     * @param postIds 岗位Ids
     * @return 用户Ids集合
     */
    @Override
    public Set<Long> selectUserSetByPostIds(Collection<Long> postIds) {
        return organizeManager.selectUserSetByPostIds(postIds);
    }

    /**
     * 获取企业部门 | 岗位树
     *
     * @return 组织对象集合
     */
    @Override
    public List<SysOrganizeTree> selectOrganizeScope() {
        return organizeManager.selectOrganizeScope();
    }

    /**
     * 获取企业部门 | 岗位树 | 移除无归属岗位的部门叶子节点
     *
     * @return 组织对象集合
     */
    @Override
    public List<SysOrganizeTree> selectOrganizeTreeExDeptNode() {
        List<SysOrganizeTree> organizeTrees = TreeUtil.buildTree(organizeManager.selectOrganizeScope());
        recursionDelLeaf(organizeTrees);
        return organizeTrees;
    }

    /**
     * 递归列表 | 移除无归属岗位的部门叶子节点
     */
    private void recursionDelLeaf(List<SysOrganizeTree> treeList) {
        SysOrganizeTree treeNode;
        for (int i = treeList.size() - 1; i >= 0; i--) {
            treeNode = treeList.get(i);
            if (CollUtil.isNotEmpty(treeNode.getChildren())) {
                recursionDelLeaf(treeNode.getChildren());
            }
            if (StrUtil.equals(treeNode.getType(), OrganizeConstants.OrganizeType.DEPT.getCode())
                    && CollUtil.isEmpty(treeNode.getChildren())) {
                treeList.remove(i);
            }
        }
    }
}
