package com.xueyi.system.organize.manager;

import com.xueyi.system.organize.domain.vo.SysOrganizeTree;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 系统服务 | 组织模块 | 组织管理 数据封装层
 *
 * @author xueyi
 */
public interface ISysOrganizeManager {

    /**
     * 登录校验 | 根据角色Ids获取关联部门
     *
     * @param roleIds 角色Ids
     * @return 部门Id集合
     */
    Set<Long> selectRoleDeptSetByRoleIds(Collection<Long> roleIds);

    /**
     * 登录校验 | 根据角色Ids获取关联岗位
     *
     * @param roleIds 角色Ids
     * @return 岗位Id集合
     */
    Set<Long> selectRolePostSetByRoleIds(Collection<Long> roleIds);

    /**
     * 登录校验 | 根据岗位Ids获取归属用户Ids集合
     *
     * @param postIds 岗位Ids
     * @return 用户Ids集合
     */
    Set<Long> selectUserSetByPostIds(Collection<Long> postIds);

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    List<SysOrganizeTree> selectOrganizeScope();
}
