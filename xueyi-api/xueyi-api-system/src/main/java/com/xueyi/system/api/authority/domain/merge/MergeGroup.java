package com.xueyi.system.api.authority.domain.merge;

/**
 * 权限管理 关联命名常量
 *
 * @author xueyi
 */
public interface MergeGroup {

    /** 模块菜单关联 - 模块 | 菜单 */
    String MODULE_MENU_GROUP = "module_menu_direct_group";

    /** 角色-模块关联（权限范围） -》 角色 */
    String ROLE_SysRoleModuleMerge_GROUP = "ROLE_SysRoleModuleMerge_GROUP";

    /** 角色-菜单关联（权限范围） -》 角色 */
    String ROLE_SysRoleMenuMerge_GROUP = "ROLE_SysRoleMenuMerge_GROUP";

    /** 角色-部门关联（权限范围） -》 角色 */
    String ROLE_SysRoleDeptMerge_GROUP = "ROLE_SysRoleDeptMerge_GROUP";

    /** 角色-岗位关联（权限范围） -》 角色 */
    String ROLE_SysRolePostMerge_GROUP = "ROLE_SysRolePostMerge_GROUP";

    /** 组织-角色关联（角色绑定） -》 角色 */
    String ROLE_SysOrganizeRoleMerge_GROUP = "ROLE_SysOrganizeRoleMerge_GROUP";
}
