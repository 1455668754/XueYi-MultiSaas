package com.xueyi.job.api.domain.dto;

import com.xueyi.job.api.domain.po.SysJobLogPo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * 调度日志 数据传输对象
 *
 * @author xueyi
 */
public class SysJobLogDto extends SysJobLogPo {

    private static final long serialVersionUID = 1L;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 停止时间 */
    private LocalDateTime stopTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getStopTime()
    {
        return stopTime;
    }

    public void setStopTime(LocalDateTime stopTime)
    {
        this.stopTime = stopTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("jobGroup", getJobGroup())
                .append("invokeTarget", getInvokeTarget())
                .append("jobMessage", getJobMessage())
                .append("status", getStatus())
                .append("exceptionInfo", getExceptionInfo())
                .append("createTime", getCreateTime())
                .append("startTime", getStartTime())
                .append("stopTime", getStopTime())
                .toString();
    }
}
