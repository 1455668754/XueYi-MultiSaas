package com.xueyi.tenant.api.domain.source.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.BaseEntity;

/**
 * 源策略 持久化对象
 *
 * @author xueyi
 */
public class TeStrategyPo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 默认源策略（Y是 N否） */
    @TableField("is_default")
    private String isDefault;

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}