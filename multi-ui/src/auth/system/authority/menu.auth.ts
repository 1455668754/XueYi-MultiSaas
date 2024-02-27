/** 菜单权限标识 */
export enum MenuAuth {
  /** 系统服务 | 权限模块 | 菜单管理 | 列表 */
  LIST = 'FE:system:authority:menu:list',
  /** 系统服务 | 权限模块 | 菜单管理 | 详情 */
  SINGLE = 'FE:system:authority:menu:single',
  /** 系统服务 | 权限模块 | 菜单管理 | 新增 */
  ADD = 'FE:system:authority:menu:add',
  /** 系统服务 | 权限模块 | 菜单管理 | 修改 */
  EDIT = 'FE:system:authority:menu:edit',
  /** 系统服务 | 权限模块 | 菜单管理 | 状态修改 */
  ES = 'FE:system:authority:menu:es',
  /** 系统服务 | 权限模块 | 菜单管理 | 删除 */
  DEL = 'FE:system:authority:menu:del',
}
