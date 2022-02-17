package com.xueyi.common.core.web.entity.common;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.base.BasisEntity;

/**
 * Base 混合基类
 *
 * @author xueyi
 */
public class CBasisEntity extends BasisEntity {

    private static final long serialVersionUID = 1L;

    /** 公共数据（Y是 N否） */
    @TableField(value = "is_common", updateStrategy = FieldStrategy.NEVER)
    private String isCommon;

    public String getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }

}
