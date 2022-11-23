package com.xueyi.gen.domain.dto;

import com.xueyi.common.core.annotation.SubRelation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.gen.GenConstants;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.gen.domain.po.GenTablePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static com.xueyi.system.api.constant.MergeConstants.GEN_TABLE_GROUP;

/**
 * 业务 数据传输对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GenTableDto extends GenTablePo {

    private static final long serialVersionUID = 1L;

    /** 主键信息 */
    private GenTableColumnDto pkColumn;

    /** 子表信息 */
    private GenTableDto subTable;

    /** 业务字段数据集合 */
    @SubRelation(groupName = GEN_TABLE_GROUP, keyType = OperateConstants.SubKeyType.RECEIVE_KEY)
    private List<GenTableColumnDto> subList;

    public static boolean isBase(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.BASE.getCode(), tplCategory);
    }

    public static boolean isTree(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.TREE.getCode(), tplCategory);
    }

    public static boolean isSubBase(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.SUB_BASE.getCode(), tplCategory);
    }

    public static boolean isSubTree(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.SUB_TREE.getCode(), tplCategory);
    }

    public static boolean isMerge(String tplCategory) {
        return tplCategory != null && StrUtil.equals(GenConstants.TemplateType.MERGE.getCode(), tplCategory);
    }

    /** 是否为单表 */
    public boolean isBase() {
        return isBase(getTplCategory());
    }

    /** 是否为树表 */
    public boolean isTree() {
        return isTree(getTplCategory());
    }

    /** 是否为主子单表 */
    public boolean isSubBase() {
        return isSubBase(getTplCategory());
    }

    /** 是否为主子树表 */
    public boolean isSubTree() {
        return isSubTree(getTplCategory());
    }

    /** 是否为关联表 */
    public boolean isMerge() {
        return isMerge(getTplCategory());
    }

}