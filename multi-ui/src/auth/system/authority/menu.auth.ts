/** 菜单权限标识 */
export enum MenuAuth {
  // 查看菜单列表
  LIST = 'authority:menu:list',
  // 查询菜单详情
  SINGLE = 'authority:menu:single',
  // 新增菜单
  ADD = 'authority:menu:add',
  // 修改菜单
  EDIT = 'authority:menu:edit',
  // 修改菜单状态
  EDIT_STATUS = 'authority:menu:es',
  // 删除菜单
  DELETE = 'authority:menu:delete',
}
