package com.xueyi.common.core.web.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * SubBase 基类
 *
 * @param <S> SubDto
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubBaseEntity<S> extends BaseEntity {

    /** 子数据集合 */
    @TableField(exist = false)
    protected List<S> subList;

}
