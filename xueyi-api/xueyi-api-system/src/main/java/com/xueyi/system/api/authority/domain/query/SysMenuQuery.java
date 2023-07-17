package com.xueyi.system.api.authority.domain.query;

import com.xueyi.system.api.authority.domain.po.SysMenuPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 权限模块 | 菜单 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuQuery extends SysMenuPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 菜单类型限制（M目录 C菜单 X详情 F按钮） */
    private String menuTypeLimit;
}
