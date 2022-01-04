package com.xueyi.job.util;

import org.quartz.JobExecutionContext;

import com.xueyi.job.domain.dto.SysJobDto;

/**
 * 定时任务处理（允许并发执行）
 * 
 * @author xueyi
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJobDto sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
