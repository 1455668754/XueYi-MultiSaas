/** 数据源策略权限标识 */
export enum StrategyAuth {
  /** 租户服务 | 策略模块 | 数据源策略管理 | 列表 */
  LIST = 'FE:tenant:source:strategy:list',
  /** 租户服务 | 策略模块 | 数据源策略管理 | 详情 */
  SINGLE = 'FE:tenant:source:strategy:single',
  /** 租户服务 | 策略模块 | 数据源策略管理 | 新增 */
  ADD = 'FE:tenant:source:strategy:add',
  /** 租户服务 | 策略模块 | 数据源策略管理 | 修改 */
  EDIT = 'FE:tenant:source:strategy:edit',
  /** 租户服务 | 策略模块 | 数据源策略管理 | 状态修改 */
  ES = 'FE:tenant:source:strategy:es',
  /** 租户服务 | 策略模块 | 数据源策略管理 | 删除 */
  DEL = 'FE:tenant:source:strategy:del',
}
