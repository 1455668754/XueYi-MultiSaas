package com.xueyi.common.security.auth.pool;

/**
 * 定时任务服务 权限标识常量
 *
 * @author xueyi
 */
public interface JobPool {

    /** 定时任务 | 调度任务管理 | 列表 */
    String SCHEDULE_JOB_LIST = "RD:job:schedule:job:list";
    /** 定时任务 | 调度任务管理 | 详情 */
    String SCHEDULE_JOB_SINGLE = "RD:job:schedule:job:single";
    /** 定时任务 | 调度任务管理 | 调度日志 */
    String SCHEDULE_JOB_LOG = "RD:job:schedule:job:log";
    /** 定时任务 | 调度任务管理 | 新增 */
    String SCHEDULE_JOB_ADD = "RD:job:schedule:job:add";
    /** 定时任务 | 调度任务管理 | 修改 */
    String SCHEDULE_JOB_EDIT = "RD:job:schedule:job:edit";
    /** 定时任务 | 调度任务管理 | 状态修改 */
    String SCHEDULE_JOB_ES = "RD:job:schedule:job:es";
    /** 定时任务 | 调度任务管理 | 删除 */
    String SCHEDULE_JOB_DEL = "RD:job:schedule:job:del";

}
