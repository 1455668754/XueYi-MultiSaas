package com.xueyi.common.core.web.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xueyi.common.core.utils.core.ConvertUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.StrUtil;
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
    @JsonIgnore
    protected String ancestors;

    /** 层级 */
    protected Integer level;

    /** 是否存在默认顶级（true存在 false不存在） */
    @JsonIgnore
    @TableField(exist = false)
    protected Boolean defaultNode = Boolean.FALSE;

    /** 自定义顶级节点名称 */
    @JsonIgnore
    @TableField(exist = false)
    protected String topNodeName;

    /** 移除当前及子节点（true是 false否） */
    @JsonIgnore
    @TableField(exist = false)
    protected Boolean exNodes = Boolean.FALSE;

    /** 子节点集合 */
    @TableField(exist = false)
    protected List<D> children;

    /** 原始祖籍列表 */
    @JsonIgnore
    @TableField(exist = false)
    protected String oldAncestors;

    /** 原始层级 */
    @JsonIgnore
    @TableField(exist = false)
    protected Integer oldLevel;

    /** 获取当前节点的子节点对应的祖籍 */
    @JsonIgnore
    public String getChildAncestors() {
        return this.ancestors + StrUtil.COMMA + this.id;
    }

    /** 获取当前节点的子节点对应的原始祖籍 */
    @JsonIgnore
    public String getOldChildAncestors() {
        return this.oldAncestors + StrUtil.COMMA + this.id;
    }

    /** 计算节点变化的层级数 */
    @JsonIgnore
    public int getLevelChange() {
        return ConvertUtil.toInt(this.level, NumberUtil.Zero) - ConvertUtil.toInt(this.oldLevel, NumberUtil.Zero);
    }
}
