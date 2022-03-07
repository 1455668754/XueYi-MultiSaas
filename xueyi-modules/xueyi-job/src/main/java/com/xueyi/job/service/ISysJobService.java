package com.xueyi.job.service;

import com.xueyi.common.core.exception.job.TaskException;
import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.job.domain.dto.SysJobDto;
import org.quartz.SchedulerException;

/**
 * 调度任务管理 服务层
 *
 * @author xueyi
 */
public interface ISysJobService extends IBaseService<SysJobDto> {

    /**
     * 暂停任务
     *
     * @param job 调度信息
     * @return 结果
     */
    int pauseJob(SysJobDto job) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param job 调度信息
     * @return 结果
     */
    int resumeJob(SysJobDto job) throws SchedulerException;

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param job 调度信息
     * @return 结果
     */
    int deleteJob(SysJobDto job) throws SchedulerException;

    /**
     * 批量删除调度信息
     *
     * @param jobIds 需要删除的任务ID
     */
    void deleteJobByIds(Long[] jobIds) throws SchedulerException;

    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     * @return 结果
     */
    int changeStatus(SysJobDto job) throws SchedulerException;

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     */
    void run(SysJobDto job) throws SchedulerException;

    /**
     * 新增任务
     *
     * @param job 调度信息
     * @return 结果
     */
    int insertJob(SysJobDto job) throws SchedulerException, TaskException;

    /**
     * 更新任务
     *
     * @param job 调度信息
     * @return 结果
     */
    int updateJob(SysJobDto job) throws SchedulerException, TaskException;

}