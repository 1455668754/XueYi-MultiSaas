package com.xueyi.system.authority.domain.query;

import com.xueyi.system.authority.domain.po.SysAuthGroupPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 权限模块 | 租户权限组 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysAuthGroupQuery extends SysAuthGroupPo {

    @Serial
    private static final long serialVersionUID = 1L;
}