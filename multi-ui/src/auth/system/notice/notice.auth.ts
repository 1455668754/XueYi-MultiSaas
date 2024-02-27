/** 通知公告权限标识 */
export enum NoticeAuth {
  /** 系统服务 | 消息模块 | 通知公告管理 | 列表 */
  LIST = 'FE:system:notice:notice:list',
  /** 系统服务 | 消息模块 | 通知公告管理 | 详情 */
  SINGLE = 'FE:system:notice:notice:single',
  /** 系统服务 | 消息模块 | 通知公告管理 | 新增 */
  ADD = 'FE:system:notice:notice:add',
  /** 系统服务 | 消息模块 | 通知公告管理 | 修改 */
  EDIT = 'FE:system:notice:notice:edit',
  /** 系统服务 | 消息模块 | 通知公告管理 | 删除 */
  DEL = 'FE:system:notice:notice:del',
}
