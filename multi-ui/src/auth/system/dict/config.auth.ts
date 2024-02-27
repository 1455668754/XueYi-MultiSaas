/** 参数权限标识 */
export enum ConfigAuth {
  /** 系统服务 | 字典模块 | 参数管理 | 列表 */
  LIST = 'FE:system:dict:config:list',
  /** 系统服务 | 字典模块 | 参数管理 | 详情 */
  SINGLE = 'FE:system:dict:config:single',
  /** 系统服务 | 字典模块 | 参数管理 | 新增 */
  ADD = 'FE:system:dict:config:add',
  /** 系统服务 | 字典模块 | 参数管理 | 修改 */
  EDIT = 'FE:system:dict:config:edit',
  /** 系统服务 | 字典模块 | 参数管理 | 状态修改 */
  ES = 'FE:system:dict:config:es',
  /** 系统服务 | 字典模块 | 参数管理 | 删除 */
  DEL = 'FE:system:dict:config:del',
}
