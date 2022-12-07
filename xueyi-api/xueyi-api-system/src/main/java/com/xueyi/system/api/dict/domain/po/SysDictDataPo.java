package com.xueyi.system.api.dict.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.annotation.SubRelation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.xueyi.system.api.constant.MergeConstants.DICT_DATA_GROUP;

/**
 * 字典数据 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict_data", excludeProperty = {"name", "delFlag"})
public class SysDictDataPo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    @Excel(name = "字典编码")
    @SubRelation(groupName = DICT_DATA_GROUP, keyType = OperateConstants.SubKeyType.SUB_KEY)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String code;

    /** 数据键值 */
    @Excel(name = "数据键值")
    protected String value;

    /** 数据标签 */
    @Excel(name = "数据标签")
    protected String label;

    /** 样式属性（其他样式扩展） */
    protected String cssClass;

    /** 表格字典样式 */
    protected String listClass;

    /** 是否默认（Y是 N否） */
    @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
    protected String isDefault;

}