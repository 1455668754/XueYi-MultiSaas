package com.xueyi.tenant.api.source.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 租户服务 | 策略模块 | 数据源 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("te_source")
public class TeSourcePo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 数据源编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String slave;

    /** 驱动 */
    protected String driverClassName;

    /** 连接地址 */
    protected String urlPrepend;

    /** 连接参数 */
    protected String urlAppend;

    /** 用户名 */
    protected String userName;

    /** 密码 */
    protected String password;

    /** 默认数据源（Y是 N否） */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    protected String isDefault;

}