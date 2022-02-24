package com.xueyi.system.organize.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.system.organize.domain.vo.SysOrganizeTree;
import com.xueyi.system.organize.manager.SysOrganizeManager;
import com.xueyi.system.organize.service.ISysOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 组织管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysOrganizeServiceImpl implements ISysOrganizeService {

    @Autowired
    private SysOrganizeManager organizeManager;

    /**
     * 获取企业部门|岗位树
     *
     * @return 组织对象集合
     */
    @Override
    public List<SysOrganizeTree> selectOrganizeScope() {
        return organizeManager.selectOrganizeScope();
    }

    /**
     * 获取角色组织Ids
     *
     * @param roleId 角色Id
     * @return 组织Ids
     */
    @Override
    public Long[] selectRoleOrganizeMerge(Long roleId) {
        return organizeManager.selectRoleOrganizeMerge(roleId);
    }

    /**
     * 根据部门Id获取关联的角色Ids
     *
     * @param deptId 部门Id
     * @return 角色Ids
     */
    @Override
    public Long[] selectDeptRoleMerge(Long deptId) {
        return organizeManager.selectDeptRoleMerge(deptId);
    }

    /**
     * 根据岗位Id获取关联的角色Ids
     *
     * @param postId 岗位Id
     * @return 角色Ids
     */
    @Override
    public Long[] selectPostRoleMerge(Long postId) {
        return organizeManager.selectPostRoleMerge(postId);
    }

    /**
     * 根据用户Id获取关联的角色Ids
     *
     * @param userId 用户Id
     * @return 角色Ids
     */
    @Override
    public Long[] selectUserRoleMerge(Long userId) {
        return organizeManager.selectUserRoleMerge(userId);
    }

    /**
     * 新增角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    @Override
    public void addRoleOrganizeMerge(Long roleId, Long[] organizeIds) {
        organizeManager.addRoleOrganizeMerge(roleId, organizeIds);
    }


    /**
     * 修改角色组织权限
     *
     * @param roleId      角色Id
     * @param organizeIds 组织Ids
     */
    @Override
    public void editRoleOrganizeMerge(Long roleId, Long[] organizeIds) {
        organizeManager.editRoleOrganizeMerge(roleId, organizeIds);
    }

    /**
     * 修改部门的角色关联数据
     *
     * @param deptId  部门Id
     * @param roleIds 角色Ids
     */
    @Override
    public void editDeptRoleMerge(Long deptId, Long[] roleIds) {
        organizeManager.editDeptRoleMerge(deptId, roleIds);
    }

    /**
     * 修改岗位的角色关联数据
     *
     * @param postId  岗位Id
     * @param roleIds 角色Ids
     */
    @Override
    public void editPostIdRoleMerge(Long postId, Long[] roleIds) {
        organizeManager.editPostIdRoleMerge(postId, roleIds);
    }

    /**
     * 修改用户的角色关联数据
     *
     * @param userId  用户Id
     * @param roleIds 角色Ids
     */
    @Override
    public void editUserRoleMerge(Long userId, Long[] roleIds) {
        organizeManager.editUserRoleMerge(userId, roleIds);
    }
}
