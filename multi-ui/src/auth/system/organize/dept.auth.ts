/** 部门权限标识 */
export enum DeptAuth {
  /** 系统服务 | 组织模块 | 部门管理 | 列表 */
  LIST = 'FE:system:organize:dept:list',
  /** 系统服务 | 组织模块 | 部门管理 | 详情 */
  SINGLE = 'FE:system:organize:dept:single',
  /** 系统服务 | 组织模块 | 部门管理 | 新增 */
  ADD = 'FE:system:organize:dept:add',
  /** 系统服务 | 组织模块 | 部门管理 | 修改 */
  EDIT = 'FE:system:organize:dept:edit',
  /** 系统服务 | 组织模块 | 部门管理 | 权限 */
  AUTH = 'FE:system:organize:dept:auth',
  /** 系统服务 | 组织模块 | 部门管理 | 状态修改 */
  ES = 'FE:system:organize:dept:es',
  /** 系统服务 | 组织模块 | 部门管理 | 删除 */
  DEL = 'FE:system:organize:dept:del',
}
