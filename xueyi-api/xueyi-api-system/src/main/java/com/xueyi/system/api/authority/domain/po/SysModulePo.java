package com.xueyi.system.api.authority.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xueyi.common.core.web.tenant.common.TCBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统服务 | 权限模块 | 模块 持久化对象
 *
 * @author xueyi
 */
@Data
@TableName("sys_module")
@EqualsAndHashCode(callSuper = true)
public class SysModulePo extends TCBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 模块logo */
    protected String logo;

    /** 路由地址 */
    protected String path;

    /** 路由参数 */
    protected String paramPath;

    /** 模块类型（0常规 1内嵌 2外链） */
    protected String type;

    /** 模块显隐状态（Y隐藏 N显示） */
    protected String hideModule;

    /** 默认模块（Y是 N否） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String isDefault;

    /** 租户Id */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    protected Long tenantId;

}
