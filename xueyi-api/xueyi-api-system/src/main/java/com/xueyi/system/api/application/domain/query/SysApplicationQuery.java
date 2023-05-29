package com.xueyi.system.api.application.domain.query;

import com.xueyi.system.api.application.domain.po.SysApplicationPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 应用模块 | 应用 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysApplicationQuery extends SysApplicationPo {

    @Serial
    private static final long serialVersionUID = 1L;
}