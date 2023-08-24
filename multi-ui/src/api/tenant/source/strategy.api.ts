import { StrategyIM, StrategyLRM, StrategyPPM } from '@/model/tenant/source';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_STRATEGY = '/tenant/admin/strategy/list',
  GET_STRATEGY = '/tenant/admin/strategy/',
  ADD_STRATEGY = '/tenant/admin/strategy',
  EDIT_STRATEGY = '/tenant/admin/strategy',
  EDIT_STATUS_STRATEGY = '/tenant/admin/strategy/status',
  DEL_BATCH_STRATEGY = '/tenant/admin/strategy/batch/',
}

/** 查询数据源策略列表 */
export const listStrategyApi = (params?: StrategyPPM) =>
  defHttp.get<StrategyLRM>({ url: Api.LIST_STRATEGY, params });

/** 查询数据源策略详细 */
export const getStrategyApi = (id: string) =>
  defHttp.get<StrategyIM>({ url: Api.GET_STRATEGY, params: id });

/** 新增数据源策略 */
export const addStrategyApi = (params: StrategyIM) =>
  defHttp.post({ url: Api.ADD_STRATEGY, params });

/** 修改数据源策略 */
export const editStrategyApi = (params: StrategyIM) =>
  defHttp.put({ url: Api.EDIT_STRATEGY, params });

/** 修改数据源策略状态 */
export const editStatusStrategyApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_STRATEGY,
    params: { id: id, status: status },
  });

/** 删除数据源策略 */
export const delStrategyApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_STRATEGY, params: ids.toString() });
