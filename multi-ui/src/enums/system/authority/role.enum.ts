/** 角色主页路由 */
export const RoleIndexGo = '88c136711d98441699a6013ef27a356a';

/** 角色详情页路由 */
export const RoleDetailGo = '458c8e2ae43e47059b978504dd363a94';

/** 字典编码：角色 */
export enum DicCodeRoleEnum {
  // 系统开关
  AUTH_DATA_SCOPE = 'auth_data_scope',
  // 是否
  SYS_YES_NO = 'sys_yes_no',
}

/** 字典：数据范围（1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5本岗位数据权限  6仅本人数据权限） */
export enum DataScopeEnum {
  // 全部数据权限
  ALL = '1',
  // 自定义数据权限
  CUSTOM = '2',
  // 本部门数据权限
  DEPT = '3',
  // 本部门及以下数据权限
  DEPT_AND_CHILD = '4',
  // 本岗位数据权限
  POST = '5',
  // 仅本人数据权限'
  SELF = '6',
}
