/** 用户权限标识 */
export enum UserAuth {
  /** 系统服务 | 组织模块 | 用户管理 | 列表 */
  LIST = 'FE:system:organize:user:list',
  /** 系统服务 | 组织模块 | 用户管理 | 详情 */
  SINGLE = 'FE:system:organize:user:single',
  /** 系统服务 | 组织模块 | 用户管理 | 新增 */
  ADD = 'FE:system:organize:user:add',
  /** 系统服务 | 组织模块 | 用户管理 | 修改 */
  EDIT = 'FE:system:organize:user:edit',
  /** 系统服务 | 组织模块 | 用户管理 | 权限 */
  AUTH = 'FE:system:organize:user:auth',
  /** 系统服务 | 组织模块 | 用户管理 | 状态修改 */
  ES = 'FE:system:organize:user:es',
  /** 系统服务 | 组织模块 | 用户管理 | 密码修改 */
  RES_PWD = 'FE:system:organize:user:rp',
  /** 系统服务 | 组织模块 | 用户管理 | 删除 */
  DEL = 'FE:system:organize:user:del',
  /** 系统服务 | 组织模块 | 用户管理 | 导入 */
  IMPORT = 'FE:system:organize:user:import',
  /** 系统服务 | 组织模块 | 用户管理 | 导出 */
  EXPORT = 'FE:system:organize:user:export',
}
