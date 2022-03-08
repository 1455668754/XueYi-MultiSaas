package com.xueyi.job.util;

import cn.hutool.core.util.ObjectUtil;
import com.xueyi.common.core.constant.job.ScheduleConstants;
import com.xueyi.common.core.utils.ExceptionUtil;
import com.xueyi.common.core.utils.SpringUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.bean.BeanUtils;
import com.xueyi.job.domain.dto.SysJobDto;
import com.xueyi.job.domain.dto.SysJobLogDto;
import com.xueyi.job.service.ISysJobLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 抽象quartz调用
 *
 * @author xueyi
 */
public abstract class AbstractQuartzJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysJobDto sysJob = new SysJobDto();
        BeanUtils.copyBeanProp(sysJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try {
            before(context, sysJob);
            if (ObjectUtil.isNotNull(sysJob))
                doExecute(context, sysJob);
            after(context, sysJob, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, sysJob, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void before(JobExecutionContext context, SysJobDto sysJob) {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void after(JobExecutionContext context, SysJobDto sysJob, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final SysJobLogDto jobLog = new SysJobLogDto();
        jobLog.setName(sysJob.getName());
        jobLog.setJobGroup(sysJob.getJobGroup());
        jobLog.setInvokeTarget(sysJob.getInvokeTarget());
        jobLog.setStartTime(startTime);
        jobLog.setStopTime(new Date());
//        jobLog.setEnterpriseId(sysJob.getEnterpriseId());
        long runMs = jobLog.getStopTime().getTime() - jobLog.getStartTime().getTime();
        jobLog.setJobMessage(jobLog.getName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            jobLog.setStatus("1");
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            jobLog.setExceptionInfo(errorMsg);
        } else {
            jobLog.setStatus("0");
        }
        // 写入数据库当中
        SpringUtils.getBean(ISysJobLogService.class).insert(jobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, SysJobDto sysJob) throws Exception;
}