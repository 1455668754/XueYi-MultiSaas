package com.xueyi.system.api.dict.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 参数配置 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_config", excludeProperty = {"status", "delFlag"})
public class SysConfigPo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 参数键名 */
    @Excel(name = "参数键名")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String code;

    /** 参数键值 */
    @Excel(name = "参数键值")
    protected String value;

    /** 系统内置（Y是 N否） */
    @Excel(name = "系统内置", readConverterExp = "Y=是,N=否")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String type;

}
