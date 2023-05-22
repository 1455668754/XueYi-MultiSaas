package com.xueyi.system.api.dict.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.xueyi.system.api.dict.domain.merge.MergeGroup.DICT_DATA_GROUP;

/**
 * 字典数据 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict_data", excludeProperty = {"name"})
public class SysDictDataPo extends TBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    @Correlation(groupName = DICT_DATA_GROUP, keyType = OperateConstants.SubKeyType.SLAVE)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String code;

    /** 数据键值 */
    protected String value;

    /** 数据标签 */
    protected String label;

    /** 样式属性（其他样式扩展） */
    protected String cssClass;

    /** 表格字典样式 */
    protected String listClass;

    /** 是否默认（Y是 N否） */
    protected String isDefault;

}