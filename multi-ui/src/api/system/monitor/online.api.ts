import { OnlineLRM, OnlinePPM } from '@/model/system/monitor';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_ONLINE = '/system/admin/online/list',
  DEL_BATCH_ONLINE = '/system/admin/online/batch/',
}

/** 查询在线用户列表 */
export const listOnlineApi = (params?: OnlinePPM) =>
  defHttp.get<OnlineLRM>({ url: Api.LIST_ONLINE, params });

/** 强退在线用户 */
export const delOnlineApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_ONLINE, params: ids.toString() });
