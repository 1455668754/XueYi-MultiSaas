package com.xueyi.system.organize.domain.merge;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 组织-角色关联（角色绑定） 持久化对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_organize_role_merge")
public class SysOrganizeRoleMerge extends TBasisEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 部门Id */
    private Long deptId;

    /** 岗位Id */
    private Long postId;

    /** 用户Id */
    private Long userId;

    /** 角色Id */
    private Long roleId;

    public SysOrganizeRoleMerge(Long organizeId, Long roleId, OrganizeConstants.OrganizeType organizeType) {
        switch (organizeType) {
            case DEPT:
                this.deptId = organizeId;
                break;
            case POST:
                this.postId = organizeId;
                break;
            case USER:
                this.userId = organizeId;
                break;
        }
        this.roleId = roleId;
    }

}
