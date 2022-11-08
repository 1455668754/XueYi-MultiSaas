package com.xueyi.common.core.web.tenant.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SubBase 租户基类
 *
 * @param <S> SubDto
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TSubBaseEntity<S> extends SubBaseEntity<S> {

    private static final long serialVersionUID = 1L;

    /** 租户Id */
    @TableField(exist = false)
    protected Long enterpriseId;

}
