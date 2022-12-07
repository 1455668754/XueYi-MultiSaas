package com.xueyi.tenant.api.tenant.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 数据源策略 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("te_strategy")
public class TeStrategyPo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 数据源Id */
    protected Long sourceId;

    /** 数据源编码 */
    protected String sourceSlave;

    /** 默认策略（Y是 N否） */
    protected String isDefault;

}