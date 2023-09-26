import { NoticeIM, NoticeLRM, NoticePPM } from '@/model/system/notice';
import { defHttp } from '@/utils/http/axios';

const basicApi = '/system/admin/notice';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询通知公告列表 */
export const listNoticeApi = (params?: NoticePPM) =>
  defHttp.get<NoticeLRM>({ url: Api.LIST, params });

/** 查询通知公告详细 */
export const getNoticeApi = (id: string) => defHttp.get<NoticeIM>({ url: Api.GET, params: id });

/** 新增通知公告 */
export const addNoticeApi = (params: NoticeIM) => defHttp.post({ url: Api.ADD, params });

/** 修改通知公告 */
export const editNoticeApi = (params: NoticeIM) => defHttp.put({ url: Api.EDIT, params });

/** 删除通知公告 */
export const delNoticeApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
