package com.xueyi.system.authority.domain.merge;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色-模块关联 持久化对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_module_merge")
public class SysRoleModuleMerge extends TBasisEntity {

    private static final long serialVersionUID = 1L;

    /** 角色Id */
    private Long roleId;

    /** 模块Id */
    private Long moduleId;

    public SysRoleModuleMerge(Long roleId, Long moduleId) {
        this.roleId = roleId;
        this.moduleId = moduleId;
    }

}
