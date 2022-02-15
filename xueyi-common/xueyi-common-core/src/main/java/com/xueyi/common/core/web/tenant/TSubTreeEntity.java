package com.xueyi.common.core.web.tenant;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.SubTreeEntity;

import static com.xueyi.common.core.constant.TenantConstants.TENANT_ID;

/**
 * SubTree 租户基类
 *
 * @param <D> Dto
 * @param <S> SubDto
 * @author xueyi
 */
public class TSubTreeEntity<D, S> extends SubTreeEntity<D, S> {

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
