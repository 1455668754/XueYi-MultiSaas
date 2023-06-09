import { SourceIM, SourcePPM, SourceLRM } from '/@/model/tenant';
import { defHttp } from '/@/utils/http/axios';

enum Api {
  LIST_SOURCE = '/tenant/source/list',
  OPTION_SOURCE = '/tenant/source/option',
  GET_SOURCE = '/tenant/source/',
  CONNECTION = '/tenant/source/connection',
  ADD_SOURCE = '/tenant/source',
  EDIT_SOURCE = '/tenant/source',
  EDIT_STATUS_SOURCE = '/tenant/source/status',
  DEL_BATCH_SOURCE = '/tenant/source/batch/',
}

/** 查询数据源列表 */
export const listSourceApi = (params?: SourcePPM) =>
  defHttp.get<SourceLRM>({ url: Api.LIST_SOURCE, params });

/** 查询数据源选择框列表 */
export const optionSourceApi = () => defHttp.get<SourceLRM>({ url: Api.OPTION_SOURCE });

/** 查询数据源详细 */
export const getSourceApi = (id: string | number) =>
  defHttp.get<SourceIM>({ url: Api.GET_SOURCE, params: id });

/** 数据源连接测试 */
export const connectionSourceApi = (params: SourceIM) =>
  defHttp.post({ url: Api.CONNECTION, params });

/** 新增数据源 */
export const addSourceApi = (params: SourceIM) => defHttp.post({ url: Api.ADD_SOURCE, params });

/** 修改数据源 */
export const editSourceApi = (params: SourceIM) => defHttp.put({ url: Api.EDIT_SOURCE, params });

/** 修改数据源状态 */
export const editStatusSourceApi = (id: string | number, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_SOURCE,
    params: { id: id, status: status },
  });

/** 删除数据源 */
export const delSourceApi = (ids: (string | number) | (string | number)[]) =>
  defHttp.delete({ url: Api.DEL_BATCH_SOURCE, params: ids.toString() });
