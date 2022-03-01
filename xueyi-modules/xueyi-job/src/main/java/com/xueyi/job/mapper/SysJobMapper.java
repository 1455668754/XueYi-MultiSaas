package com.xueyi.job.mapper;

import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.job.domain.dto.SysJobDto;

import java.util.List;

/**
 * 调度任务管理 数据层
 *
 * @author xueyi
 */
public interface SysJobMapper extends BaseMapper<SysJobDto> {
    /**
     * 查询调度任务日志集合
     * 访问控制 j 租户查询
     *
     * @param job 调度信息
     * @return 操作日志集合
     */
    public List<SysJobDto> selectJobList(SysJobDto job);

    /**
     * 查询所有调度任务
     *
     * @return 调度任务列表
     */
    public List<SysJobDto> selectJobAll();

    /**
     * 通过调度Id查询调度任务信息
     *
     * @param jobId 调度Id
     * @return 角色对象信息
     */
    public SysJobDto selectJobById(Long jobId);

    /**
     * 通过调度Id删除调度任务信息
     *
     * @param jobId 调度Id
     * @return 结果
     */
    public int deleteJobById(Long jobId);

    /**
     * 批量删除调度任务信息
     *
     * @param ids 需要删除的数据Id
     * @return 结果
     */
    public int deleteJobByIds(Long[] ids);

    /**
     * 修改调度任务信息
     *
     * @param job 调度任务信息
     * @return 结果
     */
    public int updateJob(SysJobDto job);

    /**
     * 新增调度任务信息
     * 访问控制 empty 租户更新（无前缀） (控制方法位于 service/impl层 com.xueyi.job.service.impl)
     * @param job 调度任务信息
     * @return 结果
     */
    public int insertJob(SysJobDto job);
}
