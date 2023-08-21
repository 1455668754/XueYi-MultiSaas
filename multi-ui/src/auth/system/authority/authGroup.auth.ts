/** 权限组权限标识 */
export enum AuthGroupAuth {
  // 查看权限组列表
  LIST = 'authority:authGroup:list',
  // 查询权限组详情
  SINGLE = 'authority:authGroup:single',
  // 新增权限组
  ADD = 'authority:authGroup:add',
  // 修改权限组
  EDIT = 'authority:authGroup:edit',
  // 修改权限组状态
  EDIT_STATUS = 'authority:authGroup:es',
  // 删除权限组
  DELETE = 'authority:authGroup:delete',
}
