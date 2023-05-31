package com.xueyi.tenant.api.source.domain.query;

import com.xueyi.tenant.api.source.domain.po.TeSourcePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 租户服务 | 策略模块 | 数据源 数据查询对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeSourceQuery extends TeSourcePo {

    @Serial
    private static final long serialVersionUID = 1L;

}