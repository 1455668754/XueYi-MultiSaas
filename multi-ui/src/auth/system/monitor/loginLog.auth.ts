/** 登录日志权限标识 */
export enum LoginLogAuth {
  /** 系统服务 | 监控模块 | 访问日志管理 | 列表 */
  LIST = 'FE:system:monitor:loginLog:list',
  /** 系统服务 | 监控模块 | 访问日志管理 | 删除 */
  DEL = 'FE:system:monitor:loginLog:del',
  /** 系统服务 | 监控模块 | 访问日志管理 | 导出 */
  EXPORT = 'FE:system:monitor:loginLog:export',
}
