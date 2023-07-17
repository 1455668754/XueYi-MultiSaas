import { NoticeIM, NoticeLRM, NoticePPM } from '/@/model/system';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_NOTICE = '/system/notice/list',
  OPTION_NOTICE = '/system/notice/option',
  GET_NOTICE = '/system/notice/',
  ADD_NOTICE = '/system/notice',
  EDIT_NOTICE = '/system/notice',
  EDIT_STATUS_NOTICE = '/system/notice/status',
  DEL_BATCH_NOTICE = '/system/notice/batch/',
}

/** 查询通知公告列表 */
export const listNoticeApi = (params?: NoticePPM) =>
  defHttp.get<NoticeLRM>({ url: Api.LIST_NOTICE, params });

/** 查询通知公告选择框列表 */
export const optionNoticeApi = () => defHttp.get<NoticeLRM>({ url: Api.OPTION_NOTICE });

/** 查询通知公告详细 */
export const getNoticeApi = (id: string) =>
  defHttp.get<NoticeIM>({ url: Api.GET_NOTICE, params: id });

/** 新增通知公告 */
export const addNoticeApi = (params: NoticeIM) => defHttp.post({ url: Api.ADD_NOTICE, params });

/** 修改通知公告 */
export const editNoticeApi = (params: NoticeIM) => defHttp.put({ url: Api.EDIT_NOTICE, params });

/** 修改通知公告状态 */
export const editStatusNoticeApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_NOTICE,
    params: { id: id, status: status },
  });

/** 删除通知公告 */
export const delNoticeApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_NOTICE, params: ids.toString() });
