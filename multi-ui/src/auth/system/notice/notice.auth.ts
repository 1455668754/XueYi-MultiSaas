/** 通知公告权限标识 */
export enum NoticeAuth {
  // 查询通知公告列表
  LIST = 'system:notice:list',
  // 查看通知公告详情
  SINGLE = 'system:notice:single',
  // 新增通知公告
  ADD = 'system:notice:add',
  // 修改通知公告
  EDIT = 'system:notice:edit',
  // 删除通知公告
  DELETE = 'system:notice:delete',
}
