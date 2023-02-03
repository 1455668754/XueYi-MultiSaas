package com.xueyi.system.api.authority.domain.query;

import com.xueyi.system.api.authority.domain.po.SysClientPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 客户端 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysClientQuery extends SysClientPo {

    @Serial
    private static final long serialVersionUID = 1L;
}
