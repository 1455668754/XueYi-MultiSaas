package com.xueyi.tenant.api.tenant.domain.query;

import com.xueyi.tenant.api.tenant.domain.po.TeTenantPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 租户服务 | 租户模块 | 租户 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeTenantQuery extends TeTenantPo {

    @Serial
    private static final long serialVersionUID = 1L;

}