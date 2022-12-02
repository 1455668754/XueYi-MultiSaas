package com.xueyi.common.core.web.tenant.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xueyi.common.core.web.entity.base.BasisEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Basis 租户基类
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TBasisEntity extends BasisEntity {

    private static final long serialVersionUID = 1L;

    /** 租户Id */
    @JsonIgnore
    @TableField(exist = false)
    protected Long enterpriseId;

}
