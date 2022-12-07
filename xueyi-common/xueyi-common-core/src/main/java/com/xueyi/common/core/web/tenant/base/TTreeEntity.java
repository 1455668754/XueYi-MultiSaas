package com.xueyi.common.core.web.tenant.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xueyi.common.core.web.entity.base.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Tree 租户基类
 *
 * @param <D> Dto
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TTreeEntity<D> extends TreeEntity<D> {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 租户Id */
    @JsonIgnore
    @TableField(exist = false)
    protected Long enterpriseId;

}
