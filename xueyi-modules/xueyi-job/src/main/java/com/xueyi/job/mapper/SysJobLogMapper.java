package com.xueyi.job.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.job.domain.dto.SysJobLogDto;

import java.util.List;

/**
 * 调度任务日志管理 数据层
 *
 * @author xueyi
 */
public interface SysJobLogMapper extends BaseMapper<SysJobLogDto> {
    /**
     * 获取quartz调度器日志的计划任务
     * 访问控制 jl 租户查询
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @DataScope(eAlias = "jl")
    public List<SysJobLogDto> selectJobLogList(SysJobLogDto jobLog);

    /**
     * 查询所有调度任务日志
     *
     * @return 调度任务日志列表
     */
    public List<SysJobLogDto> selectJobLogAll();

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    public SysJobLogDto selectJobLogById(Long jobLogId);

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     * @return 结果
     */
    public int insertJobLog(SysJobLogDto jobLog);

    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteJobLogByIds(Long[] logIds);

    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     * @return 结果
     */
    public int deleteJobLogById(Long jobId);

    /**
     * 清空任务日志
     */
    public void cleanJobLog();
}
