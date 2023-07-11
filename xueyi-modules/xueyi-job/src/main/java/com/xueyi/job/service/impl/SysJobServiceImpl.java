package com.xueyi.job.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.constant.job.ScheduleConstants;
import com.xueyi.common.core.exception.job.TaskException;
import com.xueyi.common.core.utils.core.StrUtil;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.correlate.contant.CorrelateConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.job.api.domain.dto.SysJobDto;
import com.xueyi.job.api.domain.query.SysJobQuery;
import com.xueyi.job.domain.correlate.SysJobCorrelate;
import com.xueyi.job.manager.impl.SysJobManagerImpl;
import com.xueyi.job.service.ISysJobService;
import com.xueyi.job.util.ScheduleUtil;
import jakarta.annotation.PostConstruct;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.xueyi.common.core.constant.basic.SecurityConstants.CREATE_BY;

/**
 * 调度任务管理 服务层实现
 *
 * @author xueyi
 */
@Service
public class SysJobServiceImpl extends BaseServiceImpl<SysJobQuery, SysJobDto, SysJobCorrelate, SysJobManagerImpl> implements ISysJobService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 默认方法关联配置定义
     */
    @Override
    protected Map<CorrelateConstants.ServiceType, SysJobCorrelate> defaultCorrelate() {
        return new HashMap<>() {{
            put(CorrelateConstants.ServiceType.DELETE, SysJobCorrelate.BASE_DEL);
        }};
    }

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库Id和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<SysJobDto> jobList = baseManager.initScheduler();
        for (SysJobDto job : jobList)
            ScheduleUtil.createScheduleJob(scheduler, job);
    }

    /**
     * 查询调度任务对象列表 | 数据权限 | 附加数据
     *
     * @param job 调度任务对象
     * @return 调度任务对象集合
     */
    @Override
    @DataScope(userAlias = CREATE_BY, mapperScope = "SysJobMapper")
    public List<SysJobDto> selectListScope(SysJobQuery job) {
        return super.selectListScope(job);
    }

    /**
     * 暂停任务
     *
     * @param job 调度信息
     * @return 结果
     */
    @Override
    @DSTransactional
    public int pauseJob(SysJobDto job) throws SchedulerException {
        job.setStatus(ScheduleConstants.Status.PAUSE.getCode());
        int row = baseManager.updateStatus(job);
        if (row > 0) scheduler.pauseJob(ScheduleUtil.getJobKey(job.getId(), job.getJobGroup()));
        return row;
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     * @return 结果
     */
    @Override
    @DSTransactional
    public int resumeJob(SysJobDto job) throws SchedulerException {
        job.setStatus(ScheduleConstants.Status.NORMAL.getCode());
        int row = baseManager.updateStatus(job);
        if (row > 0) scheduler.resumeJob(ScheduleUtil.getJobKey(job.getId(), job.getJobGroup()));
        return row;
    }

    /**
     * 立即运行任务
     *
     * @param id Id
     */
    @Override
    @DSTransactional
    public boolean run(Long id) throws SchedulerException {
        SysJobDto job = baseManager.selectById(id);
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, job);
        JobKey jobKey = ScheduleUtil.getJobKey(job.getId(), job.getJobGroup());
        if (scheduler.checkExists(jobKey)) {
            scheduler.triggerJob(jobKey, dataMap);
            return true;
        }
        return false;
    }


    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    private void updateSchedulerJob(SysJobDto job, String jobGroup) throws SchedulerException, TaskException {
        // 判断是否存在
        JobKey jobKey = ScheduleUtil.getJobKey(job.getId(), jobGroup);
        // 防止创建时存在数据问题 先移除，然后在执行创建操作
        if (scheduler.checkExists(jobKey)) scheduler.deleteJob(jobKey);
        ScheduleUtil.createScheduleJob(scheduler, job);
    }

    /**
     * 初始化调用租户字符串
     *
     * @param job 任务对象
     */
    private void initInvokeTenant(SysJobDto job) {
        job.setInvokeTenant(SecurityUtils.getEnterpriseId() + "L, '" + SecurityUtils.getIsLessor() + "', '" + SecurityUtils.getSourceName() + "'");
    }

    /**
     * 单条操作 - 开始处理
     *
     * @param operate   服务层 - 操作类型
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    @Override
    protected void startHandle(OperateConstants.ServiceType operate, SysJobDto originDto, SysJobDto newDto) {
        if (Objects.requireNonNull(operate) == OperateConstants.ServiceType.ADD) {
            newDto.setStatus(ScheduleConstants.Status.PAUSE.getCode());
            initInvokeTenant(newDto);
        }
    }

    /**
     * 单条操作 - 结束处理
     *
     * @param operate   服务层 - 操作类型
     * @param row       操作数据条数
     * @param originDto 源数据对象（新增时不存在）
     * @param newDto    新数据对象（删除时不存在）
     */
    @Override
    protected void endHandle(OperateConstants.ServiceType operate, int row, SysJobDto originDto, SysJobDto newDto) {
        if (row <= 0)
            return;
        switch (operate) {
            case ADD -> {
                try {
                    ScheduleUtil.createScheduleJob(scheduler, newDto);
                } catch (SchedulerException | TaskException e) {
                    throw new RuntimeException(e);
                }
            }
            case EDIT -> {
                newDto.setInvokeTenant(originDto.getInvokeTenant());
                try {
                    updateSchedulerJob(newDto, originDto.getJobGroup());
                } catch (SchedulerException | TaskException e) {
                    throw new RuntimeException(e);
                }
            }
            case EDIT_STATUS -> {
                if (StrUtil.notEquals(originDto.getStatus(), newDto.getStatus())) {
                    try {
                        if (StrUtil.equals(newDto.getStatus(), ScheduleConstants.Status.NORMAL.getCode())) {
                            resumeJob(originDto);
                        } else {
                            pauseJob(originDto);
                        }
                    } catch (SchedulerException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case DELETE -> {
                try {
                    scheduler.deleteJob(ScheduleUtil.getJobKey(originDto.getId(), originDto.getJobGroup()));
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        super.endHandle(operate, row, originDto, newDto);
    }

    /**
     * 批量操作 - 结束处理
     *
     * @param operate    服务层 - 操作类型
     * @param rows       操作数据条数
     * @param originList 源数据对象集合（新增时不存在）
     * @param newList    新数据对象集合（删除时不存在）
     */
    @Override
    protected void endBatchHandle(OperateConstants.ServiceType operate, int rows, Collection<SysJobDto> originList, Collection<SysJobDto> newList) {
        if (rows <= 0)
            return;
        if (Objects.requireNonNull(operate) == OperateConstants.ServiceType.BATCH_DELETE) {
            for (SysJobDto job : originList) {
                try {
                    scheduler.deleteJob(ScheduleUtil.getJobKey(job.getId(), job.getJobGroup()));
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        super.endBatchHandle(operate, rows, originList, newList);
    }
}