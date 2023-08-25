package com.xueyi.system.api.authority.domain.dto;

import com.xueyi.system.api.authority.domain.po.SysRolePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 权限模块 | 角色 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleDto extends SysRolePo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 菜单权限Ids */
    private Long[] menuIds;

    /** 模块权限Ids */
    private Long[] moduleIds;

    /** 组织Ids（数据权限） */
    private Long[] organizeIds;

    /** 组织-部门Ids（数据权限） */
    private Long[] orgDeptIds;

    /** 组织-岗位Ids（数据权限） */
    private Long[] orgPostIds;
}
