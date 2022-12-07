package com.xueyi.system.api.organize.domain.query;

import com.xueyi.system.api.organize.domain.po.SysPostPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 岗位 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPostQuery extends SysPostPo {

    @Serial
    private static final long serialVersionUID = 1L;

}
