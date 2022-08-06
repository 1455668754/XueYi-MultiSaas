package com.xueyi.common.core.web.entity.common;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.web.entity.base.BasisEntity;

/**
 * Base 混合基类
 *
 * @author xueyi
 */
public class CBasisEntity extends BasisEntity {

    private static final long serialVersionUID = 1L;

    /** 公共数据（0是 1否） */
    @TableField(value = "is_common", updateStrategy = FieldStrategy.NEVER)
    private String isCommon;

    public String getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }

    /** 校验是否为公共数据 */
    public boolean isCommon(){
        return StrUtil.equals(DictConstants.DicCommonPrivate.COMMON.getCode(), getIsCommon());
    }

    /** 校验是否非公共数据 */
    public boolean isNotCommon(){
        return !isCommon();
    }
}
