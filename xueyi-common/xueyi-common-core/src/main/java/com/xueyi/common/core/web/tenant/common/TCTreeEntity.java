package com.xueyi.common.core.web.tenant.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xueyi.common.core.web.entity.common.CTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Tree 租户混合基类
 *
 * @param <D> Dto
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TCTreeEntity<D> extends CTreeEntity<D> {

    private static final long serialVersionUID = 1L;

    /** 租户Id */
    @TableField(exist = false)
    protected Long enterpriseId;

}
