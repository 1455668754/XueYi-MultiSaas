package com.xueyi.system.api.authority.domain.query;

import com.xueyi.system.api.authority.domain.po.SysModulePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 模块 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysModuleQuery extends SysModulePo {

    @Serial
    private static final long serialVersionUID = 1L;

}
