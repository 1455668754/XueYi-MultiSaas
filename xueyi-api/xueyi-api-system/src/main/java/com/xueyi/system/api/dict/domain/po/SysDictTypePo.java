package com.xueyi.system.api.dict.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Correlation;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;
import static com.xueyi.system.api.dict.domain.merge.MergeGroup.DICT_DATA_GROUP;

/**
 * 字典类型 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict_type")
public class SysDictTypePo extends TBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 字典类型 */
    @NotBlank(message = "字典类型不能为空")
    @Size(max = 100, message = "字典类型类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    @Correlation(groupName = DICT_DATA_GROUP, keyType = OperateConstants.SubKeyType.MAIN)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String code;

    /** 数据类型（0默认 1只增 2只减 3只读） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String dataType;

    /** 缓存类型（0租户 1全局） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String cacheType;

    /** 名称 */
    @TableField(condition = LIKE)
    @NotBlank(message = "字典名称不能为空")
    @Size(max = 100, message = "字典名称长度不能超过100个字符")
    protected String name;

}
