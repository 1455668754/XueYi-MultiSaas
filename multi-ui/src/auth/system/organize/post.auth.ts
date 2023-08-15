/** 岗位权限标识 */
export enum PostAuth {
  // 查看岗位列表
  LIST = 'organize:post:list',
  // 查询岗位详情
  SINGLE = 'organize:post:single',
  // 新增岗位
  ADD = 'organize:post:add',
  // 修改岗位
  EDIT = 'organize:post:edit',
  // 修改岗位状态
  EDIT_STATUS = 'organize:post:es',
  // 岗位角色分配
  AUTH = 'organize:post:auth',
  // 删除岗位
  DELETE = 'organize:post:delete',
}
