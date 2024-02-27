/** 调度任务权限标识 */
export enum JobAuth {
  /** 定时任务 | 调度任务管理 | 列表 */
  LIST = 'FE:job:schedule:job:list',
  /** 定时任务 | 调度任务管理 | 详情 */
  SINGLE = 'FE:job:schedule:job:single',
  /** 定时任务 | 调度任务管理 | 调度日志 */
  LOG = 'FE:job:schedule:job:log',
  /** 定时任务 | 调度任务管理 | 新增 */
  ADD = 'FE:job:schedule:job:add',
  /** 定时任务 | 调度任务管理 | 修改 */
  EDIT = 'FE:job:schedule:job:edit',
  /** 定时任务 | 调度任务管理 | 状态修改 */
  ES = 'FE:job:schedule:job:es',
  /** 定时任务 | 调度任务管理 | 删除 */
  DEL = 'FE:job:schedule:job:del',
}
