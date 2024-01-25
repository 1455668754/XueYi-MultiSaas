package com.xueyi.system.api.dict.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.xueyi.common.core.constant.basic.EntityConstants.STATUS;

/**
 * 导入导出配置 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_im_ex", excludeProperty = { STATUS })
public class SysImExPo extends TBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 配置编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String code;

    /** 导入配置 */
    protected String importConfig;

    /** 导出配置 */
    protected String exportConfig;

    /** 数据类型（0默认 1只读） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String dataType;

    /** 缓存类型（0租户 1全局） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String cacheType;

    /** 租户Id */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    protected Long tenantId;
}