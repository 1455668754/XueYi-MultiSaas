/** 数据源权限标识 */
export enum SourceAuth {
  /** 租户服务 | 策略模块 | 数据源管理 | 列表 */
  LIST = 'FE:tenant:source:source:list',
  /** 租户服务 | 策略模块 | 数据源管理 | 详情 */
  SINGLE = 'FE:tenant:source:source:single',
  /** 租户服务 | 策略模块 | 数据源管理 | 新增 */
  ADD = 'FE:tenant:source:source:add',
  /** 租户服务 | 策略模块 | 数据源管理 | 修改 */
  EDIT = 'FE:tenant:source:source:edit',
  /** 租户服务 | 策略模块 | 数据源管理 | 状态修改 */
  ES = 'FE:tenant:source:source:es',
  /** 租户服务 | 策略模块 | 数据源管理 | 删除 */
  DEL = 'FE:tenant:source:source:del',
}
