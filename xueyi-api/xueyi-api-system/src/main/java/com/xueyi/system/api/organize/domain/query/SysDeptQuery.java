package com.xueyi.system.api.organize.domain.query;

import com.xueyi.system.api.organize.domain.po.SysDeptPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 组织模块 | 部门 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptQuery extends SysDeptPo {

    @Serial
    private static final long serialVersionUID = 1L;

}
