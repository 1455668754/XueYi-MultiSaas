package com.xueyi.system.authority.domain.dto;

import com.xueyi.system.authority.domain.po.SysAuthGroupPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * 系统服务 | 权限模块 | 租户权限组 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysAuthGroupDto extends SysAuthGroupPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 权限Ids */
    private List<Long> authIds;

    /** 菜单权限Ids */
    private List<Long> menuIds;

    /** 模块权限Ids */
    private List<Long> moduleIds;
}