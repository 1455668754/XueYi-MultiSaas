import { defHttp } from '@/utils/http/axios';
import { DictTypeIM, DictTypeLRM, DictTypePPM } from '@/model/system/dict';

const basicApi = '/system/admin/dict/type';

enum Api {
  LIST = basicApi + '/list',
  GET = basicApi + '/',
  ADD = basicApi,
  EDIT = basicApi,
  EDIT_STATUS = basicApi + '/status',
  DEL_BATCH = basicApi + '/batch/',
  REFRESH_DICT = basicApi + '/refresh',
}

/** 查询字典类型列表 */
export const listDictTypeApi = (params?: DictTypePPM) =>
  defHttp.get<DictTypeLRM>({ url: Api.LIST, params });

/** 查询字典类型详细 */
export const getDictTypeApi = (id: string) => defHttp.get<DictTypeIM>({ url: Api.GET, params: id });

/** 新增字典类型 */
export const addDictTypeApi = (params: DictTypeIM) => defHttp.post({ url: Api.ADD, params });

/** 修改字典类型 */
export const editDictTypeApi = (params: DictTypeIM) => defHttp.put({ url: Api.EDIT, params });

/** 修改字典类型状态 */
export const editStatusDictTypeApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS,
    params: { id: id, status: status },
  });

/** 删除字典类型 */
export const delDictTypeApi = (ids: string[]) =>
  defHttp.delete({
    url: Api.DEL_BATCH,
    params: ids.toString(),
  });

/** 刷新字典缓存 */
export const refreshDictApi = () => defHttp.get({ url: Api.REFRESH_DICT });
