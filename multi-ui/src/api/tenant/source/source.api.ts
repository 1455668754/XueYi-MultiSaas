import { SourceIM, SourceLRM, SourcePPM } from '@/model/tenant/source';
import { defHttp } from '@/utils/http/axios';

const basicApi = '/tenant/admin/source';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  CONNECTION = basicApi + '/connection',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
}

/** 查询数据源列表 */
export const listSourceApi = (params?: SourcePPM) =>
  defHttp.get<SourceLRM>({ url: Api.LIST, params });

/** 查询数据源详细 */
export const getSourceApi = (id: string) => defHttp.get<SourceIM>({ url: Api.GET, params: id });

/** 数据源连接测试 */
export const connectionSourceApi = (params: SourceIM) =>
  defHttp.post({ url: Api.CONNECTION, params });

/** 新增数据源 */
export const addSourceApi = (params: SourceIM) => defHttp.post({ url: Api.ADD, params });

/** 修改数据源 */
export const editSourceApi = (params: SourceIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改数据源状态 */
export const editStatusSourceApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除数据源 */
export const delSourceApi = (ids: string | string[]) =>
  defHttp.delete({ url: Api.DEL_BATCH, params: ids.toString() });
