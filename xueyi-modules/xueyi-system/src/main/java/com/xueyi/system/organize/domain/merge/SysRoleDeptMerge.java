package com.xueyi.system.organize.domain.merge;


import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 角色-部门关联（权限范围） 持久化对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_dept_merge")
public class SysRoleDeptMerge extends TBasisEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 角色Id */
    private Long roleId;

    /** 部门Id */
    private Long deptId;

    public SysRoleDeptMerge(Long roleId, Long deptId) {
        this.roleId = roleId;
        this.deptId = deptId;
    }

}