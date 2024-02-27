/** 操作日志权限标识 */
export enum OperateLogAuth {
  /** 系统服务 | 监控模块 | 操作日志管理 | 列表 */
  LIST = 'FE:system:monitor:operateLog:list',
  /** 系统服务 | 监控模块 | 操作日志管理 | 详情 */
  SINGLE = 'FE:system:monitor:operateLog:single',
  /** 系统服务 | 监控模块 | 操作日志管理 | 删除 */
  DEL = 'FE:system:monitor:operateLog:del',
  /** 系统服务 | 监控模块 | 操作日志管理 | 导出 */
  EXPORT = 'FE:system:monitor:operateLog:export',
}
