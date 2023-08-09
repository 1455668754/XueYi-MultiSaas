import { defHttp } from '/@/utils/http/axios';
import { DictTypeIM, DictTypeLRM, DictTypePPM } from '@/model/tenant/dict';

enum Api {
  LIST_DICT_TYPE = '/system/admin/dict/type/list',
  OPTION_DICT_TYPE = '/system/admin/dict/type/option',
  GET_DICT_TYPE = '/system/admin/dict/type/',
  ADD_DICT_TYPE = '/system/admin/dict/type',
  EDIT_DICT_TYPE = '/system/admin/dict/type',
  EDIT_STATUS_DICT_TYPE = '/system/admin/dict/type/status',
  DEL_BATCH_DICT_TYPE = '/system/admin/dict/type/batch/',
  REFRESH_DICT = '/system/admin/dict/type/refresh',
}

/** 查询字典类型列表 */
export const listDictTypeApi = (params?: DictTypePPM) =>
  defHttp.get<DictTypeLRM>({ url: Api.LIST_DICT_TYPE, params });

/** 查询字典类型选择框列表 */
export const optionDictTypeApi = () => defHttp.get<DictTypeLRM>({ url: Api.OPTION_DICT_TYPE });

/** 查询字典类型详细 */
export const getDictTypeApi = (id: string) =>
  defHttp.get<DictTypeIM>({ url: Api.GET_DICT_TYPE, params: id });

/** 新增字典类型 */
export const addDictTypeApi = (params: DictTypeIM) =>
  defHttp.post({ url: Api.ADD_DICT_TYPE, params });

/** 修改字典类型 */
export const editDictTypeApi = (params: DictTypeIM) =>
  defHttp.put({ url: Api.EDIT_DICT_TYPE, params });

/** 修改字典类型状态 */
export const editStatusDictTypeApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_DICT_TYPE,
    params: { id: id, status: status },
  });

/** 删除字典类型 */
export const delDictTypeApi = (ids: string[]) =>
  defHttp.delete({
    url: Api.DEL_BATCH_DICT_TYPE,
    params: ids.toString(),
  });

/** 刷新字典缓存 */
export const refreshDictApi = () => defHttp.get({ url: Api.REFRESH_DICT });
