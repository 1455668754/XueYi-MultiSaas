package com.xueyi.common.core.web.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Tree 基类
 *
 * @param <D> Dto
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeEntity<D> extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 父级Id */
    protected Long parentId;

    /** 父级名称 */
    @TableField(exist = false)
    protected String parentName;

    /** 祖籍列表 */
    protected String ancestors;

    /** 层级 */
    protected Integer level;

    /** 子节点集合 */
    @TableField(exist = false)
    protected List<D> children;

}
