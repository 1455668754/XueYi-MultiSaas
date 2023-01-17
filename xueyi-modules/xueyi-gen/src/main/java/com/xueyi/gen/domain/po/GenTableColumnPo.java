package com.xueyi.gen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.xueyi.gen.domain.merge.MergeGroup.GEN_TABLE_GROUP;

/**
 * 业务字段 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "gen_table_column", excludeProperty = {"status", "remark", "delFlag"})
public class GenTableColumnPo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 归属表Id */
    @Correlation(groupName = GEN_TABLE_GROUP, keyType = OperateConstants.SubKeyType.SLAVE)
    protected Long tableId;

    /** 列描述 */
    protected String comment;

    /** 列类型 */
    protected String type;

    /** JAVA类型 */
    protected String javaType;

    /** JAVA字段名 */
    @NotBlank(message = "Java属性不能为空")
    protected String javaField;

    /** 主键字段（Y是 N否） */
    protected Boolean isPk;

    /** 自增字段（Y是 N否） */
    protected Boolean isIncrement;

    /** 必填字段（Y是 N否） */
    protected Boolean isRequired;

    /** 查看字段（Y是 N否） */
    protected Boolean isView;

    /** 新增字段（Y是 N否） */
    protected Boolean isInsert;

    /** 编辑字段（Y是 N否） */
    protected Boolean isEdit;

    /** 列表字段（Y是 N否） */
    protected Boolean isList;

    /** 查询字段（Y是 N否） */
    protected Boolean isQuery;

    /** 唯一字段（Y是 N否） */
    protected Boolean isUnique;

    /** 导入字段（1是 0否） */
    protected Boolean isImport;

    /** 导出字段（1是 0否） */
    protected Boolean isExport;

    /** 隐藏字段（1是 0否） */
    protected Boolean isHide;

    /** 掩蔽字段 - 仅针对基类（1是 0否） */
    protected Boolean isCover;

    /** 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围） */
    protected String queryType;

    /** 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件） */
    protected String htmlType;

    /** 字典类型 */
    protected String dictType;

}