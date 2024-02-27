/** 字典类型权限标识 */
export enum DictTypeAuth {
  /** 系统服务 | 字典模块 | 字典管理 | 列表 */
  LIST = 'FE:system:dict:dict:list',
  /** 系统服务 | 字典模块 | 字典管理 | 详情 */
  SINGLE = 'FE:system:dict:dict:single',
  /** 系统服务 | 字典模块 | 字典管理 | 新增 */
  ADD = 'FE:system:dict:dict:add',
  /** 系统服务 | 字典模块 | 字典管理 | 修改 */
  EDIT = 'FE:system:dict:dict:edit',
  /** 系统服务 | 字典模块 | 字典管理 | 状态修改 */
  ES = 'FE:system:dict:dict:es',
  /** 系统服务 | 字典模块 | 字典管理 | 删除 */
  DEL = 'FE:system:dict:dict:del',
  /** 系统服务 | 字典模块 | 字典管理 | 字典管理 */
  DICT = 'FE:system:dict:dict:dict',
}
