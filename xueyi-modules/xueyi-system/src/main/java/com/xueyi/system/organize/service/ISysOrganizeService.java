package com.xueyi.system.organize.service;

import com.xueyi.system.organize.domain.vo.SysOrganizeTree;

import java.util.List;

/**
 * 组织管理 服务层
 *
 * @author xueyi
 */
public interface ISysOrganizeService {

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    List<SysOrganizeTree> selectOrganizeScope();

    /**
     * 获取角色组织Ids
     *
     * @param roleId 角色Id
     * @return 组织Ids
     */
    Long[] selectRoleOrganize(Long roleId);

    /**
     * 新增角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    void addRoleOrganize(Long roleId, Long[] organizeIds);

    /**
     * 修改角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    void editRoleOrganize(Long roleId, Long[] organizeIds);
}
