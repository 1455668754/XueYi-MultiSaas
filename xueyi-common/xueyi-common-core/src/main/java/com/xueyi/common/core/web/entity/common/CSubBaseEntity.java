package com.xueyi.common.core.web.entity.common;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SubBase 混合基类
 *
 * @param <S> SubDto
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CSubBaseEntity<S> extends SubBaseEntity<S> {

    private static final long serialVersionUID = 1L;

    /** 公共数据（Y是 N否） */
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
