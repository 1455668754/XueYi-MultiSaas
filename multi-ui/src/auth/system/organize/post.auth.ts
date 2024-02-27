/** 岗位权限标识 */
export enum PostAuth {
  /** 系统服务 | 组织模块 | 岗位管理 | 列表 */
  LIST = 'FE:system:organize:post:list',
  /** 系统服务 | 组织模块 | 岗位管理 | 详情 */
  SINGLE = 'FE:system:organize:post:single',
  /** 系统服务 | 组织模块 | 岗位管理 | 新增 */
  ADD = 'FE:system:organize:post:add',
  /** 系统服务 | 组织模块 | 岗位管理 | 修改 */
  EDIT = 'FE:system:organize:post:edit',
  /** 系统服务 | 组织模块 | 岗位管理 | 权限 */
  AUTH = 'FE:system:organize:post:auth',
  /** 系统服务 | 组织模块 | 岗位管理 | 状态修改 */
  ES = 'FE:system:organize:post:es',
  /** 系统服务 | 组织模块 | 岗位管理 | 删除 */
  DEL = 'FE:system:organize:post:del',
}
