package com.xueyi.system.api.authority.domain.query;

import com.xueyi.system.api.authority.domain.po.SysRolePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 角色 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleQuery extends SysRolePo {

    @Serial
    private static final long serialVersionUID = 1L;

}
