package com.xueyi.common.core.web.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
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

    @Serial
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

    /** 是否存在默认顶级（true存在 false不存在） */
    @TableField(exist = false)
    protected Boolean defaultNode;

    /** 子节点集合 */
    @TableField(exist = false)
    protected List<D> children;

    /** 原始祖籍列表 */
    @TableField(exist = false)
    protected String oldAncestors;

    /** 原始层级 */
    @TableField(exist = false)
    protected Integer oldLevel;
}
