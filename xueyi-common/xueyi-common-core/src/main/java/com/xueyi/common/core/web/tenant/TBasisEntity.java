package com.xueyi.common.core.web.tenant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.BasisEntity;

import static com.xueyi.common.core.constant.TenantConstants.TENANT_ID;

/**
 * Base 租户Po基类
 *
 * @author xueyi
 */
public class TBasisEntity extends BasisEntity {

    private static final long serialVersionUID = 1L;

    /** 租户Id */
    @TableField(value = TENANT_ID, select = false)
    private Long enterpriseId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

}
