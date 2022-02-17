package com.xueyi.system.authority.domain.merge;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;

/**
 * 角色-组织关联（权限范围） 持久化对象
 *
 * @author xueyi
 */
@TableName("sys_role_organize_merge")
public class SysRoleOrganizeMerge extends TBasisEntity {

    private static final long serialVersionUID = 1L;

    /** 角色Id */
    @TableField("role_id")
    private Long roleId;

    /** 组织Id（部门 | 岗位混合Id） */
    @TableField("organize_id")
    private Long organizeId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(Long organizeId) {
        this.organizeId = organizeId;
    }
}
