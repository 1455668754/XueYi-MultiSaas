package com.xueyi.system.api.log.domain.po;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 访问日志 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_login_log", excludeProperty = {"name", "sort", "createBy", "createTime", "updateBy", "updateTime", "remark"})
public class SysLoginLogPo extends TBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 企业账号 */
    protected String enterpriseName;

    /** 用户Id */
    protected Long userId;

    /** 用户账号 */
    @TableField(condition = LIKE)
    protected String userName;

    /** 用户名称 */
    @TableField(condition = LIKE)
    protected String userNick;

    /** 状态 0成功 1失败 */
    protected String status;

    /** 地址 */
    @TableField(condition = LIKE)
    protected String ipaddr;

    /** 描述 */
    protected String msg;

    /** 访问时间 */
    @OrderBy(sort = 10)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime accessTime;

}
