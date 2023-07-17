import { defHttp } from '/@/utils/http/axios';
import { DictDataIM, DictDataLRM, DictDataPPM } from '@/model/tenant';

enum Api {
  LIST_DICT_DATA = '/system/admin/dict/data/list',
  GET_DICT_DATA = '/system/admin/dict/data/',
  ADD_DICT_DATA = '/system/admin/dict/data',
  EDIT_DICT_DATA = '/system/admin/dict/data',
  EDIT_STATUS_DICT_DATA = '/system/admin/dict/data/status',
  DEL_BATCH_DICT_DATA = '/system/admin/dict/data/batch/',
}

/** 查询字典数据列表 */
export const listDictDataApi = (params?: DictDataPPM) =>
  defHttp.get<DictDataLRM>({ url: Api.LIST_DICT_DATA, params });

/** 查询字典数据详细 */
export const getDictDataApi = (id: string) =>
  defHttp.get<DictDataIM>({ url: Api.GET_DICT_DATA, params: id });

/** 新增字典数据 */
export const addDictDataApi = (params: DictDataIM) =>
  defHttp.post({ url: Api.ADD_DICT_DATA, params });

/** 修改字典数据 */
export const editDictDataApi = (params: DictDataIM) =>
  defHttp.put({ url: Api.EDIT_DICT_DATA, params });

/** 修改字典数据状态 */
export const editStatusDictDataApi = (id: string, status: any) =>
  defHttp.put({
    url: Api.EDIT_STATUS_DICT_DATA,
    params: { id: id, status: status },
  });

/** 删除字典数据 */
export const delDictDataApi = (ids: string[]) =>
  defHttp.delete({
    url: Api.DEL_BATCH_DICT_DATA,
    params: ids.toString(),
  });
