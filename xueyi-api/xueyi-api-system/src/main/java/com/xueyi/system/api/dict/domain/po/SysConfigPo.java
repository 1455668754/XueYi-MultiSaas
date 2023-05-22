package com.xueyi.system.api.dict.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
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
@TableName(value = "sys_config", excludeProperty = {"status"})
public class SysConfigPo extends TBaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 参数键名 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String code;

    /** 数据类型（0默认 1只读） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String dataType;

    /** 缓存类型（0租户 1全局） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String cacheType;

    /** 参数键值 */
    protected String value;

    /** 系统内置（Y是 N否） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String type;

}
