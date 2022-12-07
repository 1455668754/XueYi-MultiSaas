package com.xueyi.tenant.api.tenant.domain.query;

import com.xueyi.tenant.api.tenant.domain.po.TeStrategyPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 数据源策略 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeStrategyQuery extends TeStrategyPo {

    @Serial
    private static final long serialVersionUID = 1L;

}