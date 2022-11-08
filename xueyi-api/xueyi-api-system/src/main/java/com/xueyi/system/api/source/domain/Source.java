package com.xueyi.system.api.source.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 源策略 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "te_strategy", excludeProperty = {"name", "sort", "remark", "createBy", "createTime", "updateBy", "updateTime"})
public class Source extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主写源 */
    @TableField("source_slave")
    String master;

}