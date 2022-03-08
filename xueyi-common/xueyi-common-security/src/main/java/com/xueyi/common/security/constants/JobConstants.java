package com.xueyi.common.security.constants;

/**
 * 定时任务模块 权限标识常量
 *
 * @author xueyi
 */
public interface JobConstants {

    /** 定时任务 - 调度任务管理 - 列表 */
    String JOB_JOB_LIST = "monitor:job:list";
    /** 定时任务 - 调度任务管理 - 详情 */
    String JOB_JOB_SINGLE = "monitor:job:single";
    /** 定时任务 - 调度任务管理 - 新增 */
    String JOB_JOB_ADD = "monitor:job:add";
    /** 定时任务 - 调度任务管理 - 修改 */
    String JOB_JOB_EDIT = "monitor:job:edit";
    /** 定时任务 - 调度任务管理 - 修改状态 */
    String JOB_JOB_EDIT_STATUS = "monitor:job:es";
    /** 定时任务 - 调度任务管理 - 删除 */
    String JOB_JOB_DELETE = "monitor:job:delete";
    /** 定时任务 - 调度任务管理 - 导入 */
    String JOB_JOB_IMPORT = "monitor:job:import";
    /** 定时任务 - 调度任务管理 - 导出 */
    String JOB_JOB_EXPORT = "monitor:job:export";
}
