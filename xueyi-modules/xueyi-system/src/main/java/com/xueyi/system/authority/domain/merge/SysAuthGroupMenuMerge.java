package com.xueyi.system.authority.domain.merge;

import com.baomidou.mybatisplus.annotation.*;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 系统服务 | 权限模块 | 企业权限组和菜单关联 持久化对象
 *
 * @author xueyi
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_auth_group_menu_merge")
public class SysAuthGroupMenuMerge extends BasisEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId
    protected Long id;

    /** 菜单Id */
    protected Long menuId;

    /** 租户权限组Id */
    protected Long authGroupId;

}