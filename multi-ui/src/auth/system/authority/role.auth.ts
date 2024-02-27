/** 角色权限标识 */
export enum RoleAuth {
  /** 系统服务 | 权限模块 | 角色管理 | 列表 */
  LIST = 'FE:system:authority:role:list',
  /** 系统服务 | 权限模块 | 角色管理 | 详情 */
  SINGLE = 'FE:system:authority:role:single',
  /** 系统服务 | 权限模块 | 角色管理 | 新增 */
  ADD = 'FE:system:authority:role:add',
  /** 系统服务 | 权限模块 | 角色管理 | 修改 */
  EDIT = 'FE:system:authority:role:edit',
  /** 系统服务 | 权限模块 | 角色管理 | 权限 */
  AUTH = 'FE:system:authority:role:auth',
  /** 系统服务 | 权限模块 | 角色管理 | 状态修改 */
  ES = 'FE:system:authority:role:es',
  /** 系统服务 | 权限模块 | 角色管理 | 删除 */
  DEL = 'FE:system:authority:role:del',
}
