package com.xueyi.gen.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.constant.GenConstants;
import com.xueyi.gen.domain.po.GenTablePo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 业务 数据传输对象
 *
 * @author xueyi
 */
@TableName(value = "gen_table", excludeProperty = {"status", "sort", "delFlag"})
public class GenTableDto extends GenTablePo<GenTableColumnDto> {

    private static final long serialVersionUID = 1L;

    /** 主键信息 */
    @TableField(exist = false)
    private GenTableColumnDto pkColumn;

    /** 子表信息 */
    @TableField(exist = false)
    private GenTableDto subTable;

    /** 模块 | 菜单信息 */
    @TableField(exist = false)
    private String menuOptions;

    public GenTableColumnDto getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(GenTableColumnDto pkColumn) {
        this.pkColumn = pkColumn;
    }

    public GenTableDto getSubTable() {
        return subTable;
    }

    public void setSubTable(GenTableDto subTable) {
        this.subTable = subTable;
    }

    public String getMenuOptions() {
        return menuOptions;
    }

    public void setMenuOptions(String menuOptions) {
        this.menuOptions = menuOptions;
    }

    public boolean isBase() {
        return isBase(getTplCategory());
    }

    public static boolean isBase(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.BASE.getCode(), tplCategory);
    }

    public boolean isTree() {
        return isTree(getTplCategory());
    }

    public static boolean isTree(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.TREE.getCode(), tplCategory);
    }

    public boolean isSubBase() {
        return isSubBase(getTplCategory());
    }

    public static boolean isSubBase(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.SUB_BASE.getCode(), tplCategory);
    }

    public boolean isSubTree() {
        return isSubTree(getTplCategory());
    }

    public static boolean isSubTree(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.SUB_TREE.getCode(), tplCategory);
    }

    public boolean isMerge() {
        return isMerge(getTplCategory());
    }

    public static boolean isMerge(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.MERGE.getCode(), tplCategory);
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("comment", getComment())
                .append("className", getClassName())
                .append("prefix", getPrefix())
                .append("tplCategory", getTplCategory())
                .append("packageName", getPackageName())
                .append("moduleName", getModuleName())
                .append("businessName", getBusinessName())
                .append("functionName", getFunctionName())
                .append("functionAuthor", getFunctionAuthor())
                .append("genType", getGenType())
                .append("genPath", getGenPath())
                .append("options", getOptions())
                .append("pkColumn", getPkColumn())
                .append("subTable", getSubTable())
                .toString();
    }
}