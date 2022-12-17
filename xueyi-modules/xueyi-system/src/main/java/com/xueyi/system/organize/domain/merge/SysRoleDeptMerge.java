package com.xueyi.system.organize.domain.merge;


import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.annotation.Correlations;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.xueyi.system.api.authority.domain.merge.MergeGroup.ROLE_SysRoleDeptMerge_GROUP;
import static com.xueyi.system.api.organize.domain.merge.MergeGroup.DEPT_SysRoleDeptMerge_GROUP;

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
    @Correlations({
            @Correlation(groupName = ROLE_SysRoleDeptMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_MAIN),
            @Correlation(groupName = DEPT_SysRoleDeptMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_SLAVE)
    })
    private Long roleId;

    /** 部门Id */
    @Correlations({
            @Correlation(groupName = ROLE_SysRoleDeptMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_SLAVE),
            @Correlation(groupName = DEPT_SysRoleDeptMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_MAIN)
    })
    private Long deptId;

    public SysRoleDeptMerge(Long roleId, Long deptId) {
        this.roleId = roleId;
        this.deptId = deptId;
    }

}