package com.xueyi.system.api.organize.domain.merge;

/**
 * 组织管理 关联命名常量
 *
 * @author xueyi
 */
public interface MergeGroup {

    /** 岗位 -》 部门 */
    String DEPT_SysPost_GROUP = "DEPT_SysPost_GROUP";

    /** 部门 -》 岗位 */
    String POST_SysDept_GROUP = "POST_SysDept_GROUP";

    /** 组织-角色关联（角色绑定） -》 部门 */
    String DEPT_OrganizeRoleMerge_GROUP = "DEPT_OrganizeRoleMerge_GROUP";

    /** 组织-角色关联（角色绑定） -》 岗位 */
    String POST_OrganizeRoleMerge_GROUP = "POST_OrganizeRoleMerge_GROUP";

    /** 组织-角色关联（角色绑定） -》 用户 */
    String USER_OrganizeRoleMerge_GROUP = "USER_OrganizeRoleMerge_GROUP";

    /** 角色-部门关联（权限范围） -》 部门 */
    String DEPT_SysRoleDeptMerge_GROUP = "DEPT_SysRoleDeptMerge_GROUP";

    /** 角色-岗位关联（权限范围） -》 岗位 */
    String POST_SysRolePostMerge_GROUP = "SYS_POST_SysRolePostMerge_GROUP";

    /** 用户岗位关联 -》 用户 */
    String USER_SysUserPostMerge_GROUP = "USER_SysUserPostMerge_GROUP";

    /** 用户岗位关联 -》 岗位 */
    String POST_SysUserPostMerge_GROUP = "POST_SysUserPostMerge_GROUP";
}
