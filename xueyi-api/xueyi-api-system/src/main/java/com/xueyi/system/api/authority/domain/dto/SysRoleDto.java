package com.xueyi.system.api.authority.domain.dto;

import com.xueyi.system.api.authority.domain.po.SysRolePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleDto extends SysRolePo {

    private static final long serialVersionUID = 1L;

    /** 权限Ids（菜单权限） */
    private Long[] authIds;

    /** 组织Ids（数据权限） */
    private Long[] organizeIds;

}
