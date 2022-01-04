package com.xueyi.job.util;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

import com.xueyi.job.domain.dto.SysJobDto;

/**
 * 定时任务处理（禁止并发执行）
 * 
 * @author xueyi
 *
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJobDto sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
