package com.xueyi.common.security.auth.pool;

/**
 * 定时任务服务 权限标识常量
 *
 * @author xueyi
 */
public interface JobPool {

    /** 定时任务 - 调度任务管理 - 列表 */
    String SCHEDULE_JOB_LIST = "schedule:job:list";
    /** 定时任务 - 调度任务管理 - 详情 */
    String SCHEDULE_JOB_SINGLE = "schedule:job:single";
    /** 定时任务 - 调度任务管理 - 新增 */
    String SCHEDULE_JOB_ADD = "schedule:job:add";
    /** 定时任务 - 调度任务管理 - 修改 */
    String SCHEDULE_JOB_EDIT = "schedule:job:edit";
    /** 定时任务 - 调度任务管理 - 修改状态 */
    String SCHEDULE_JOB_ES = "schedule:job:es";
    /** 定时任务 - 调度任务管理 - 删除 */
    String SCHEDULE_JOB_DEL = "schedule:job:delete";
    /** 定时任务 - 调度任务管理 - 导入 */
    String SCHEDULE_JOB_IMPORT = "schedule:job:import";
    /** 定时任务 - 调度任务管理 - 导出 */
    String SCHEDULE_JOB_EXPORT = "schedule:job:export";
}
