package com.xueyi.system.organize.domain.merge;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.annotation.Correlations;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.system.OrganizeConstants;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.xueyi.system.api.organize.domain.merge.MergeGroup.*;

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
    @Correlation(groupName = DEPT_OrganizeRoleMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_MAIN)
    private Long deptId;

    /** 岗位Id */
    @Correlation(groupName = POST_OrganizeRoleMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_MAIN)
    private Long postId;

    /** 用户Id */
    @Correlation(groupName = USER_OrganizeRoleMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_MAIN)
    private Long userId;

    /** 角色Id */
    @Correlations({
            @Correlation(groupName = DEPT_OrganizeRoleMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_SLAVE),
            @Correlation(groupName = POST_OrganizeRoleMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_SLAVE),
            @Correlation(groupName = USER_OrganizeRoleMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_SLAVE)
    })
    private Long roleId;

    public SysOrganizeRoleMerge(Long organizeId, Long roleId, OrganizeConstants.OrganizeType organizeType) {
        switch (organizeType) {
            case DEPT -> this.deptId = organizeId;
            case POST -> this.postId = organizeId;
            case USER -> this.userId = organizeId;
        }
        this.roleId = roleId;
    }

}
