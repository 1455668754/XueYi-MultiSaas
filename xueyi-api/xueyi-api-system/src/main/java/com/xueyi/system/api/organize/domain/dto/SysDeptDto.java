package com.xueyi.system.api.organize.domain.dto;

import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.system.api.authority.domain.dto.SysRoleDto;
import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

import static com.xueyi.system.api.organize.domain.merge.MergeGroup.DEPT_SysPost_GROUP;

/**
 * 部门 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptDto extends SysDeptPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 角色对象 */
    private List<SysRoleDto> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 部门数据 */
    @Correlation(groupName = DEPT_SysPost_GROUP, keyType = OperateConstants.SubKeyType.RECEIVE)
    private List<SysPostDto> subList;

}
