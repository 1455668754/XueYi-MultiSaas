import { OnlineLRM, OnlinePPM } from '@/model/system/monitor';
import { defHttp } from '@/utils/http/axios';

const basicApi = '/system/admin/online';

enum Api {
  LIST = basicApi + '/list',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询在线用户列表 */
export const listOnlineApi = (params?: OnlinePPM) =>
  defHttp.get<OnlineLRM>({ url: Api.LIST, params });

/** 强退在线用户 */
export const delOnlineApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
