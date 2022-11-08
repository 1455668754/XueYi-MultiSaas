package com.xueyi.job.api.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xueyi.common.core.annotation.Excel;
import com.xueyi.common.core.web.tenant.base.TBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 调度日志 持久化对象
 *
 * @author xueyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_job_log", excludeProperty = {"createBy","updateBy","remark","updateTime","sort"})
public class SysJobLogPo extends TBaseEntity {

    private static final long serialVersionUID = 1L;

    /** 任务Id */
    @TableField("job_id")
    protected Long jobId;

    /** 任务组名 */
    @Excel(name = "任务组名")
    @TableField("job_group")
    protected String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串")
    @TableField("invoke_target")
    protected String invokeTarget;

    /** 调用租户字符串 */
    @TableField(value = "invoke_tenant")
    protected String invokeTenant;

    /** 日志信息 */
    @Excel(name = "日志信息")
    @TableField("job_message")
    protected String jobMessage;

    /** 执行状态（0正常 1失败） */
    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    @TableField("status")
    protected String status;

    /** 异常信息 */
    @Excel(name = "异常信息")
    @TableField("exception_info")
    protected String exceptionInfo;

}
