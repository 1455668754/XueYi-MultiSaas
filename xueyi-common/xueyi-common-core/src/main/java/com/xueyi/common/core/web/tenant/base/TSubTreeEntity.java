package com.xueyi.common.core.web.tenant.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.base.SubTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SubTree 租户基类
 *
 * @param <D> Dto
 * @param <S> SubDto
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TSubTreeEntity<D, S> extends SubTreeEntity<D, S> {

    private static final long serialVersionUID = 1L;

    /** 租户Id */
    @TableField(exist = false)
    protected Long enterpriseId;

}
