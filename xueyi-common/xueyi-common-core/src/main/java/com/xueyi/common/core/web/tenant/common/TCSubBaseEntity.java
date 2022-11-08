package com.xueyi.common.core.web.tenant.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.common.CSubBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SubBase 租户混合基类
 *
 * @param <S> SubDto
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TCSubBaseEntity<S> extends CSubBaseEntity<S> {

    private static final long serialVersionUID = 1L;

    /** 租户Id */
    @TableField(exist = false)
    protected Long enterpriseId;

}
