package com.xueyi.common.core.web.entity.common;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base 混合基类
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CBasisEntity extends BasisEntity {

    private static final long serialVersionUID = 1L;

    /** 公共数据（0是 1否） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String isCommon;

    /** 校验是否为公共数据 */
    public boolean isCommon(){
        return StrUtil.equals(DictConstants.DicCommonPrivate.COMMON.getCode(), getIsCommon());
    }

    /** 校验是否非公共数据 */
    public boolean isNotCommon(){
        return !isCommon();
    }
}
