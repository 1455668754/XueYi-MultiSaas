/** 在线用户权限标识 */
export enum OnlineAuth {
  /** 系统服务 | 监控模块 | 在线用户管理 | 列表 */
  LIST = 'FE:system:monitor:online:list',
  /** 系统服务 | 监控模块 | 在线用户管理 | 强退 */
  FORCE_LOGOUT = 'FE:system:monitor:online:forceLogout',
}
