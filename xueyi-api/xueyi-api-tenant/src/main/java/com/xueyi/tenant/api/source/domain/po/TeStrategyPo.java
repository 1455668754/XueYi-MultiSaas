package com.xueyi.tenant.api.source.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 租户服务 | 策略模块 | 源策略 持久化对象
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
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected Long sourceId;

    /** 数据源编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String sourceSlave;

    /** 默认策略（Y是 N否） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String isDefault;

}