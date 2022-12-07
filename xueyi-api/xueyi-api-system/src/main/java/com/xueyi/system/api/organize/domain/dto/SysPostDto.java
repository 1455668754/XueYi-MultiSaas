package com.xueyi.system.api.organize.domain.dto;

import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.annotation.Excel.Type;
import com.xueyi.common.core.annotation.Excels;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.organize.domain.po.SysPostPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * 岗位 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPostDto extends SysPostPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 部门对象 */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT),
            @Excel(name = "部门编码(*)", targetAttr = "deptCode", type = Type.IMPORT)
    })
    private SysDeptDto dept;

    /** 角色对象 */
    private List<SysRoleDto> roles;

    /** 角色组 */
    private Long[] roleIds;

}
