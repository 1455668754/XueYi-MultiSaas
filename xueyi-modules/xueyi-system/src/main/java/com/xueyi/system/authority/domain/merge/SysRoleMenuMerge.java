package com.xueyi.system.authority.domain.merge;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.annotation.Correlations;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.xueyi.system.api.authority.domain.merge.MergeGroup.MENU_SysRoleMenuMerge_GROUP;
import static com.xueyi.system.api.authority.domain.merge.MergeGroup.ROLE_SysRoleMenuMerge_GROUP;

/**
 * 角色-菜单关联 持久化对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_menu_merge")
public class SysRoleMenuMerge extends TBasisEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 角色Id */
    @Correlations({
            @Correlation(groupName = ROLE_SysRoleMenuMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_MAIN),
            @Correlation(groupName = MENU_SysRoleMenuMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_SLAVE)
    })
    private Long roleId;

    /** 菜单Id */
    @Correlations({
            @Correlation(groupName = ROLE_SysRoleMenuMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_SLAVE),
            @Correlation(groupName = MENU_SysRoleMenuMerge_GROUP, keyType = OperateConstants.SubKeyType.MERGE_MAIN)
    })
    private Long menuId;

    public SysRoleMenuMerge(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

}
