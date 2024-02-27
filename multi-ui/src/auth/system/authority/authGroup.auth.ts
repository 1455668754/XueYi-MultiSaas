/** 权限组权限标识 */
export enum AuthGroupAuth {
  /** 系统服务 | 权限模块 | 租户权限组管理 | 列表 */
  LIST = 'FE:system:authority:authGroup:list',
  /** 系统服务 | 权限模块 | 租户权限组管理 | 详情 */
  SINGLE = 'FE:system:authority:authGroup:single',
  /** 系统服务 | 权限模块 | 租户权限组管理 | 新增 */
  ADD = 'FE:system:authority:authGroup:add',
  /** 系统服务 | 权限模块 | 租户权限组管理 | 修改 */
  EDIT = 'FE:system:authority:authGroup:edit',
  /** 系统服务 | 权限模块 | 租户权限组管理 | 状态修改 */
  ES = 'FE:system:authority:authGroup:es',
  /** 系统服务 | 权限模块 | 租户权限组管理 | 删除 */
  DEL = 'FE:system:authority:authGroup:del',
}
