import { StrategyIM, StrategyLRM, StrategyPPM } from '@/model/tenant/source';
import { defHttp } from '/@/utils/http/axios';

const basicApi = '/tenant/admin/strategy';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询数据源策略列表 */
export const listStrategyApi = (params?: StrategyPPM) =>
  defHttp.get<StrategyLRM>({ url: Api.LIST, params });

/** 查询数据源策略详细 */
export const getStrategyApi = (id: string) => defHttp.get<StrategyIM>({ url: Api.GET, params: id });

/** 新增数据源策略 */
export const addStrategyApi = (params: StrategyIM) => defHttp.post({ url: Api.ADD, params });

/** 修改数据源策略 */
export const editStrategyApi = (params: StrategyIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改数据源策略状态 */
export const editStatusStrategyApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除数据源策略 */
export const delStrategyApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
