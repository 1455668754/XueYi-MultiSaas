/** 模块权限标识 */
export enum ModuleAuth {
  /** 系统服务 | 权限模块 | 模块管理 | 列表 */
  LIST = 'FE:system:authority:module:list',
  /** 系统服务 | 权限模块 | 模块管理 | 详情 */
  SINGLE = 'FE:system:authority:module:single',
  /** 系统服务 | 权限模块 | 模块管理 | 新增 */
  ADD = 'FE:system:authority:module:add',
  /** 系统服务 | 权限模块 | 模块管理 | 修改 */
  EDIT = 'FE:system:authority:module:edit',
  /** 系统服务 | 权限模块 | 模块管理 | 状态修改 */
  ES = 'FE:system:authority:module:es',
  /** 系统服务 | 权限模块 | 模块管理 | 删除 */
  DEL = 'FE:system:authority:module:del',
}
