package com.xueyi.system.api.organize.domain.query;

import com.xueyi.system.api.organize.domain.po.SysUserPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 组织模块 | 用户 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserQuery extends SysUserPo {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 岗位Id - 查询 */
    private Long postId;

    /** 部门Id - 查询 */
    private Long deptId;
}
