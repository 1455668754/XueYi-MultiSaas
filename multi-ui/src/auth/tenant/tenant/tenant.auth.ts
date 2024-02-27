/** 租户权限标识 */
export enum TenantAuth {
  /** 租户服务 | 租户模块 | 租户管理 | 列表 */
  LIST = 'FE:tenant:tenant:tenant:list',
  /** 租户服务 | 租户模块 | 租户管理 | 详情 */
  SINGLE = 'FE:tenant:tenant:tenant:single',
  /** 租户服务 | 租户模块 | 租户管理 | 新增 */
  ADD = 'FE:tenant:tenant:tenant:add',
  /** 租户服务 | 租户模块 | 租户管理 | 修改 */
  EDIT = 'FE:tenant:tenant:tenant:edit',
  /** 租户服务 | 租户模块 | 租户管理 | 权限 */
  AUTH = 'FE:tenant:tenant:tenant:auth',
  /** 租户服务 | 租户模块 | 租户管理 | 修改状态 */
  ES = 'FE:tenant:tenant:tenant:es',
  /** 租户服务 | 租户模块 | 租户管理 | 删除 */
  DEL = 'FE:tenant:tenant:tenant:del',
}
