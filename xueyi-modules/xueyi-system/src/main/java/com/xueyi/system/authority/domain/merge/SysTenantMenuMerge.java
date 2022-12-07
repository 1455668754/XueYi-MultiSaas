package com.xueyi.system.authority.domain.merge;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TBasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 租户-菜单关联 持久化对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant_menu_merge")
public class SysTenantMenuMerge extends TBasisEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 菜单Id */
    private Long menuId;

    public SysTenantMenuMerge(Long menuId) {
        this.menuId = menuId;
    }

}
