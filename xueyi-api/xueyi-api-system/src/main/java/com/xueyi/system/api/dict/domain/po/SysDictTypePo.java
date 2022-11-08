package com.xueyi.system.api.dict.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.entity.base.SubBaseEntity;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 字典类型 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict_type", excludeProperty = {"delFlag"})
public class SysDictTypePo extends SubBaseEntity<SysDictDataDto> {

    private static final long serialVersionUID = 1L;

    /** 字典类型 */
    @Excel(name = "字典类型")
    @NotBlank(message = "字典类型不能为空")
    @Size(max = 100, message = "字典类型类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String code;

    /** 名称 */
    @TableField(condition = LIKE)
    @NotBlank(message = "字典名称不能为空")
    @Size(max = 100, message = "字典名称长度不能超过100个字符")
    protected String name;

}
